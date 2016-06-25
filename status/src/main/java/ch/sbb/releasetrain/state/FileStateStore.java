/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.state.model.ReleaseState;
import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;

@Component
@Slf4j
public class FileStateStore implements StateStore {

    @Autowired
    private StateStoreConfig storeConfig;

    private YamlModelAccessor<ReleaseState> releaseState = new YamlModelAccessor<ReleaseState>();

    @Override
    public void writeReleaseStatus(ReleaseState releaseStatus) {
        try {
            FileUtils.writeStringToFile(new File(storeConfig.getUrl(), releaseStatus.retreiveIdentifier() + "-stored-state.yaml"), releaseState.convertEntry(releaseStatus));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public ReleaseState readReleaseStatus(String releaseName) {
        String in = "";
        String fileName = storeConfig.getUrl() + "/" + releaseName + "-stored-state.yaml";
        try {

            if (!new File(fileName).exists()) {
                log.info("file " + fileName + " not existing");
                return null;
            }

            in = FileUtils.readFileToString(new File(fileName));
            return releaseState.convertEntry(in);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
