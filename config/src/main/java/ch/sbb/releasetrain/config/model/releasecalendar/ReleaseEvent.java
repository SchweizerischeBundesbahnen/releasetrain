/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releasecalendar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ch.sbb.releasetrain.utils.model.Recognizable;

/**
 * Represents a scheduled Release Calendar Event, to trigger a Release Action
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
@Slf4j
@ToString
public class ReleaseEvent implements Recognizable<ReleaseEvent> {

    private transient final static String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private String date = "1900-01-01 00:01";

    private String releaseVersion;

    private String snapshotVersion;

    private String maintenaceVersion;

    private String actionType;

    public LocalDateTime retreiveAsDate() {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDateTime.from(formater.parse(date));
    }

    public void putAsDateTime(LocalDateTime dateTime) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        date = format.format(dateTime);
    }

    @Override
    public int compareTo(ReleaseEvent o) {
        return this.date.compareTo(o.getDate());
    }

    @Override
    public String retreiveIdentifier() {
        return releaseVersion;
    }

}
