/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releasecalendar;

import ch.sbb.releasetrain.utils.model.Recognizable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a scheduled Release Calendar Event, to trigger a Release Action
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
@Slf4j
@ToString
public class ReleaseCalendarEvent implements Recognizable<ReleaseCalendarEvent> {

	private transient final static String DATE_PATTERN = "yyyy-MM-dd HH:mm";

	private String date = "1900-01-01 00:01";

	private HashMap<String, String> parameters = new HashMap<>();

	private String actionType;

	private ReleaseCalendar root;

	private String state;

	public Map<String, String> retriveFilteredParams() {
		Map<String, String> ret = new HashMap<>();
		for (ReleaseColumn col : root.getColoumns()) {
			if (col.getOn()) {
				ret.put(col.getName(), parameters.get(col.getName()));
			}
		}
		return ret;
	}

	public LocalDateTime retreiveAsDate() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern(DATE_PATTERN);
		return LocalDateTime.from(formater.parse(date));
	}

	public void putAsDateTime(LocalDateTime dateTime) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
		date = format.format(dateTime);
	}

	public Date getAsDate() {
		SimpleDateFormat formater = new SimpleDateFormat(DATE_PATTERN);
		formater.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
		try {
			return formater.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setAsDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
		format.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
		this.date = format.format(date);
	}

	@Override
	public int compareTo(ReleaseCalendarEvent o) {
		return this.date.compareTo(o.getDate());
	}

	@Override
	public String retreiveIdentifier() {
		return date.replace(" ", "_").replace(":", "");
	}

}
