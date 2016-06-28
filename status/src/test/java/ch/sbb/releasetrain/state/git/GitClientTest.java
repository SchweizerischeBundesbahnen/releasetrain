/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.git;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public class GitClientTest {
    @Test
    public void filenameFromUrlHttp() {
        assertEquals("https___github_com_SchweizerischeBundesbahnen_releasetrain_git",
                GitClientImpl.filenameFromUrl("https://github.com/SchweizerischeBundesbahnen/releasetrain.git"));

    }

    @Test
    public void filenameFromUrlSSH() {
        assertEquals("git_github_com_SchweizerischeBundesbahnen_releasetrain_git",
                GitClientImpl.filenameFromUrl("git@github.com:SchweizerischeBundesbahnen/releasetrain.git\n"));

    }
}
