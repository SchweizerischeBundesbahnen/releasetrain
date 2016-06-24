/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director.model;

import lombok.Data;

/**
 * Represents a Version schema
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public class DefaultVersionSchemaImpl {

    private final static String SNAPSHOT = "-SNAPSHOT";

    private int major;
    private int minor;
    private int increment;

    private boolean snapshot = false;

    public DefaultVersionSchemaImpl(String version) {
        if (version.contains(SNAPSHOT)) {
            version = version.replace(SNAPSHOT, "");
            snapshot = true;
        }
        String[] str = version.split("\\.");
        major = Integer.parseInt(str[0]);
        minor = Integer.parseInt(str[1]);
        increment = Integer.parseInt(str[2]);
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(major);
        buff.append(".");
        buff.append(minor);
        buff.append(".");
        buff.append(increment);
        if (snapshot) {
            buff.append(SNAPSHOT);
        }
        return buff.toString();
    }

}
