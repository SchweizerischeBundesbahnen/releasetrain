/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releaseconfig;

import java.text.SimpleDateFormat;
import java.util.*;

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
		long minute = offseMinutes % 60;
		long hour = (offseMinutes - minute) / 60;
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, (int) hour);
		now.set(Calendar.MINUTE, (int) minute);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}

	public void setOffset(Date date) {

		offseMinutes = date.getTime() / 60000;
		offseMinutes = offseMinutes + 60;
		if(offseMinutes < 0){
			offseMinutes = 0;
		}
	}

	public String getOffsetStr() {
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
		long minute = offseMinutes % 60;
		long hour = (offseMinutes - minute) / 60;
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, (int) hour);
		now.set(Calendar.MINUTE, (int) minute);
		now.set(Calendar.SECOND, 0);

		return fmt.format(now.getTime());
	}

	public String retreiveIdentifier() {
		return getName();
	}

	public int compareTo(ActionConfig actionConfig) {
		return actionConfig.retreiveIdentifier().compareTo(getName());
	}

}
