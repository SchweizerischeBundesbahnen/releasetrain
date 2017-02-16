/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.git;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Access a git repository via JGit.
 *
 * @author u206123 (Florian Seidl)
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
public final class GitRepoImpl implements GitRepo {
	
	private final File gitDir;

	private final String url;

	private final String branch;

	private final String user;

	private final String password;

	private Git git;

	GitRepoImpl(final String url, final String branch, final String user, final String password, final File tempDir) {
		this.url = url;
		this.branch = branch;
		this.user = user;
		this.password = password;
		this.gitDir = tempDir;
	}

	boolean isCloned() {
		return new File(gitDir, ".git").exists();
	}

	@Override
	public File directory() {
		return gitDir;
	}

	@Override
	public void reset() {
		try {
			if (git == null) {
				return;
			}
			git.close();
			FileUtils.deleteDirectory(this.gitDir);
		} catch (IOException e) {
			log.error("not able to delete folder: " + this.gitDir, e);
		}
	}

	@Override
	public void cloneOrPull() {
		if (isCloned()) {
			Git git = null;
			try {
				git = pull(gitOpen());
			} catch (GitAPIException e) {
				log.error(e.getMessage(),e);
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
			this.git = git;
			checkoutOrCreateBranch(git);
		} else {
			Git git = null;
			try {
				git = gitClone();
			} catch (GitAPIException e) {
				log.error(e.getMessage(),e);
			}
			checkoutOrCreateBranch(git);
		}
	}

	@Override
	public void addCommitPush() {
		try {
			Git git = gitOpen();

			git.add().addFilepattern(".").call();

			// status
			Status status=git.status().call();

			// rm the deleted ones
			if(status.getMissing().size() >0){
				for(String rm : status.getMissing()){
					git.rm().addFilepattern(rm).call();
				}
			}

			// commit and push if needed
			if(!status.hasUncommittedChanges()){
				log.debug("not commiting git, because there are no changes");
				return;
			}

			git.commit().setMessage("Automatic commit by releasetrain").call();
			Iterable<PushResult> pushResults = git.push().setCredentialsProvider(credentialsProvider()).call();
			
			
			for (PushResult pushResult : pushResults) {
			    for (RemoteRefUpdate update : pushResult.getRemoteUpdates()) {
				if (!update.getStatus().equals(RemoteRefUpdate.Status.OK)) {
				    log.error("GIT PUSH nicht erfolgreich. Status={} Message={}, Details={}", update.getStatus(), update.getMessage(), pushResult.getMessages());
				    throw new TransportException(update.getMessage());
				}
			    }
			}
			
			
		} catch (Exception e) {
			throw new GitException(e.getMessage(), e);
		}
	}

	private Git pull(Git git) throws GitAPIException {
		if (remoteBranchExists(git)) {
			PullResult result = git.pull().setStrategy(MergeStrategy.RECURSIVE).setCredentialsProvider(this.credentialsProvider()).call();
			if (result.isSuccessful()) {
				return git;
			} else {
				log.info("*** pulling git, not able to merge with MergeStrategy.RECURSIVE go for MergeStrategy.THEIRS");
				result = git.pull().setStrategy(MergeStrategy.THEIRS).setCredentialsProvider(this.credentialsProvider()).call();
				if(result.isSuccessful()){
					return git;
				}
				throw new GitException("Pull failed: " + result.toString());
			}
		} else {
			return git;
		}
	}

	private Git checkoutOrCreateBranch(final Git git) {
		try {
		if (!branch.equals(git.getRepository().getBranch())) {
			CheckoutCommand checkoutCommand = git.checkout().setCreateBranch(true).setName(branch).setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK);

			if (remoteBranchExists(git)) {
				checkoutCommand.setStartPoint("origin/" + branch);
			}
				checkoutCommand.call();
		}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return git;
	}

	private boolean remoteBranchExists(final Git git) throws GitAPIException {
		List<Ref> branchRefs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
		final String refsHeadBranch = "refs/remotes/origin/" + branch;
		for (Ref branchRef : branchRefs) {
			if (refsHeadBranch.equals(branchRef.getName())) {
				return true;
			}
		}
		return false;
	}

	private Git gitClone() throws GitAPIException {
		return Git.cloneRepository().setURI(url).setCredentialsProvider(credentialsProvider()).setDirectory(gitDir).call();
	}

	private Git gitOpen() throws IOException {
		if (this.git == null) {
			this.git = Git.open(new File(gitDir, ".git"));
		}
		return git;
	}

	private CredentialsProvider credentialsProvider() {
		return new UsernamePasswordCredentialsProvider(user, password);
	}

	/**
	 * Use with care!
	 */
	public boolean deleteBranch() {
		if (!branch.startsWith("feature/")) {
			throw new GitException("Can only delete feature branch.");
		}
		try {

			final Git git = gitOpen();
			git.checkout().setCreateBranch(true).setName("feature/temp_" + UUID.randomUUID()).call();
			List<String> deletedBranches = git.branchDelete().setBranchNames(branch).setForce(true).call();
			if (deletedBranches.size() == 1) {
				// delete branch 'branchToDelete' on remote 'origin'
				RefSpec refSpec = new RefSpec().setSource(null).setDestination("refs/heads/" + branch);
				Iterable<PushResult> results = git.push().setCredentialsProvider(credentialsProvider()).setRefSpecs(refSpec).setRemote("origin").call();
				for (PushResult result : results) {
					RemoteRefUpdate myUpdate = result.getRemoteUpdate("refs/heads/" + branch);
					if (myUpdate.isDelete() && myUpdate.getStatus() == RemoteRefUpdate.Status.OK) {
						return true;
					}
				}
			}
			return false;
		} catch (IOException | GitAPIException e) {
			throw new GitException("Delete branch failed", e);
		}
	}

	@FunctionalInterface
	private interface GitConsumer<T> {
		void accept(T t) throws IOException, GitAPIException;
	}

}
