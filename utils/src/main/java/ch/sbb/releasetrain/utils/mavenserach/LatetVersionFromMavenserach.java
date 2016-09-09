/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.mavenserach;

import ch.sbb.releasetrain.utils.emails.SMTPUtil;

import ch.sbb.releasetrain.utils.http.HttpUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Email Util
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Component
@Slf4j
public class LatetVersionFromMavenserach  {

	@Autowired
	private HttpUtil util;

	public String getLatestVersionForGroupId(String group){
		String ret = util.getPageAsString("http://search.maven.org/solrsearch/select?q=g:%22"+group+"%22&rows=1&wt=xml");
		return StringUtils.substringBetween(ret,"<str name=\"latestVersion\">","</str>");
	}

}
