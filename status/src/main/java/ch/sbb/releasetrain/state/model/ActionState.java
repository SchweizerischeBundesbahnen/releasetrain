/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;

/**
 * The state of a release event.
 *
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionState {

    private String resultString;

    private ActionConfig config;

    private ActionResult actionResult = ActionResult.NONE;

    public ActionState(final ActionConfig actionConfig) {
        this.config = actionConfig;
    }

    public String getActionName() {
        return config.getName();
    }

}
