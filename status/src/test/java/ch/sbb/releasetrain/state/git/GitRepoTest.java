/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.git;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



public class GitRepoTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void isClonedEmpty() throws IOException {
        assertFalse(new GitRepoImpl("", "", "", "", temporaryFolder.newFolder("leer")).isCloned());
    }


    @Test
    public void isCloned() throws IOException {
        File dir = temporaryFolder.newFolder("cloned");
        new File(dir, ".git").mkdir();
        assertTrue(new GitRepoImpl("", "", "", "", dir).isCloned());
    }


    @Test
    public void filenameFromUrlHttp() {
        assertEquals("https___github_com_SchweizerischeBundesbahnen_releasetrain_git",
                    GitRepoImpl.filenameFromUrl("https://github.com/SchweizerischeBundesbahnen/releasetrain.git"));

    }

    @Test
    public void filenameFromUrlSSH() {
        assertEquals("git_github_com_SchweizerischeBundesbahnen_releasetrain_git",
                GitRepoImpl.filenameFromUrl("git@github.com:SchweizerischeBundesbahnen/releasetrain.git\n"));

    }

}
