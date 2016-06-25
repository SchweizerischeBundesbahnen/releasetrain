/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state;

import ch.sbb.releasetrain.state.model.ReleaseState;
import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
class StateFileWriter extends AbstractStateFileAccess {

    public StateFileWriter(File dir) {
        super(dir);
    }

    public void write(final ReleaseState releaseState) {
        try {
            FileUtils.writeStringToFile(file(releaseState.retreiveIdentifier()), converter.convertEntry(releaseState));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error writing state to file %s", file(releaseState.retreiveIdentifier())), e);
        }
    }
}
