/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.csv.git;

import org.junit.Assert;
import org.junit.Test;

import ch.sbb.releasetrain.utils.git.GITAccessorImpl;


public class GITAccessorTest {

    @Test
    public void testIsBuildRUNNING() throws Exception {

        GITAccessorImpl git = new GITAccessorImpl();

        Assert.assertTrue(git.readFileToString("pom.xml").contains("<modelVersion>4.0.0</modelVersion>"));

        // Assert.assertTrue(git.writeFile("pom.xml","housi"));

    }

}
