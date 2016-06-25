/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;

public class ReleaseStatusTest  {

    @Test
    public void newReleaseStatus()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", createConfigs());
        assertEquals("myrelease", releaseStatus.getReleaseName());
    }

    @Test
    public void newReleaseStatusWithActionNames()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", createConfigs());
        assertEquals(2, releaseStatus.getActionState().size());
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
        configs.add(new ActionConfig());
        configs.add(new ActionConfig());
        return configs;
    }

}
