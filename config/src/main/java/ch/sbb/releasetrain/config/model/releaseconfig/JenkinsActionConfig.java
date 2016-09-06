/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releaseconfig;

import ch.sbb.releasetrain.utils.crypt.EncryptorImpl;
import ch.sbb.releasetrain.utils.model.Recognizable;

import lombok.Data;

/**
 * Representation of a Jenkins Action Config retreived from a storage provider
 * (Ex: GIT Repo)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public class JenkinsActionConfig extends ActionConfig implements Recognizable<JenkinsActionConfig> {

	private String jenkinsUrl;

	private String jenkinsBuildToken;

	private String jenkinsJobname;

	private String jenkinsUser;

	private String jenkinsPassword;

	public String getEncPassword() {
		if (jenkinsPassword == null) {
			return null;
		}
		return EncryptorImpl.decrypt(jenkinsPassword);
	}

	public void setEncPassword(String jenkinsPassword) {
		this.jenkinsPassword = EncryptorImpl.encrypt(jenkinsPassword);
	}

	@Override
	public int compareTo(JenkinsActionConfig jenkinsActionConfig) {
		return super.compareTo(jenkinsActionConfig);
	}

	@Override
	public String getName() {
		return "jenkinsAction";
	}

}
