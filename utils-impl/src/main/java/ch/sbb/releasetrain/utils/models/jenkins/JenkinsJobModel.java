/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.models.jenkins;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ch.sbb.releasetrain.utils.models.Recognizable;

/**
 * Represents a Jenkins Job from the Jenkins over the /api/xml
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.9, 2015
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "job")
public class JenkinsJobModel implements Recognizable<JenkinsJobModel> {

    private String name;
    private String color;
    private String url;
    private boolean running = false;

    public boolean isGreen() {
        return !running && color.equals("green");
    }

    public boolean isUnstable() {
        return !running && color.equals("yellow");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int compareTo(JenkinsJobModel o) {
        return name.compareTo(o.getName());
    }

    public String getId() {
        return name;
    }

    public boolean isContinuousJob() {
        return name.endsWith(".continuous");
    }

    public String toString() {
        return "JenkinsJobModel [name=" + name + "]";
    }


}
