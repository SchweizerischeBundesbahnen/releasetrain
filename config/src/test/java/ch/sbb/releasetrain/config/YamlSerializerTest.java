/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import ch.sbb.releasetrain.config.model.email.MailReceiver;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;
import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public class YamlSerializerTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private YamlModelAccessor<ReleaseConfig> configSerializer = new YamlModelAccessor<>();
	private YamlModelAccessor<MailReceiver> emailSerializer = new YamlModelAccessor<>();

	@Test
	public void testSerializeActionConfig() throws Exception {

		ReleaseConfig release = new ReleaseConfig();
		ActionConfig action = new JenkinsActionConfig();
		action.getProperties().put("myProp1", "Hallo Welt1");
		action.getProperties().put("myProp2", "Hallo Welt2");

		ActionConfig action2 = new JenkinsActionConfig();
		action2.getProperties().put("myProp2", "Hallo asxasxsxWelt");

		release.setTyp("demo-release");
		release.getActions().add(action);
		release.getActions().add(action2);

		File file = new File(testFolder.getRoot(), "action.yml");
		FileUtils.writeStringToFile(file, configSerializer.convertEntry(release), "UTF-8");

		ReleaseConfig release2 = configSerializer.convertEntry(FileUtils.readFileToString(file, "UTF-8"));
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

		File file = new File(testFolder.getRoot(), "mail.yml");
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