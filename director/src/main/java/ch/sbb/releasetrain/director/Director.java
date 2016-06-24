/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.action.Action;
import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseEvent;

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
    private List<Action> actions;

    public void direct() {

        List<ReleaseEvent> calendar = config.readReleaseCalendars();
        for (ReleaseEvent event : calendar) {
            // is not in future
            if (event.retreiveAsDate().before(new Date())) {
                handleEvent(event);
            }

        }

    }

    private void handleEvent(ReleaseEvent event) {

        // 1) if status not here
        // hole action config
        // status anlegen

        // if action state in past and not executet
        evaluateActionForName(event.getActionType());

        // read the ReleaseConfig
        // runEvent();

    }

    private void executeAtion() {

    }

    private Action evaluateActionForName(String name) {
        for (Action action : actions) {
            if (action.getName().equalsIgnoreCase(name)) {
                log.error("no Action for name: " + name + " found");
                return action;
            }
        }
        log.error("no Action for name: " + name + " found");
        return null;
    }


}
