/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.config;

import java.io.File;

import lombok.extern.apachecommons.CommonsLog;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.google.inject.Singleton;

/**
 * Global Config for all the Mojos
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.6, 2015
 */
@Singleton
@CommonsLog
public class GlobalConfigImpl implements GlobalConfig {

    private final static String CONFIG_FILE = "config.properties";
    private Configuration config;

    public GlobalConfigImpl() {
        Configurations configs = new Configurations();
        try {
            Configuration config = configs.properties(new File(CONFIG_FILE));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

    }

    public String get(String id) {
        if (getFromEnv(id) != null) {
            return getFromEnv(id);
        }
        String ret = config.getString(id);

        if (ret == null) {
            log.fatal("no config for key: " + id + " found in Env or " + CONFIG_FILE);
        }
        return ret;
    }

    private String getFromEnv(String key) {
        return System.getProperty(key);
    }

}
