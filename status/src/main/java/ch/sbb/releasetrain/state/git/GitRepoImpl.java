/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.state.git;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


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


    @FunctionalInterface
    private interface GitConsumer<T> {
        void accept(T t) throws IOException, GitAPIException;
    }

    public GitRepoImpl(final String url, final String branch, final String user, final String password) {
        this(url, branch, user, password, new File(System.getProperty("java.io.tmpdir"), filenameFromUrl(url)));
    }

    GitRepoImpl(final String url, final String branch, final String user, final String password, final File tempDir) {
        this.url = url;
        this.branch = branch;
        this.user = user;
        this.password = password;
        this.gitDir = tempDir;
    }


    static String filenameFromUrl(final String url) {
        return url.trim().replaceAll("[\\W]", "_");
    }

    boolean isCloned() {
        return new File(gitDir, ".git").exists();
    }

    @Override
    public File directory() {
        return gitDir;
    }

    public void cloneOrPull() {
        callWithRetry(c -> doCloneOrPull(), 0);
    }

    private void doCloneOrPull() throws IOException, GitAPIException {
        if (isCloned()) {
            Git git = pull(gitOpen());
            checkoutOrCreateBranch(git);
        }
        else {
            Git git = gitClone();
            checkoutOrCreateBranch(git);
        }
    }

    public void addCommitPush() {
        callWithRetry(c -> doAddCommitPush(), 0);
    }

    public void doAddCommitPush() throws IOException, GitAPIException {
        Git git = gitOpen();
        gitOpen().add()
                .addFilepattern(".")
                .call();
        git.commit()
                .setMessage("Automatic commit by releasetrain")
                .call();
        git.push()
                .setCredentialsProvider(credentialsProvider())
                .call();
    }

    private void callWithRetry(final GitConsumer call, int retry) {
        try {
            call.accept(null);
        }
        catch (IOException | TransportException e) {
            if (retry < 3) {
                try {
                    Thread.sleep(1000L * (retry + 1)); // back off
                    callWithRetry(call, retry + 1);
                } catch (InterruptedException e1) { // bad luck...
                }
            } else {
                throw new GitException(String.format("Git operation falied after %d retries", retry), e);
            }
        }
        catch (GitAPIException e) {
            throw new GitException("Git operation failed", e);
        }
    }

    Git pull(Git git) throws GitAPIException {
        if(remoteBranchExists(git)) {
            PullResult result = git
                    .pull()
                    .setStrategy(MergeStrategy.THEIRS)
                    .call();
            if (result.isSuccessful()) {
                return git;
            }
            else {
                throw new GitException("Pull failed: " + result.toString());
            }
        }
        else {
            return git;
        }
    }

    Git checkoutOrCreateBranch(final Git git) throws GitAPIException, IOException {
        if (!branch.equals(git.getRepository().getBranch())) {
            CheckoutCommand checkoutCommand = git.checkout()
                    .setCreateBranch(true)
                    .setName(branch)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK);

            if (remoteBranchExists(git)) {
                checkoutCommand.setStartPoint("origin/" + branch);
            }
            checkoutCommand.call();
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
        return Git.cloneRepository()
                .setURI(url)
                .setCredentialsProvider(credentialsProvider())
                .setDirectory(gitDir)
                .call();
    }

    private Git gitOpen() throws IOException {
        return Git.open(new File(gitDir, ".git"));
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
            git.checkout().
                    setCreateBranch(true).
                    setName("feature/temp_" + UUID.randomUUID()).
                    call();
            List<String> deletedBranches = git.branchDelete().setBranchNames(branch).setForce(true).call();
            if (deletedBranches.size() == 1) {
                // delete branch 'branchToDelete' on remote 'origin'
                RefSpec refSpec = new RefSpec()
                        .setSource(null)
                        .setDestination("refs/heads/" + branch);
                Iterable<PushResult> results = git.push()
                        .setCredentialsProvider(credentialsProvider())
                        .setRefSpecs(refSpec)
                        .setRemote("origin")
                        .call();
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


}
