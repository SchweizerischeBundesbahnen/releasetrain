/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.config.model.ReleaseConfig;
import ch.sbb.releasetrain.director.modelaccessor.YamlModelAccessor;
import ch.sbb.releasetrain.utils.http.HttpUtil;

/**
 * Provides Acces to the Release Configs, stored in a storage like GIT
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Slf4j
@Component
public class ConfigAccessorImpl implements ConfigAccessor {

    @Value("config.baseurl")
    private String baseURL;

    @Autowired
    private HttpUtil http;

    private YamlModelAccessor<ReleaseConfig> xstream = new YamlModelAccessor<>();

    @Override
    public ReleaseConfig readConfig(String name) {
        String xml = http.getPageAsString(baseURL + "/" + name + "xml");
        return xstream.convertEntrys(xml).get(0);
    }

}
