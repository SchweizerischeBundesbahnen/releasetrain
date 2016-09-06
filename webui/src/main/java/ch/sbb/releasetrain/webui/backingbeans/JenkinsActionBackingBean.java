/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.backingbeans;

import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * JenkinsActionBackingBean.
 *
 * @author Author: info@emad.ch
 * @since 0.0.1
 */
@Controller
@Slf4j
@Scope("session")
public class JenkinsActionBackingBean {

	@Autowired
	private DefaultPersistence persistence;

	public JenkinsActionConfig getJenkins() {
		JenkinsActionConfig conf = persistence.getJenkins();
		if (conf == null) {
			conf = new JenkinsActionConfig();
			persistence.setJenkins(conf);
		}
		return conf;
	}

	public void setJenkins(JenkinsActionConfig config) {
		persistence.setJenkins(config);
	}

	public void save() {
		persistence.save();
	}
}
