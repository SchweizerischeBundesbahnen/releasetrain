/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releasecalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ch.sbb.releasetrain.director.modelaccessor.Recognizable;

/**
 * Represents a scheduled Release Calendar Event, to trigger a Release Action
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
@Slf4j
public class ReleaseEvent implements Recognizable<ReleaseEvent> {

    private transient final static String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private String date = "1900-01-01 00:01";

    private String releaseVersion;

    private String snapshotVersion;

    private String maintenaceVersion;

    private String actionType;

    public Date retreiveAsDate() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public void putAsDate() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        date = format.toString();
    }

    @Override
    public int compareTo(ReleaseEvent o) {
        return this.retreiveIdentifier().compareTo(o.retreiveIdentifier());
    }

    @Override
    public String retreiveIdentifier() {
        return date;
    }

}
