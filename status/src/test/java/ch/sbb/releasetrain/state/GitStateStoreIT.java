/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state;

import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.state.git.GitClientImpl;
import ch.sbb.releasetrain.state.git.GitRepo;
import ch.sbb.releasetrain.state.git.GitRepoImpl;
import ch.sbb.releasetrain.state.model.ReleaseState;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public class GitStateStoreIT {

    private static final String URL = "https://github.com/SchweizerischeBundesbahnen/releasetrain.git";
    private static final String BRANCH = "feature/testbranchstorepleaseignore";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    public String gitToken = System.getProperty("git.token");

    private GitStateStore gitStateStore;

    private GitClientImpl gitClient;

    @After
    public void deleteTestBranch() {
        if(gitClient != null) {
            GitRepoImpl gitRepo = (GitRepoImpl) gitClient.gitRepo(URL, BRANCH, gitToken, "");
            gitRepo.deleteBranch();
        }
    }

    @Before
    public void initGitStatusStore() throws IOException {
        assertNotNull("Missing parameter git.token, please set jvm property -Dgit.token=<your.github.token>", gitToken);
        gitClient = new GitClientImpl();
        gitStateStore = new GitStateStore(gitClient, new StateStoreConfig(URL,BRANCH,gitToken,""));
    }

    @Test
    public void write() {
        gitStateStore.writeReleaseStatus(new ReleaseState("myFirstRelease", Collections.<ActionConfig>emptyList()));
    }

    @Test
    public void writeAndRead() {
        ReleaseState releaseState  = new ReleaseState("myFirstRelease", Collections.<ActionConfig>emptyList());
        gitStateStore.writeReleaseStatus(releaseState);
        assertEquals(releaseState, gitStateStore.readReleaseStatus("myFirstRelease"));
    }

}
