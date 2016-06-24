/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.git;

import org.junit.Assert;
import org.junit.Test;


public class GITAccessorTest {

    @Test
    public void testIsBuildRUNNING() throws Exception {

        GITAccessorImpl git = new GITAccessorImpl();
        git.setRepo("https://github.com/SchweizerischeBundesbahnen/releasetrain.git");
        Assert.assertTrue(git.readFileToString("pom.xml", "develop").contains("<modelVersion>4.0.0</modelVersion>"));

        // Assert.assertTrue(git.writeFile("pom.xml","housi"));

    }

}
