/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.git;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
@Slf4j
public final class GITAccessorImpl implements GitAccessor {

    @Setter
    private String repo = "";

    private File gitDir = Files.createTempDir();

    @Setter
    private String branch = "master";

    @Setter
    private String user = "master";

    @Setter
    private String password = "master";

    public GITAccessorImpl() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("deletig: " + getGitDir());
                try {
                    org.apache.commons.io.FileUtils.deleteDirectory(gitDir);
                } catch (IOException e) {
                    log.debug(e.getMessage(), e);
                }
                log.info("deleted: " + getGitDir());
            }
        });
    }

    public boolean writeFile(String pathAndFile, String content, String startingPoint) {
        if (!startingPoint.contains("origin")) {
            startingPoint = "origin/" + startingPoint;
        }

        Git git = null;
        try {
            git = gitClone(gitDir);
            if (git == null) {
                log.error("git not initialized!");
                return false;
            }
            git.checkout().
                    setCreateBranch(true).
                    setName(branch).
                    setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                    setStartPoint(startingPoint).
                    call();
        } catch (GitAPIException e) {
            log.info(e.getMessage());
        }

        try {
            git.pull().call();
        } catch (GitAPIException e) {
            log.info(e.getMessage());
        }

        try {
            FileUtils.writeStringToFile(new File(gitDir.getAbsoluteFile(), pathAndFile), content);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        if (git == null) {
            log.error("git not initialized!");
            return false;
        }
        try {
            git.add().addFilepattern(".").call();
            git.commit().setMessage("commit").call();
            PushCommand command = git.push().setForce(true);
            command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
            command.call();
        } catch (GitAPIException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public void connectToRepoAndBranch(String user, String password, String branch) {

    }

    @Override
    public boolean writeAndCommitFile(String pathAndFile, String content) {
        return false;
    }

    @Override
    public String readFileToString(String pathAndFile, String startingPoint) {
        if (!startingPoint.contains("origin")) {
            startingPoint = "origin/" + startingPoint;
        }
        File tempDir = Files.createTempDir();
        try {
            this.gitClone(tempDir);
            Git git = gitOpen(tempDir);
            Ref ref = git.checkout().
                    setCreateBranch(true).
                    setName(branch).
                    setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                    setStartPoint(startingPoint).
                    call();
        } catch (GitAPIException e) {
            log.error(e.getMessage(), e);
        }
        try {
            return IOUtils.toString(new FileReader(new File(tempDir, pathAndFile)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public void wipeGitWorkspace(File fileToPurge, String... save) {
        if (fileToPurge == null) {
            fileToPurge = gitDir;
        }
        for (File file : fileToPurge.listFiles()) {
            if (file.isDirectory()) {
                wipeGitWorkspace(file);
            }
            deleteFile(file, save);
        }
    }

    private void deleteFile(File file, String[] save) {
        List<String> list = Arrays.asList(save);
        list = new ArrayList<>(list);
        list.add(".git");
        String name = "";
        try {
            name = file.getCanonicalPath();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        for (String blocker : list) {
            if (name.contains(blocker)) {
                return;
            }
        }
        file.delete();
    }

    public void stageAndPushDeletedFile() {
        try {
            Git git = this.gitOpen(gitDir);
            git.add().addFilepattern(".").setUpdate(true).call();
            git.commit().setMessage("wipe").call();
            PushCommand command = git.push().setForce(true);
            command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
            command.call();
        } catch (GitAPIException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Git gitClone(File dir) {
        Git git = gitOpen(dir);
        if (git != null) {
            log.info("already cloned ...");
            return git;
        }

        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI(repo);
        cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
        cloneCommand.setDirectory(dir);
        try {
            cloneCommand.call();
        } catch (GitAPIException e) {
            log.error(e.getMessage(), e);
        }
        return gitOpen(dir);
    }

    private Git gitOpen(File dir) {
        Git git = null;
        if (new File(dir, ".git").exists()) {
            log.debug("* git repo is here, will not clone");
            try {
                git = Git.open(new File(dir, ".git"));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            return git;
        }
        return null;
    }
}
