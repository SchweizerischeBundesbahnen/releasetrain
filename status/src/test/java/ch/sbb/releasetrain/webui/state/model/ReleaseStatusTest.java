/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.state.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.sbb.releasetrain.webui.config.model.releaseconfig.ActionConfig;

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
        assertEquals("myaction1", releaseStatus.getActionState().get(0).getActionName());
        assertEquals("myaction2", releaseStatus.getActionState().get(1).getActionName());
    }

    @Test
    public void newActionStatusResult()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", createConfigs());
        assertEquals(ActionResult.NONE, releaseStatus.getActionState().get(0).getActionResult());
    }

    private List<ActionConfig> createConfigs() {
        List<ActionConfig> configs = new ArrayList<>();
        ActionConfig action1 = new ActionConfig();
        action1.setName("myaction1");
        configs.add(action1);

        ActionConfig action2 = new ActionConfig();
        action2.setName("myaction2");
        configs.add(action2);

        configs.add(new ActionConfig());
        return configs;
    }

}
