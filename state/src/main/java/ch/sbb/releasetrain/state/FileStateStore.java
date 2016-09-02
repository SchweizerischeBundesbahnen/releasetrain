/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state;

import java.io.File;

import ch.sbb.releasetrain.git.GITAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.state.model.ReleaseState;

/**
 * Simple Store for state with file for test purposes. Put the directory in the URL and activate the file spring profiel.
 *
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@Component
@Profile("file")
public class FileStateStore implements StateStore {

    @Autowired
    private GITAccessor git;

    @Override
    public void writeReleaseStatus(ReleaseState releaseStatus) {
        new StateFileWriter(new File(git.directory().toURI())).write(releaseStatus);
    }

    @Override
    public ReleaseState readReleaseStatus(String releaseName) {
        return new StateFileReader(new File(git.directory().toURI())).read(releaseName);
    }
}
