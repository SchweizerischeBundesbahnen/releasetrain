/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.utils.model.Recognizable;

import com.google.common.collect.ImmutableList;

/**
 * The state of a release event.
 *
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ReleaseState implements Recognizable<ReleaseState> {

    @Getter
    @Setter
    private List<ActionState> actionState;

    @Getter
    @Setter
    private String releaseName;

    public String getState(){
        String ret = "-";
        for(ActionState state : actionState){

            if(state.getActionResult() == ActionResult.NONE){
                ret = "NONE";
            }

            if(state.getActionResult() == ActionResult.SUCCESS &&  !ret.equals("NONE")){
                return "SUCCESS";
            }

            if(state.getActionResult() == ActionResult.FAILED){
                return "FAILED";
            }
        }
        return ret;
    }

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
        return releaseName.replace(":","").replace(" ","_");
    }

    @Override
    public int compareTo(ReleaseState releaseState) {
        return releaseState.retreiveIdentifier().compareTo(this.retreiveIdentifier());
    }
}
