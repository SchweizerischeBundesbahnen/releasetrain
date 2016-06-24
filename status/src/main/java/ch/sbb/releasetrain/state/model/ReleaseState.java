/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.model;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.List;

public class ReleaseState {
    @Getter
    private final List<ActionState> actionState;

    @Getter
    private final String releaseName;

    public ReleaseState(String releaseName, String... actionNames) {
        this.releaseName = releaseName;
        ImmutableList.Builder<ActionState> actionStatus = new ImmutableList.Builder<>();
        for(String actionName : actionNames) {
            actionStatus.add(new ActionState(actionName));
        }
        this.actionState = actionStatus.build();
    }
}
