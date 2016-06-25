/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director;

import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.action.Action;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseEvent;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;
import ch.sbb.releasetrain.config.releasecalendar.ConfigAccessor;
import ch.sbb.releasetrain.state.StateStore;
import ch.sbb.releasetrain.state.model.ActionResult;
import ch.sbb.releasetrain.state.model.ActionState;
import ch.sbb.releasetrain.state.model.ReleaseState;

/**
 * Mighty Director - dictator
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Component
@Slf4j
public class Director {

    @Autowired
    private ConfigAccessor config;

    @Autowired
    private StateStore stateStore;

    @Autowired
    private List<Action> actions;

    public void direct() {

        List<ReleaseEvent> calendar = config.readReleaseCalendar();
        log.info("calendar found in config [" + calendar.size() + "]: " + calendar);
        for (ReleaseEvent event : calendar) {
            // is not in future
            if (laysInPast(event.retreiveAsDate())) {
                handleEvent(event);
            }
        }
    }

    private void handleEvent(ReleaseEvent event) {

        // if state inexisting
        ReleaseState state = stateStore.readReleaseStatus(event.retreiveIdentifier());
        // ... create new one
        if (state == null) {
            state = createReleaseState(event);
        }

        handleAction(event, state);

        // save the releaseState
        stateStore.writeReleaseStatus(state);
    }

    private void handleAction(ReleaseEvent event, ReleaseState state) {
        // if one of the action state in past and not executet execute it
        for (ActionState actionState : state.getActionState()) {
            LocalDateTime actionStartDate = event.retreiveAsDate().plusHours(actionState.getConfig().getOffsetHours());

            ActionResult rs = actionState.getActionResult();

            if (laysInPast(actionStartDate) && actionState.getActionResult() != ActionResult.SUCCESS) {
                Action action = evaluateActionForName(actionState.getConfig().getName());
                try {
                    // will set action state inside this method
                    rs = action.run(actionState, event.getReleaseVersion(), event.getSnapshotVersion(), event.getSnapshotVersion());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    rs = ActionResult.SUCCESS;
                    actionState.setResultString(e.getMessage());
                }
            }

            actionState.setActionResult(rs);

            if (actionState.getActionResult() != ActionResult.SUCCESS) {
                return;
            }
        }
    }

    private Action evaluateActionForName(String name) {
        for (Action action : actions) {
            if (action.getName().equalsIgnoreCase(name)) {
                log.info("Action for name: " + name + " found");
                return action;
            }
        }
        log.error("no Action for name: " + name + " found");
        return null;
    }

    private boolean laysInPast(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    private ReleaseState createReleaseState(ReleaseEvent event) {
        ReleaseConfig releaseConfig = config.readConfig(event.getActionType());
        return new ReleaseState(event.retreiveIdentifier(), releaseConfig.getActions());
    }

}
