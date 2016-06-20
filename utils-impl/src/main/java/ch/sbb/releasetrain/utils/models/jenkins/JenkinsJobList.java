/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.models.jenkins;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * 
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.9, 2015
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "listView")
public class JenkinsJobList {

    @XmlElement(name = "job")
    private List<JenkinsJobModel> jobs = new ArrayList<>();

    public List<JenkinsJobModel> getJobs() {
        return jobs;
    }

}
