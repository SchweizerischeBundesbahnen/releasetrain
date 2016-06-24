/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.git;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class GitRepoIT {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    public String gitToken = System.getProperty("git.token");

    @Before
    public void checkGitToken() {
        if(gitToken == null) {
            throw new IllegalArgumentException("Missing parameter git.token, please set jvm property -Dgit.token=<your.github.token>");
        }
    }

    @Test
    public void cloneReleasetrainRepo() throws Exception {


        assertFalse(new File(temporaryFolder.getRoot(), ".git").exists());

        GitRepoImpl git = new GitRepoImpl("https://github.com/SchweizerischeBundesbahnen/releasetrain.git", " feature/testgitclient", gitToken, "", temporaryFolder.getRoot());
        git.cloneOrPull();
        System.out.println(temporaryFolder.getRoot().toString());

        assertTrue(new File(temporaryFolder.getRoot(), ".git").exists());
        assertTrue(new File(temporaryFolder.getRoot(), "pom.xml").exists());
    }

    @Test
    public void commitAndPushReleasetrainRepoNoChange() throws Exception {

        assertFalse(new File(temporaryFolder.getRoot(), ".git").exists());

        GitRepoImpl git = new GitRepoImpl("https://github.com/SchweizerischeBundesbahnen/releasetrain.git", "feature/testgitclient", gitToken, "", temporaryFolder.getRoot());
        git.cloneOrPull();
        git.commitAndPush();
    }
                //git.cloneOrPull("pom.xml", "develop").contains("<modelVersion>4.0.0</modelVersion>"));

        // Assert.assertTrue(git.writeFile("pom.xml","housi"));



}
