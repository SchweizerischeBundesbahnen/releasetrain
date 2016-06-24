/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.config;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Global Config for all the Mojos
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.6, 2015
 */
@Slf4j
public class GlobalConfigImpl implements GlobalConfig {

    private final static String CONFIG_FILE = "config.properties";
    private Configuration config;

    public GlobalConfigImpl() {
        Configurations configs = new Configurations();
        try {
            config = configs.properties(new File(CONFIG_FILE));
        } catch (ConfigurationException e) {
            log.error(e.getMessage(), e);
        }

    }

    public String get(String id) {

        if (System.getProperty(id) != null) {
            return System.getProperty(id);
        }
        String ret = config.getString(id);

        if (ret == null) {
            log.error("no config for key: " + id + " found in Env or " + CONFIG_FILE);
        }
        return ret;
    }

}
