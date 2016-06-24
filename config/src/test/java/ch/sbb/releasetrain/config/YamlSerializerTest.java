/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import ch.sbb.releasetrain.config.model.ActionConfig;
import ch.sbb.releasetrain.config.model.ReleaseConfig;
import ch.sbb.releasetrain.config.model.email.MailReceiver;
import ch.sbb.releasetrain.director.modelaccessor.YamlModelAccessor;

public class YamlSerializerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    YamlModelAccessor<ReleaseConfig> configSerializer = new YamlModelAccessor<>();
    YamlModelAccessor<MailReceiver> emailSerializer = new YamlModelAccessor<>();

    @Test
    public void testSerializeActionConfig() throws Exception {

        ReleaseConfig release = new ReleaseConfig();
        ActionConfig action = new ActionConfig();
        action.setName("my Action äü");
        action.getProperties().put("myProp1", "Hallo Welt1");
        action.getProperties().put("myProp2", "Hallo Welt2");

        ActionConfig action2 = new ActionConfig();
        action2.setName("my Action äüass");
        action2.getProperties().put("myProp2", "Hallo asxasxsxWelt");

        release.setTyp("demo-release");
        release.getActions().add(action);
        release.getActions().add(action2);

        File file = new File(testFolder.getRoot(), "action.yaml");
        FileUtils.writeStringToFile(file, configSerializer.convertEntry(release));

        ReleaseConfig release2 = configSerializer.convertEntry(FileUtils.readFileToString(file));
        Assert.assertNotNull(release2);
        Assert.assertEquals(release, release2);

    }

    @Test
    public void testSerializeMailReceiverConfig() throws Exception {

        List<MailReceiver> list = new ArrayList<>();
        list.add(createMailReceiver(1, "test"));
        list.add(createMailReceiver(2, "test"));
        list.add(createMailReceiver(3, "hallo"));
        list.add(createMailReceiver(4, "hallo"));

        File file = new File(testFolder.getRoot(), "mail.yaml");
        FileUtils.writeStringToFile(file, emailSerializer.convertEntrys(list));

        List<MailReceiver> list2 = emailSerializer.convertEntrys(FileUtils.readFileToString(file));
        Assert.assertNotNull(list2);
        Assert.assertEquals(list.size(), list2.size());

        Assert.assertEquals(list.get(3), list2.get(3));

    }

    private MailReceiver createMailReceiver(int number, String mailinglist) {
        MailReceiver rec = new MailReceiver();
        rec.setEmail("test" + number + "@test.ch");
        rec.getMailinglist().add(mailinglist);
        rec.getMailinglist().add("default");
        rec.getMailinglist().add("default" + number);
        return rec;
    }
}