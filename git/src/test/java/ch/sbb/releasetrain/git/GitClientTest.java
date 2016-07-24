/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.git;

import org.junit.Assert;
import org.junit.Test;

import ch.sbb.releasetrain.git.GitClientImpl;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public class GitClientTest {
    @Test
    public void filenameFromUrlHttp() {
        Assert.assertEquals("https___github_com_SchweizerischeBundesbahnen_releasetrain_git_default",
                GitClientImpl.filenameFromUrl("https://github.com/SchweizerischeBundesbahnen/releasetrain.git", "default"));

    }

    @Test
    public void filenameFromUrlSSH() {
        Assert.assertEquals("git_github_com_SchweizerischeBundesbahnen_releasetrain_git__default",
                GitClientImpl.filenameFromUrl("git@github.com:SchweizerischeBundesbahnen/releasetrain.git\n", "default"));

    }
}
