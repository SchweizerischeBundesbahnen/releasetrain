/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.action;

import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.webui.state.model.ActionResult;
import ch.sbb.releasetrain.webui.state.model.ActionState;

/**
 * A Interface for a Action (Ex: Runnable Jenkins Job)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Component
public interface Action {

    String getName();

    ActionResult run(ActionState state, String releaseVersion, String snapshotVersion, String maintenanceVersion);

}
