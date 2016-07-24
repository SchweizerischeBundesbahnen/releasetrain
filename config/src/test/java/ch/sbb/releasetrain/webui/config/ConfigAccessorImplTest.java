/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.config;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ch.sbb.releasetrain.webui.config.model.email.MailReceiver;
import ch.sbb.releasetrain.webui.config.model.releaseconfig.ReleaseConfig;
import ch.sbb.releasetrain.webui.config.releasecalendar.ConfigAccessorImpl;
import ch.sbb.releasetrain.webui.utils.http.HttpUtilImpl;

/**
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public class ConfigAccessorImplTest {

    @Test
    public void testReadConfigFromGitRepo() throws Exception {

        ConfigAccessorImpl accessor = new ConfigAccessorImpl();
        accessor.setBaseURL("https://github.com/SchweizerischeBundesbahnen/releasetrain/raw/feature/releasetrainconfig/");
        accessor.setHttp(new HttpUtilImpl());
        ReleaseConfig config = accessor.readConfig("demo-release");
        Assert.assertNotNull(config);

        List<MailReceiver> emailReceiver = accessor.readMailReceiver();
        Assert.assertEquals(4, emailReceiver.size());

        List<MailReceiver> emailReceiverList = accessor.readMailReveiverForMailinglist("test");
        Assert.assertEquals(2, emailReceiverList.size());

    }

}