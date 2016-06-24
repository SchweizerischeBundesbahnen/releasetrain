/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.state.git;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


/**
 * reading and writig a file from/to a git url, we are cloning  from origin/master
 *
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

    public void cloneOrPull() {
        if (isCloned()) {
            pull();
            checkoutOrCreateBranch();
        }
        else {
            gitClone();
      //      checkoutOrCreateBranch();
        }
    }

    void pull() {
        try {
            Git git = gitOpen();
            git.pull().call();
        } catch (GitAPIException | IOException e) {
            throw new GitException("Pull failed", e);
        }
    }

    void checkoutOrCreateBranch() {
        try {
            Git git = gitOpen();
            if(!branch.equals(git.getRepository().getBranch())) {
                git.checkout().
                        setCreateBranch(true).
                        setName(branch).
                        setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                        call();
            }
        } catch (GitAPIException | IOException e) {
            throw new GitException("Checkout falied", e);
        }
    }

    public void commitAndPush() {
        try {
            Git git = this.gitOpen();
            git.commit().setMessage("Automatic commit by releasetrain").call();
            PushCommand command = git.push();
            command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
            command.call();
        } catch (GitAPIException | IOException e) {
            throw new GitException(e);
        }
    }

    private Git gitClone() {
        try {
            CloneCommand cloneCommand = Git.cloneRepository();
            cloneCommand.setURI(url);
            cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
            cloneCommand.setDirectory(gitDir);
            cloneCommand.call();
            return gitOpen();
        } catch (GitAPIException | IOException e) {
            throw new GitException("Clone failed", e);
        }
    }

    private Git gitOpen() throws IOException {
        return Git.open(new File(gitDir, ".git"));
    }
}
