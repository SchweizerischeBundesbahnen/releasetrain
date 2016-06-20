/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.git;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import org.apache.maven.shared.utils.io.FileUtils;
import org.apache.maven.shared.utils.io.IOUtil;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.google.common.io.Files;

/**
 * reading and writig a file from/to a git repo, we are cloning  from origin/master
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@CommonsLog
public final class GITAccessorImpl implements GitAccessor {

    @Setter
    private String repoKey = "git.repo.url";

    @Setter
    private File gitDir = Files.createTempDir();

    @Setter
    private String branch = "master";

    @Setter
    private String user = "master";

    @Setter
    private String password = "master";

    public boolean writeFile(String pathAndFile, String content) {

        try {
            this.gitClone(gitDir);
            Git git = gitOpen(gitDir);
            if (git == null) {
                log.error("git not initialized!");
                return false;
            }
            git.checkout().
                    setCreateBranch(true).
                    setName(branch).
                    setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                    setStartPoint("origin/master").
                    call();
        } catch (GitAPIException e) {
            log.error(e.getMessage(), e);
        }

        try {
            FileUtils.fileWrite(gitDir.getAbsoluteFile() + "/" + pathAndFile, content);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        Git git = gitOpen(gitDir);
        if (git == null) {
            log.error("git not initialized!");
            return false;
        }
        try {
            git.add().addFilepattern(".").call();
            git.commit().setMessage("commit").call();
            PushCommand command = git.push();
            command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
            command.call();
        } catch (GitAPIException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public String readFileToString(String pathAndFile) {
        File tempDir = Files.createTempDir();
        try {
            this.gitClone(tempDir);
            Git git = gitOpen(tempDir);
            Ref ref = git.checkout().
                    setCreateBranch(true).
                    setName(branch).
                    setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                    setStartPoint("origin/master").
                    call();
        } catch (GitAPIException e) {
            log.error(e.getMessage(), e);
        }
        try {
            return IOUtil.toString(new FileReader(new File(tempDir, pathAndFile)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    private void gitClone(File dir) {
        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI("https://github.com/SchweizerischeBundesbahnen/releasetrain.git");
        cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
        cloneCommand.setDirectory(dir);
        try {
            cloneCommand.call();
        } catch (GitAPIException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Git gitOpen(File dir) {
        Git git = null;
        if (new File(dir, ".git").exists()) {
            log.debug("* git repo is here, will not clone");
            try {
                git = Git.open(new File(dir, ".git"));
            } catch (IOException e) {
                log.error(e);
            }
            return git;
        }
        return null;
    }
}
