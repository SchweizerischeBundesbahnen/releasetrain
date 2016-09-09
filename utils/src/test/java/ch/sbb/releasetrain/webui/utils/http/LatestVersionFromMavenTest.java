/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.utils.http;
/**
 * Get String from http(s) url
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
import ch.sbb.releasetrain.utils.http.HttpUtilImpl;

import ch.sbb.releasetrain.utils.mavenserach.LatetVersionFromMavenserach;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
@Slf4j
public class LatestVersionFromMavenTest {

	@Test
	public void testGetGoogle() throws Exception {
		HttpUtilImpl http = new HttpUtilImpl();
		LatetVersionFromMavenserach util = new LatetVersionFromMavenserach();
		ReflectionTestUtils.setField(util,"util",http);
		String in = util.getLatestVersionForGroupId("ch.sbb.releasetrain");
		log.info(in);
		Assert.assertFalse(in.isEmpty());
	}

}
