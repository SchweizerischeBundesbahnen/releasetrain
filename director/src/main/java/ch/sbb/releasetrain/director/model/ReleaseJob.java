/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import ch.sbb.releasetrain.director.modelaccessor.Recognizable;

/**
 * Representation of a release Build Job with his subjobs
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public class ReleaseJob implements Recognizable<ReleaseJob> {

    private transient static String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private String date = "1900-01-01 00:01";

    private ReleaseJob parent = null;

    private List<ReleaseJob> children = new ArrayList<>();

    private boolean thisJobDone = false;

    // build type (to be configured / mapped to jenkins build in )
    private String typeOfBuild;

    // a product to reelease
    private String product;

    // maven release version
    private String releaseVersion = "";

    // next snapshot version on develop branch
    private String developmentVersion = "";

    // next snapshot version on a possible maintenanceBranch
    private String developmentVersionMaintenance = "";

    // in case if we need of a buisiness version on this release
    private String businessVersion = "defaultBusinessVersion";

    private String responsibleMailingList = "";

    private String description = "defaultDescription";

    private Map<String, String> injectVersions = new HashMap<String, String>();

    private List<String> deployToDestination = new ArrayList<String>();

    public void addInjectVersions(final String product, final String version) {
        this.injectVersions.put(product, version);
    }

    public void addDeployToDestination(final String destination) {
        this.deployToDestination.add(destination);
    }

    public Date getAsDateDate() {
        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(date);
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate() {
        return date;
    }

    public boolean areChildJobsDone() {
        if (this.children.isEmpty()) {
            return thisJobDone;
        } else {
            for (ReleaseJob child : children) {
                if (!areChildJobsDone()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean areAllJobsDoneInThisTree() {
        if (this.parent != null) {
            areAllJobsDoneInThisTree();
        }
        return areChildJobsDone();
    }

    /**
     * returns new ArrayList<String> if deployTo==null
     */
    public List<String> getDeployTo() {
        if (deployToDestination.isEmpty() && this.parent != null) {
            return parent.deployToDestination;
        }
        return deployToDestination;
    }

    @Override
    public String getId() {
        return this.date;
    }

    @Override
    public int compareTo(ReleaseJob releaseJob) {
        return this.getId().compareTo(releaseJob.getId());
    }

    public String getResponsibleMailingList() {
        if (this.responsibleMailingList.isEmpty() && this.parent != null) {
            this.parent.getResponsibleMailingList();
        }
        return this.responsibleMailingList;
    }
}
