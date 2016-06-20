/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
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
 * Guice Config
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.10, 2015
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