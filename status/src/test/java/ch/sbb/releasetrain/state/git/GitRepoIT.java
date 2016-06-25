/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class GitRepoIT {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    public String gitToken = System.getProperty("git.token");

    private GitRepoImpl gitRepo;

    private File workspace;

    @Before
    public void initGitRepo() throws IOException {
        assertNotNull("Missing parameter git.token, please set jvm property -Dgit.token=<your.github.token>", gitToken);
        workspace = temporaryFolder.newFolder("default");
        assertFalse(String.format("Folder %s not empty", workspace), new File(workspace, ".git").exists());
        gitRepo = createGitRepo(workspace);
    }

    @After
    public void deleteTestBranch() {
        if(gitRepo != null) {
            gitRepo.deleteBranch();
        }
    }

    private GitRepoImpl createGitRepo(final File checkoutDir) throws IOException {
        return new GitRepoImpl("https://github.com/SchweizerischeBundesbahnen/releasetrain.git",
                "feature/testbranchpleaseignore", gitToken, "", checkoutDir);
    }

    @Test
    public void cloneReleasetrainRepo() throws Exception {
        gitRepo.cloneOrPull();

        assertTrue(new File(workspace, ".git").exists());
        assertTrue(new File(workspace, "pom.xml").exists());
    }

    @Test
    public void commitAndPushReleasetrainRepoNoChange() throws Exception {

        assertFalse(new File(workspace, ".git").exists());

        gitRepo.cloneOrPull();
        gitRepo.addCommitPush();
    }

    @Test
    public void commitAndPushReleasetrainRepoAddedFile() throws Exception {

        assertFalse(new File(workspace, ".git").exists());

        gitRepo.cloneOrPull();
        new File(workspace, "bla.txt").createNewFile();

        assertTrue(new File(workspace, "bla.txt").exists());
        gitRepo.addCommitPush();
    }

    @Test
    public void commitAndPushReleasetrainRepoAddedFileInDir() throws Exception {

        assertFalse(new File(workspace, ".git").exists());

        gitRepo.cloneOrPull();
        new File(workspace, "my/dir").mkdirs();
        new File(workspace, "my/dir/bla.txt").createNewFile();

        assertTrue(new File(new File(new File(workspace, "my"), "dir"), "bla.txt").exists());
        gitRepo.addCommitPush();
    }

    @Test
    public void commitAndPushReleasetrainRepoAddedFileCheckout() throws Exception {

        assertFalse(new File(workspace, ".git").exists());

        gitRepo.cloneOrPull();
        new File(workspace, "bla.txt").createNewFile();

        gitRepo.addCommitPush();

        final File recheckoutWorkspace = recheckout();

        assertTrue(new File(recheckoutWorkspace, "bla.txt").exists());
    }

    @Test
    public void commitAndPushReleasetrainRepoAddedAndModifiedFile() throws Exception {

        assertFalse(new File(workspace, ".git").exists());

        gitRepo.cloneOrPull();
        new File(workspace, "bla.txt").createNewFile();

        FileUtils.writeStringToFile(new File(workspace, "bla.txt"), "bliblablo");
        assertEquals("bliblablo", FileUtils.readFileToString(new File(workspace, "bla.txt")));

        gitRepo.addCommitPush();
    }

    @Test
    public void commitAndPushReleasetrainRepoAddedFileCheckoutModify() throws Exception {

        assertFalse(new File(workspace, ".git").exists());

        gitRepo.cloneOrPull();
        new File(workspace, "bla.txt").createNewFile();

        FileUtils.writeStringToFile(new File(workspace, "bla.txt"), "hotzenplotz");
        gitRepo.addCommitPush();

        final File recheckoutWorkspace = recheckout();

        assertEquals("hotzenplotz", FileUtils.readFileToString(new File(recheckoutWorkspace, "bla.txt")));
    }


    @Test
    public void cloneReleasetrainRepoAndPull() throws Exception {
        gitRepo.cloneOrPull();
        assertTrue(new File(workspace, ".git").exists());
        gitRepo.cloneOrPull();
        assertTrue(new File(workspace, ".git").exists());
    }

    @Test
    public void cloneReleasetrainRepoPushAndPull() throws Exception {
        gitRepo.cloneOrPull();
        new File(workspace, "bla.txt").createNewFile();
        gitRepo.addCommitPush();
        gitRepo.cloneOrPull();
        assertTrue(new File(workspace, "bla.txt").exists());
    }

    private File recheckout() throws IOException {
        final File recheckoutWorkspace = temporaryFolder.newFolder("recheckout");

        createGitRepo(recheckoutWorkspace).cloneOrPull();

        return recheckoutWorkspace;

    }


}
