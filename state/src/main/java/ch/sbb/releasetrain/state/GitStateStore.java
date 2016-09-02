/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state;

import ch.sbb.releasetrain.git.GITAccessor;
import ch.sbb.releasetrain.git.GitException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.git.GitClient;
import ch.sbb.releasetrain.git.GitRepo;
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
    private GITAccessor git;

    private boolean resetOnStartup = true;

    @Override
    public void writeReleaseStatus(final ReleaseState releaseState) {

        if(!git.isWrite()){
            log.error("GIT Repo not writable !");
            throw new GitException("GIT Repo not writable!");
        }

        log.debug("Writing releaseState {}", releaseState);
        new StateFileWriter(git.directory()).write(releaseState);
        log.info("Wrote releaseState for release={}", releaseState.getReleaseName());
        git.signalCommit();
    }

    @Override
    public ReleaseState readReleaseStatus(final String releaseIdentifier) {

        if(!git.isRead()){
            log.error("GIT Repo not readble !");
            throw new GitException("GIT Repo not readble !");
        }

        ReleaseState releaseState = new StateFileReader(git.directory()).read(releaseIdentifier);
        log.info("Read releaseState for release={}", releaseIdentifier);
        log.debug("Read releaseState={}", releaseState);
        return releaseState;
    }

    private GitRepo gitRepo() {

        GitRepo repo = git.getRepo();

        // reset if requested and set resetOnStartup to false, so reset is done only at the beginning
        if (this.resetOnStartup) {
            repo.reset();
            this.resetOnStartup = false;
        }
        return repo;
    }
}
