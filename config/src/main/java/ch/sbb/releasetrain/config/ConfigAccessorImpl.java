/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.config.model.ReleaseConfig;
import ch.sbb.releasetrain.config.model.email.MailReceiver;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseEvent;
import ch.sbb.releasetrain.utils.http.HttpUtil;
import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;

/**
 * Provides Acces to the Release Configs, stored in a storage like GIT
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
@Component
public class ConfigAccessorImpl implements ConfigAccessor {

    @Value("config.baseurl")
    @Setter
    private String baseURL;

    @Autowired
    @Setter
    private HttpUtil http;

    private YamlModelAccessor<ReleaseConfig> accessorRelease = new YamlModelAccessor<>();
    private YamlModelAccessor<MailReceiver> accessorMailing = new YamlModelAccessor<>();

    @Override
    public ReleaseConfig readConfig(String name) {
        String url = baseURL + "/" + name + ".yaml";
        String page = http.getPageAsString(url);
        return accessorRelease.convertEntry(page);
    }

    @Override
    public List<MailReceiver> readMailReceiver() {
        String url = baseURL + "/mailinglists.yaml";
        String page = http.getPageAsString(url);
        return accessorMailing.convertEntrys(page);
    }

    @Override
    public List<MailReceiver> readMailReveiverForMailinglist(String... mailinglists) {
        List<MailReceiver> ret = new ArrayList<>();
        for (MailReceiver mailRec : readMailReceiver()) {
            for (String mailinglist : mailinglists) {
                if (mailRec.getMailinglist().contains(mailinglist)) {
                    ret.add(mailRec);
                }
            }
        }
        return ret;
    }

    @Override
    public List<ReleaseEvent> readReleaseCalendars() {
        return null;
    }
}
