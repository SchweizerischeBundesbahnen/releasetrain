/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director.guice;

import lombok.extern.slf4j.Slf4j;
import ch.sbb.releasetrain.utils.bootstrap.GuiceConfig;

/**
 * Wrapper for Guice Injector, as a single instance
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
public class GuiceInjectorWrapper {

    private static Injector INJECTOR;
    private static GuiceInjectorWrapper INSTANCE;
    private AbstractModule guiceConfig = new GuiceConfig();

    public GuiceInjectorWrapper() {
        GuiceInjectorWrapper.INJECTOR = Guice.createInjector(guiceConfig);
    }

    public static void injectMembers(Object obj) {
        if (INJECTOR == null) {
            log.info("* creating GuiceInjectorWrapper: " + new GuiceInjectorWrapper());
        }
        INJECTOR.injectMembers(obj);
    }

}
