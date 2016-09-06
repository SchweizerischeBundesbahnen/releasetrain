/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.model;

import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.utils.model.Recognizable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The state of a release event.
 *
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionState implements Recognizable<ActionState> {

	private String resultString;

	private ActionConfig config;

	private ActionResult actionResult = ActionResult.NONE;

	public ActionState(final ActionConfig actionConfig) {
		this.config = actionConfig;
	}

	public String getActionName() {
		return config.getName();
	}

	@Override
	public String retreiveIdentifier() {
		return getActionName();
	}

	@Override
	public int compareTo(ActionState actionState) {
		return actionState.retreiveIdentifier().compareTo(this.retreiveIdentifier());
	}
}
