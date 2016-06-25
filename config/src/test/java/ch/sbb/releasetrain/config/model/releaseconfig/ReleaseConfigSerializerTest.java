/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releaseconfig;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;

/**
 * Test
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public class ReleaseConfigSerializerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private YamlModelAccessor<ReleaseConfig> configSerializer = new YamlModelAccessor<>();

    @Test
    public void testSerializeReleaseConfig() throws Exception {

        ReleaseConfig release = new ReleaseConfig();
        release.setTyp("devType");
        ActionConfig action = new ActionConfig();
        action.setName("jenkinsAction");
        action.getProperties().put("state", "SUCCESS");
        release.getActions().add(action);

        ActionConfig action2 = new ActionConfig();
        action2.setName("emailAction");
        action2.getProperties().put("state", "SUCCESS");
        release.getActions().add(action2);

        File file = new File(testFolder.getRoot(), "devType3.yaml");
        FileUtils.writeStringToFile(file, configSerializer.convertEntry(release));

        ReleaseConfig release2 = configSerializer.convertEntry(FileUtils.readFileToString(file));
        Assert.assertNotNull(release2);
        Assert.assertEquals(release, release2);

    }

}