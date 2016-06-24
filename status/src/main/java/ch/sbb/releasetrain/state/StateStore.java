/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state;

import ch.sbb.releasetrain.state.model.ReleaseState;

public interface StateStore {

    void init(StateStoreConfig storeConfig);

    void writeReleaseStatus(ReleaseState releaseStatus);

    ReleaseState readReleaseStatus(String releaseName);
}
