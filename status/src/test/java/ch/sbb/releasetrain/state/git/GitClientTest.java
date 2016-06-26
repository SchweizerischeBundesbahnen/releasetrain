/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.git;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
