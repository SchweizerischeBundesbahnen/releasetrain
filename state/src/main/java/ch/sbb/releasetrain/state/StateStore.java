/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state;

import ch.sbb.releasetrain.state.model.ReleaseState;

/**
 * Store the release and action state.
 *
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public interface StateStore {

	void writeReleaseStatus(ReleaseState releaseStatus);

	ReleaseState readReleaseStatus(String releaseIdentifier);
}
