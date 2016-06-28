/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.git;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@SuppressWarnings("ALL")
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
}
