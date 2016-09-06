/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director;

import ch.sbb.releasetrain.action.Action;
import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseCalendar;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseCalendarEvent;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;
import ch.sbb.releasetrain.git.GITPusherThread;
import ch.sbb.releasetrain.state.StateStore;
import ch.sbb.releasetrain.state.model.ActionResult;
import ch.sbb.releasetrain.state.model.ActionState;
import ch.sbb.releasetrain.state.model.ReleaseState;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mighty Director - dictator
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Component
@Slf4j
public class Director {

	@Setter
	private boolean shutdown = true;

	@Autowired
	private ConfigAccessor config;

	@Autowired
	private StateStore stateStore;

	@Autowired
	private List<Action> actions;

	@Autowired
	private GITPusherThread git;

	public void direct() {
		List<String> configs = config.readAllConfigs();
		// go for all the action calendars and look out for work to do
		for (String action : configs) {
			ReleaseCalendar cal = config.readCalendar(action);
			List<ReleaseCalendarEvent> calendar = cal.getEvents();
			log.info("calendar found in config [" + calendar.size() + "]: " + calendar);
			for (ReleaseCalendarEvent event : calendar) {
				// is not in future
				if (laysInPast(event.retreiveAsDate())) {
					handleEvent(event);
				}
			}
		}

		if (shutdown) {
			git.commit();
			System.exit(0);
		}

	}

	private void handleEvent(ReleaseCalendarEvent event) {

		// if state inexisting
		ReleaseState state = stateStore.readReleaseStatus(event.getActionType() + "-" + event.retreiveIdentifier());
		// ... create new one
		if (state == null) {
			state = createReleaseState(event);
		}

		handleAction(event, state);

		// save the releaseState
		stateStore.writeReleaseStatus(state);
	}

	private void handleAction(ReleaseCalendarEvent event, ReleaseState state) {
		// if one of the action state in past and not executet execute it
		for (ActionState actionState : state.getActionState()) {
			LocalDateTime actionStartDate = event.retreiveAsDate().plusMinutes(actionState.getConfig().getOffseMinutes());

			ActionResult rs = actionState.getActionResult();

			if (laysInPast(actionStartDate) && actionState.getActionResult() != ActionResult.SUCCESS) {
				Action action = evaluateActionForName(actionState.getConfig().getName());
				try {
					// will set action state inside this method
					if (action != null) {
						rs = action.run(actionState, event.getParameters());
					} else {
						log.error("action for name: " + actionState.getConfig().getName() + " not available");
					}
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

	private ReleaseState createReleaseState(ReleaseCalendarEvent event) {
		ReleaseConfig releaseConfig = config.readConfig(event.getActionType() + "-type");
		return new ReleaseState(event.getActionType() + "-" + event.getDate().replace(" ", "_").replace(":", ""), releaseConfig.getActions());
	}

}
