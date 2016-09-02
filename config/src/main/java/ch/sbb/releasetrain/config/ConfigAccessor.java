/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import java.util.List;

import ch.sbb.releasetrain.config.model.email.MailReceiver;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseCalendarEvent;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;

/**
 * Provides Acces to the Release Configs, stored in a storage like GIT
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
public interface ConfigAccessor {

    ReleaseConfig readConfig(String name);

    List<String> readAllConfigs();

    void writeConfig(String name, ReleaseConfig config);

    void deleteConfig(String name);

    List<MailReceiver> readMailReceiver();

    List<MailReceiver> readMailReveiverForMailinglist(String... mailinglists);

    List<ReleaseCalendarEvent> readReleaseCalendar(String action);

    void writeReleaseCalendar(List<ReleaseCalendarEvent> cal, String action);


}
