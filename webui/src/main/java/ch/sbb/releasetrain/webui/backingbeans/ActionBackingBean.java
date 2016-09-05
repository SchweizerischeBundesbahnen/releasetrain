/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.backingbeans;

import javax.annotation.PostConstruct;
import javax.validation.groups.Default;

import ch.sbb.releasetrain.git.GITAccessor;
import org.apache.commons.beanutils.BeanComparator;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JenkinsActionBackingBean.
 *
 * @author Author: info@emad.ch
 * @since 0.0.1
 */
@Controller
@Slf4j
@Scope("session")
public class ActionBackingBean {

	@Autowired
	private ConfigAccessor configAccessor;

	@Autowired
	private CalendarBackingBean calendarBackingBean;

	@Autowired
	private GITAccessor git;

	@Autowired
	private DefaultPersistence pers;

	@Getter @Setter
	private ReleaseConfig config;

	@Getter
	private String selectedAction;

	@Getter @Setter
	private String selectedActionNew;

	@Getter
	private String newAction;

	private String old;
	private long start;

	public void setSelectedAction(String selectedAction) {
		calendarBackingBean.setSelectedCalendar(selectedAction);
		if(selectedAction.isEmpty()){
			this.selectedAction = null;
			this.config = null;
			return;
		}
		this.selectedAction = selectedAction;
		this.config = configAccessor.readConfig(selectedAction+"-type");
	}

	public List<String> findAllActions(){
		List<String> ret = configAccessor.readAllConfigs();
		return ret;
	}

	public List<ActionConfig> findAllConfigs(){
		return pers.findAllConfigs();
	}

	public void newOne(){
		config = new ReleaseConfig();
		configAccessor.writeConfig(selectedActionNew,config);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error(e.getMessage(),e);
		}
		setSelectedAction(selectedActionNew);
		selectedActionNew = "";
	}

	public void setNewAction(String newAction) {
		ActionConfig config = pers.getNewForName(newAction);
		if(config != null) {
			this.config.getActions().add(config);
		}
	}

	public void save(){
		sortList();
		configAccessor.writeConfig(selectedAction,this.config);
		git.signalCommit();
	}

	public void delete(){
		configAccessor.deleteConfig(selectedAction);
		setSelectedAction("");
		git.signalCommit();
	}

	public void sortList(){
		BeanComparator<ActionConfig> eintraegeComp = new BeanComparator<ActionConfig>("offsetHours");
		Collections.sort(config.getActions(), eintraegeComp);
	}

}
