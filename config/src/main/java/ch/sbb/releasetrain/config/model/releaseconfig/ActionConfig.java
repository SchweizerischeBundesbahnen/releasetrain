/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releaseconfig;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * Representation of a Action retreived from a storage provider (Ex: GIT Repo)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public abstract class ActionConfig {

    protected int offsetHours;
    private Map<String, String> properties = new HashMap<>();

    public abstract String getName();

    public abstract int getOffsetHours();

    public abstract void setOffsetHours(int hours);

    public String retreiveIdentifier() {
        return getName();
    }


    public int compareTo(ActionConfig actionConfig) {
        return actionConfig.retreiveIdentifier().compareTo(getName());
    }

}
