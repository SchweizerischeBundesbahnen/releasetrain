/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.backingbeans;

import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseCalendar;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseCalendarEvent;
import ch.sbb.releasetrain.git.GITAccessor;
import ch.sbb.releasetrain.state.StateStore;
import ch.sbb.releasetrain.state.model.ReleaseState;
import ch.sbb.releasetrain.utils.yaml.YamlUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
import org.primefaces.event.CellEditEvent;
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

	@Getter
	@Setter
	private ReleaseCalendar calend;

	@Getter
	private String selectedCalendar;

	@Getter
	@Setter
	private String newColumn;

	public void newColumn() {
		this.calend.addColoumn(newColumn);
		newColumn = "";
		save();
	}

	public void setSelectedCalendar(String selecteCalendar) {
		if (selecteCalendar.isEmpty()) {
			this.selectedCalendar = "";
			return;
		}
		this.selectedCalendar = selecteCalendar;
		this.calend = configAccessor.readCalendar(selecteCalendar);

		if (this.calend.getEvents().size() < 1) {
			newEntry();
		}

		for (ReleaseCalendarEvent ev : this.calend.getEvents()) {
			ReleaseState state = stateStore.readReleaseStatus(ev.getActionType() + "-" + ev.retreiveIdentifier());
			if (state == null) {
				ev.setState("NEW");
			} else {
				ev.setState(state.getState());
			}
		}
	}

	public List<String> findAllActions() {
		return configAccessor.readAllConfigs();
	}

	public void save() {
		sortList();
		configAccessor.writeCalendar(this.calend, selectedCalendar);
	}

	public void reload() {
		git.cloneOrPull();
		String temp = selectedCalendar;
		setSelectedCalendar("");
		setSelectedCalendar(temp);
	}

	public void newEntry() {

		ReleaseCalendarEvent latest = null;
		ReleaseCalendarEvent cal2 = new ReleaseCalendarEvent();
		cal2.setActionType(selectedCalendar);
		ReleaseCalendarEvent latest2 = null;
		if (calend.getEvents().size() > 0) {
			latest = calend.getEvents().get(calend.getEvents().size() - 1);
		}
		if (latest != null) {
			long time = latest.getAsDate().getTime();
			time = time + 1000 * 60 * 60 * 24;
			cal2 = (ReleaseCalendarEvent) YamlUtil.unMarshall(YamlUtil.marshall(latest));
			cal2.setAsDate(new Date(time));
		} else {
			cal2.setAsDate(new Date());
		}

		cal2.setState("NEW");
		calend.getEvents().add(cal2);
		save();
	}

	public void sortList() {
		BeanComparator<ReleaseCalendarEvent> eintraegeComp = new BeanComparator<ReleaseCalendarEvent>("date");
		Collections.sort(calend.getEvents(), eintraegeComp);
	}

	public void onCellEdit(CellEditEvent event) {
		log.info("row changed... " + event);
		save();
	}
}
