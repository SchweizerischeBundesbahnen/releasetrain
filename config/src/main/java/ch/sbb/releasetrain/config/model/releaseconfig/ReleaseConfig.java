/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releaseconfig;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ch.sbb.releasetrain.utils.model.Recognizable;

/**
 * Representation of a whole Release Job retreived from a storage provider (Ex: GIT Repo)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public class ReleaseConfig implements Recognizable<ReleaseConfig> {

    private String typ;

    private List<ActionConfig> actions = new ArrayList<>();

    @Override
    public String retreiveIdentifier() {
        return typ;
    }

    @Override
    public int compareTo(ReleaseConfig releaseConfig) {
        return releaseConfig.retreiveIdentifier().compareTo(typ);
    }

}
