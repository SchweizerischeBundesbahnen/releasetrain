/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.state.git.GitClient;
import ch.sbb.releasetrain.state.git.GitRepo;
import ch.sbb.releasetrain.state.model.ReleaseState;

/**
 * Store the release state and config within a git repo in a special branch.
 *
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@Slf4j
@Component
@Profile("!file")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class GitStateStore implements StateStore {

    @Autowired
    private GitClient gitClient;

    @Autowired
    private StateStoreConfig storeConfig;

    @Override
    public void writeReleaseStatus(final ReleaseState releaseState) {
        GitRepo gitRepo = gitRepo();
        gitRepo.cloneOrPull();
        log.debug("Writing releaseState {}", releaseState);
        new StateFileWriter(gitRepo.directory()).write(releaseState);
        log.info("Wrote releaseState for release={}", releaseState.getReleaseName());
        gitRepo.addCommitPush();
    }

    @Override
    public ReleaseState readReleaseStatus(final String releaseIdentifier) {
        GitRepo gitRepo = gitRepo();
        gitRepo.cloneOrPull();
        ReleaseState releaseState = new StateFileReader(gitRepo.directory()).read(releaseIdentifier);
        log.info("Read releaseState for release={}", releaseIdentifier);
        log.debug("Read releaseState={}", releaseState);
        return releaseState;
    }

    private GitRepo gitRepo() {
        return gitClient.gitRepo(storeConfig.getUrl(), storeConfig.getBranch(), storeConfig.getUser(), storeConfig.getPassword());
    }
}
