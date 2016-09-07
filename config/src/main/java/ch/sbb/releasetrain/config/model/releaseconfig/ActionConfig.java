/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releaseconfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import lombok.Data;

/**
 * Representation of a Action retreived from a storage provider (Ex: GIT Repo)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public abstract class ActionConfig {

	protected long offseMinutes = 0;

	private Map<String, String> properties = new HashMap<>();

	public abstract String getName();

	public Date getOffset() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));
		return new Date(offseMinutes * 60000);
	}

	public void setOffset(Date date) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));
		offseMinutes = date.getTime() / 60000;
	}

	public String getOffsetStr() {
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
		fmt.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));
		return fmt.format(new Date(offseMinutes * 60000));
	}

	public String retreiveIdentifier() {
		return getName();
	}

	public int compareTo(ActionConfig actionConfig) {
		return actionConfig.retreiveIdentifier().compareTo(getName());
	}

}
