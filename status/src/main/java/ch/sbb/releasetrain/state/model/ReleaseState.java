/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.utils.model.Recognizable;

import com.google.common.collect.ImmutableList;

@NoArgsConstructor
public class ReleaseState implements Recognizable<ReleaseState> {

    @Getter
    @Setter
    private List<ActionState> actionState;

    @Getter
    @Setter
    private String releaseName;

    public ReleaseState(String releaseName, List<ActionConfig> configs) {
        this.releaseName = releaseName;
        ImmutableList.Builder<ActionState> actionStatus = new ImmutableList.Builder<>();
        for (ActionConfig actionName : configs) {
            actionStatus.add(new ActionState(actionName));
        }
        this.actionState = actionStatus.build();
    }

    @Override
    public String retreiveIdentifier() {
        return releaseName;
    }

    @Override
    public int compareTo(ReleaseState releaseState) {
        return releaseState.retreiveIdentifier().compareTo(this.retreiveIdentifier());
    }
}
