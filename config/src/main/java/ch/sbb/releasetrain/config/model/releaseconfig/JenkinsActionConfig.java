/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releaseconfig;

import lombok.Data;
import ch.sbb.releasetrain.utils.model.Recognizable;

/**
 * Representation of a Jenkins Action Config retreived from a storage provider (Ex: GIT Repo)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public class JenkinsActionConfig extends ActionConfig implements Recognizable<JenkinsActionConfig> {

    private String jenkinsUrl;

    private String jenkinsBuildToken;

    private String jenkinsJobname;

    @Override
    public int compareTo(JenkinsActionConfig jenkinsActionConfig) {
        return super.compareTo(jenkinsActionConfig);
    }

    @Override
    public String getName() {
        return "jenkinsAction";
    }

    @Override
    public int getOffsetHours() {
        return super.offsetHours;
    }

    @Override
    public void setOffsetHours(int hours) {
        super.offsetHours = hours;
    }
}