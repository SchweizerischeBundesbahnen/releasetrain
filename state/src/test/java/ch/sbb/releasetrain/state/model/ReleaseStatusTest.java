/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public class ReleaseStatusTest  {

    @Test
    public void newReleaseStatus()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", createConfigs());
        assertEquals("myrelease", releaseStatus.getReleaseName());
    }

    @Test
    public void newReleaseStatusWithActionNames()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", createConfigs());
        assertEquals(3, releaseStatus.getActionState().size());
        assertEquals("jenkinsAction", releaseStatus.getActionState().get(0).getActionName());
        assertEquals("jenkinsAction", releaseStatus.getActionState().get(1).getActionName());
    }

    @Test
    public void newActionStatusResult()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", createConfigs());
        assertEquals(ActionResult.NONE, releaseStatus.getActionState().get(0).getActionResult());
    }

    private List<ActionConfig> createConfigs() {
        List<ActionConfig> configs = new ArrayList<>();
        ActionConfig action1 = new JenkinsActionConfig();
        configs.add(action1);

        ActionConfig action2 = new JenkinsActionConfig();
        configs.add(action2);

        configs.add(new JenkinsActionConfig());
        return configs;
    }

}
