/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.mojo;

import org.apache.maven.plugin.logging.Log;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Wrapper for Guice Injector (to inject the injector)
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.10, 2015
 */
@Singleton
public class GuiceInjectorWrapper {

    private Injector injector;

    @Inject
    private Log log;

    private boolean trace = false;

    /**
     * Used to inject Members if Object ist created somwehere with new XY sowmhere
     * Ex. for Threads
     */
    public void injectMembers(Object obj) {
        if (injector != null) {
            if (trace) {
                log.debug("** injecting members in " + obj);
            }
            injector.injectMembers(obj);
        } else {
            log.error("** injecting members in " + obj + " not possible, no injector in GuiceInjectorWrapper");
        }
    }

    protected void setInjector(Injector injector) {
        this.injector = injector;
    }

}
