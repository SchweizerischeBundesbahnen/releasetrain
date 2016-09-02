/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.backingbeans;

import javax.annotation.PostConstruct;

import ch.sbb.releasetrain.config.model.releaseconfig.EmailActionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * JenkinsActionBackingBean.
 *
 * @author Author: info@emad.ch
 * @since 0.0.1
 */
@Controller
@Slf4j
@Scope("session")
public class EmailActionBackingBean {

	@Autowired
	private DefaultPersistence persistence;

	public EmailActionConfig getEmail(){
		EmailActionConfig conf = persistence.getEmail();
		if(conf == null){
			conf = new EmailActionConfig();
			persistence.setEmail(conf);
		}
		return persistence.getEmail();
	}

	public void setEmail(EmailActionConfig config){
		persistence.setEmail(config);
	}

	public void  save(){
		persistence.save();
	}

}
