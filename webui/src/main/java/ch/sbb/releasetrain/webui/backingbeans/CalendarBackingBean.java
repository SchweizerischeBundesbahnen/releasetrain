/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.backingbeans;

import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseCalendarEvent;


import java.util.Collections;
import java.util.Date;
import java.util.List;

import ch.sbb.releasetrain.git.GITAccessor;
import ch.sbb.releasetrain.state.StateStore;
import ch.sbb.releasetrain.state.model.ReleaseState;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
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
public class CalendarBackingBean {

	@Autowired
	private ConfigAccessor configAccessor;

	@Autowired
	private StateStore stateStore;

	@Autowired
	private GITAccessor git;

	@Getter @Setter
	private List<ReleaseCalendarEvent> config;

	@Getter
	private String selectedCalendar;

	public void setSelectedCalendar(String selecteCalendar) {
		if(selecteCalendar.isEmpty()){
			this.selectedCalendar = "";
			this.config = null;
			return;
		}
		this.selectedCalendar = selecteCalendar;
		this.config = configAccessor.readReleaseCalendar(selecteCalendar);

		for(ReleaseCalendarEvent ev: this.config){
			ReleaseState state = stateStore.readReleaseStatus(ev.getActionType() + "-" + ev.retreiveIdentifier());
			if(state == null){
				ev.setState("NEW");
			} else {
				ev.setState(state.getState());
			}
		}
	}

	public List<String> findAllActions(){
		return configAccessor.readAllConfigs();
	}

	public void save(){
		sortList();
		configAccessor.writeReleaseCalendar(this.config,selectedCalendar);
		git.signalCommit();
	}

	public void reload(){
		String temp = selectedCalendar;
		setSelectedCalendar("");
		setSelectedCalendar(temp);
	}

	public void newEntry(){
		ReleaseCalendarEvent cal = new ReleaseCalendarEvent();
		cal.setActionType(selectedCalendar);
		ReleaseCalendarEvent latest = null;
		if(config.size() > 0) {
			latest = config.get(config.size() - 1);
		}
		if(latest != null){
			long time = latest.getAsDate().getTime() ;
			time = time + 1000 * 60 * 60 * 24;
			cal.setAsDate(new Date(time));
		} else {
			cal.setAsDate(new Date());
		}
		cal.setState("NEW");
		config.add(cal);
	}

	public void sortList(){
		BeanComparator<ReleaseCalendarEvent> eintraegeComp = new BeanComparator<ReleaseCalendarEvent>("date");
		Collections.sort(config, eintraegeComp);
	}
}
