/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReleaseStatusTest  {

    @Test
    public void newReleaseStatus()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease");
        assertEquals("myrelease", releaseStatus.getReleaseName());
    }

    @Test
    public void newReleaseStatusWithActionNames()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", "myaction1", "myaction2");
        assertEquals(2, releaseStatus.getActionState().size());
        assertEquals("myaction1", releaseStatus.getActionState().get(0).getActionName());
        assertEquals("myaction2", releaseStatus.getActionState().get(1).getActionName());
    }

    @Test
    public void newActionStatusResult()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", "myaction");
        assertEquals(ActionResult.NONE, releaseStatus.getActionState().get(0).getActionResult());
    }

    @Test
    public void newActionStatusProgress()  {
        ReleaseState releaseStatus = new ReleaseState("myrelease", "myaction");
        assertEquals(ActionProgress.OPEN, releaseStatus.getActionState().get(0).getActionProgress());
    }
}
