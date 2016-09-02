/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.backingbeans;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.sbb.releasetrain.action.JenkinsAction;
import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.EmailActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

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
public class JenkinsActionBackingBean {

	@Autowired
	private DefaultPersistence persistence;

	public JenkinsActionConfig getJenkins(){
		JenkinsActionConfig conf = persistence.getJenkins();
		if(conf == null){
			conf = new JenkinsActionConfig();
			persistence.setJenkins(conf);
		}
		return conf;
	}

	public void setJenkins(JenkinsActionConfig config){
		persistence.setJenkins(config);
	}

	public void  save(){
		persistence.save();
	}
}
