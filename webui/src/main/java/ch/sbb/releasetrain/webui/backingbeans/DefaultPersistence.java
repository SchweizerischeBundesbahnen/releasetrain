/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.backingbeans;

import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.EmailActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;
import ch.sbb.releasetrain.utils.yaml.YamlUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * JenkinsActionBackingBean.
 *
 * @author Author: info@emad.ch
 * @since 0.0.1
 */
@Controller
@Slf4j
public class DefaultPersistence {

	private final static String NAME = "defaultConfig";
	private EmailActionConfig email;
	private JenkinsActionConfig jenkins;
	private ReleaseConfig config;
	@Autowired
	private ConfigAccessor configAccessor;

	private boolean init = false;

	public Boolean isReady() {
		init();
		if (email == null || jenkins == null) {
			return Boolean.FALSE;
		}

		if (email.getSmtpServer() == null || email.getSmtpServer().isEmpty()) {
			return Boolean.FALSE;
		}

		if (jenkins == null || jenkins.getJenkinsBuildToken() == null || jenkins.getJenkinsBuildToken().isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public void init() {

		if (init) {
			return;
		}

		config = configAccessor.readConfig(NAME);

		if (config == null) {
			config = new ReleaseConfig();
			configAccessor.writeConfig(NAME, config);
		}
		for (ActionConfig cf : config.getActions()) {
			if (cf instanceof EmailActionConfig) {
				EmailActionConfig emailActionConfig = (EmailActionConfig) cf;
				email = emailActionConfig;
			}

			if (cf instanceof JenkinsActionConfig) {
				JenkinsActionConfig jenkinsActionConfig = (JenkinsActionConfig) cf;
				jenkins = jenkinsActionConfig;
			}

		}

		if (email == null) {
			EmailActionConfig emailActionConfig = new EmailActionConfig();
			config.getActions().add(emailActionConfig);
		}

		if (jenkins == null) {
			JenkinsActionConfig jenkinsAction = new JenkinsActionConfig();
			config.getActions().add(jenkinsAction);
		}
		init = true;
	}

	public List<ActionConfig> findAllConfigs() {
		if (!init) {
			init();
		}
		List<ActionConfig> ret = new ArrayList<>();
		ret.add(jenkins);
		ret.add(email);
		return ret;
	}

	public ActionConfig getNewForName(String name) {

		if (name.isEmpty()) {
			return null;
		}

		if (name.equalsIgnoreCase("JenkinsAction")) {
			try {
				return (ActionConfig) YamlUtil.clone(jenkins);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		if (name.equalsIgnoreCase("EmailAction")) {
			try {
				return (ActionConfig) YamlUtil.clone(email);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		throw new RuntimeException("no Action found for name: " + name);
	}

	public void save() {

		init();

		config.getActions().clear();
		config.getActions().add(jenkins);
		config.getActions().add(email);

		configAccessor.writeConfig(NAME, config);
	}

	public EmailActionConfig getEmail() {
		init();
		return email;
	}

	public void setEmail(EmailActionConfig email) {
		init();
		this.email = email;
	}

	public JenkinsActionConfig getJenkins() {
		init();
		return jenkins;
	}

	public void setJenkins(JenkinsActionConfig jenkins) {
		init();
		this.jenkins = jenkins;
	}

}
