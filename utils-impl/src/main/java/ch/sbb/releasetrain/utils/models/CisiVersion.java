/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.models;

/**
 * Represents a Nets version
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.6, 2015
 */
public class CisiVersion {

    private final static String SNAPSHOT = "-SNAPSHOT";

    private int major;
    private int minor;
    private int sprint;
    private int branchbuild;

    private boolean mavenSNAPSHOT = false;

    public CisiVersion(String version) {

        if (version.contains(SNAPSHOT)) {
            version = version.replace(SNAPSHOT, "");
            mavenSNAPSHOT = true;
        }

        String[] str = version.split("\\.");
        major = Integer.parseInt(str[0]);
        minor = Integer.parseInt(str[1]);
        sprint = Integer.parseInt(str[2].substring(0, 2));
        branchbuild = Integer.parseInt(str[2].substring(2, 4));

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(major);
        builder.append(".");
        builder.append(minor);
        builder.append(".");
        builder.append(sprint);
        builder.append(".");
        builder.append(branchbuild);
        if (mavenSNAPSHOT) {
            builder.append(SNAPSHOT);
        }
        return builder.toString();
    }

    private String get1() {
        return "" + major;
    }

    private String get2() {
        return "" + minor;
    }

    private String get3() {
        return "" + String.format("%02d", sprint) + String.format("%02d", branchbuild);
    }

    public String getVersion() {
        String snap = "";
        if (isMavenSNAPSHOT()) {
            snap = SNAPSHOT;
        }

        return get1() + "." + get2() + "." + get3() + snap;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getSprint() {
        return sprint;
    }

    public int getBranchbuild() {
        return branchbuild;
    }

    public boolean isMavenSNAPSHOT() {
        return mavenSNAPSHOT;
    }


    public String getTagSVNUrl() {
        return "https://svn.sbb.ch/svn/cisi/tags/release/${major}.${minor}/${major}.${minor}.${sprint}/${major}.${minor}.${sprint}${branchbuild}/cisi/".replace("${major}", "" + this.major)
                .replace("${minor}", "" + this.minor)
                .replace("${sprint}", "" + this.sprint).replace("${branchbuild}", "" + this.branchbuild);
    }

    public String getSVNUrl() {
        return "https://svn.sbb.ch/svn/cisi/branches/release/${major}.${minor}/${major}.${minor}.${sprint}/cisi/".replace("${major}", "" + this.major).replace("${minor}", "" + this.minor)
                .replace("${sprint}", "" + this.sprint);
    }

}
