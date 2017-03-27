/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releasecalendar;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a Calendar
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
@ToString
public class ReleaseCalendar {

	@Getter
	private List<ReleaseColumn> coloumns = new ArrayList<>();

	@Getter
	@Setter
	private List<ReleaseCalendarEvent> events = new ArrayList<>();

	public ReleaseCalendar() {
		addColoumn("releaseVersion");
		addColoumn("snapshotVersion");
		addColoumn("maintenanceVersion");
		addColoumn("custom1");
		addColoumn("custom2");
	}

	public List<String> getColoumnsFiltered() {
		List<String> ret = new ArrayList<>();
		for (ReleaseColumn col : coloumns) {
			if (col.getOn()) {
				ret.add(col.getName());
			}
		}
		return ret;
	}

	public void setColoumnsFiltered(List<String> in) {

		List<String> nc = new ArrayList<>();

		for (ReleaseColumn col : coloumns) {
			col.setOn(Boolean.FALSE);
		}

		for (String colN : in) {
			ReleaseColumn col = getColForName(colN);
			if (col == null) {
				nc.add(colN);
			} else {
				col.setOn(true);
			}
		}
		for (String name : nc) {
			addColoumn(name);
		}
	}

	public List<String> getAllColoumsNames() {
		List<String> ret = new ArrayList<>();
		for (ReleaseColumn col : coloumns) {
			ret.add(col.getName());
		}
		return ret;
	}

	public void addColoumn(String name) {
		coloumns.add(new ReleaseColumn(name, Boolean.TRUE));
		for (ReleaseCalendarEvent ev : events) {
			ev.getParameters().put(name, "");
		}
	}

	public void addEvent() {
		ReleaseCalendarEvent ev = new ReleaseCalendarEvent();
		for (ReleaseColumn col : coloumns) {
			ev.getParameters().put(col.getName(), "");
			ev.setRoot(this);
		}
	}

	private ReleaseColumn getColForName(String name) {
		for (ReleaseColumn col : coloumns) {
			if (col.getName().equals(name)) {
				return col;
			}
		}
		return null;
	}

}
