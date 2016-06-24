/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director.guice;

/**
 * Abstract Mojo to injecting Guice Dependencies into the mojo
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */

import lombok.Getter;
import lombok.Setter;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class GuiceAbstractMojo extends AbstractMojo {

    @Getter
    @Setter
    @Parameter(property = "workspace")
    protected String workspace;

    public GuiceAbstractMojo() {
        GuiceInjectorWrapper.injectMembers(this);
    }

}