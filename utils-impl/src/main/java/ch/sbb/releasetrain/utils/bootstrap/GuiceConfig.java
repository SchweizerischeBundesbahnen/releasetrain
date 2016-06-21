/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.bootstrap;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import ch.sbb.releasetrain.utils.cisi.EnvironmentOracle;
import ch.sbb.releasetrain.utils.cisi.EnvironmentOracleImpl;
import ch.sbb.releasetrain.utils.config.GlobalConfig;
import ch.sbb.releasetrain.utils.config.GlobalConfigImpl;
import ch.sbb.releasetrain.utils.file.FileUtil;
import ch.sbb.releasetrain.utils.file.FileUtilImpl;
import ch.sbb.releasetrain.utils.http.HttpUtil;
import ch.sbb.releasetrain.utils.http.HttpUtilImpl;

import com.google.inject.AbstractModule;


/**
 * Google Guice Base Config
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public class GuiceConfig extends AbstractModule {

    @Override
    protected void configure() {

        bind(Log.class).to(SystemStreamLog.class);

        bind(FileUtil.class).to(FileUtilImpl.class);

        bind(HttpUtil.class).to(HttpUtilImpl.class);

        bind(GlobalConfig.class).to(GlobalConfigImpl.class);

        bind(EnvironmentOracle.class).to(EnvironmentOracleImpl.class);

    }
}