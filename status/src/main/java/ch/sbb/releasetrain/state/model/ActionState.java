/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;

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
