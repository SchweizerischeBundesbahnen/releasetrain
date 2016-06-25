/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state;

import ch.sbb.releasetrain.state.model.ReleaseState;
import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;

import java.io.File;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
abstract class AbstractStateFileAccess {
    protected final YamlModelAccessor<ReleaseState> converter = new YamlModelAccessor<>();

    protected final File dir;

    public AbstractStateFileAccess(File dir) {
        this.dir = dir;
    }

    protected File file(final String releaseIdentifier) {
        return new File(dir, releaseIdentifier + "-stored-state.yaml");
    }
}
