/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
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
    private StateStoreConfig storeConfig;

    @Override
    public void writeReleaseStatus(ReleaseState releaseStatus) {
        new StateFileWriter(new File(storeConfig.getUrl())).write(releaseStatus);
    }

    @Override
    public ReleaseState readReleaseStatus(String releaseName) {
        return new StateFileReader(new File(storeConfig.getUrl())).read(releaseName);
    }
}
