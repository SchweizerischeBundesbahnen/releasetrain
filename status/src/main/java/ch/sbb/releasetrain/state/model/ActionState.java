/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ActionState {
    @Getter
    private final String actionName;

    @Getter
    @Setter
    private ActionProgress actionProgress = ActionProgress.OPEN;

    @Getter
    @Setter
    private ActionResult actionResult = ActionResult.NONE;

    public ActionState(final String actionName) {
        this.actionName = actionName;
    }



}
