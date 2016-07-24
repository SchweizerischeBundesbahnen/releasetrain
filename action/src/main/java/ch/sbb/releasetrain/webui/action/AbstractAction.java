/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ch.sbb.releasetrain.webui.state.model.ActionResult;
import ch.sbb.releasetrain.webui.state.model.ActionState;
import ch.sbb.releasetrain.webui.utils.model.Recognizable;

/**
 * Marshaling / unmarsahling models from / to xstream strings
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
@AllArgsConstructor
public abstract class AbstractAction<T extends Recognizable> implements Action {

    protected boolean doWeRun(ActionState state) {
        // is done: return
        if (state.getActionResult() == ActionResult.SUCCESS) {
            log.info("action " + getName() + " is done: nothing to do");
            return false;
        }
        return true;
    }

    public ActionResult run(ActionState state, String releaseVersion, String snapshotVersion, String maintenanceVersion) {
        if (!doWeRun(state)) {
            return state.getActionResult();
        }
        return doWork(state, releaseVersion, snapshotVersion, maintenanceVersion);
    }

    abstract ActionResult doWork(ActionState state, String releaseVersion, String snapshotVersion, String maintenanceVersion);

}
