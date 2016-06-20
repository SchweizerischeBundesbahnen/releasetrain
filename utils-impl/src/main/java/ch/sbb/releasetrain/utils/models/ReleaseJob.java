/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of a release Build Job
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.6, 2015
 */
public class ReleaseJob implements Recognizable<ReleaseJob> {

    private transient static String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    // 2015-12-12 10:44
    private String date = "1900-01-01 00:01";

    // client, server, tram, unomodel
    private String typ;

    // maven release version
    private String relVersion = "";

    // maven next snapshot version
    private String devVersion = "";

    private String betreuerMailList = "marthaler";

    private String cisiVersion = "1602-RC1";

    private String beschreibung = "";

    private Map<String, String> injectVersions = new HashMap<>();

    private List<String> deployTo = new ArrayList<>();


    public void addInjectVersions(final String project, final String version) {
        this.injectVersions.put(project, version);
    }

    public void addSeployTo(final String stage) {
        this.deployTo.add(stage);
    }

    public int compareTo(final ReleaseJob o) {
        return this.getId().compareTo(o.getId());
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

    public void setDate(final String date) {
        this.date = date;
    }

    public String getColor() {
        if (typ.contains("trunk")) {
            return "danger";
        }
        if (typ.contains("mail")) {
            return "info";
        }
        return "warning";
    }

    /**
     * @return - returns new ArrayList<String> if deployTo==null
     */
    public List<String> getDeployTo() {
        if (deployTo == null) {
            return new ArrayList<>();
        }
        return deployTo;
    }

    public void setDeployTo(final List<String> deployTo) {
        this.deployTo = deployTo;
    }

    public String getDevVersion() {
        return devVersion;
    }

    public void setDevVersion(final String devVersion) {
        this.devVersion = devVersion;
    }

    @Override
    public String getId() {
        return date;
    }

    public Map<String, String> getInjectVersions() {
        return injectVersions;
    }

    public void setInjectVersions(final Map<String, String> injectVersions) {
        this.injectVersions = injectVersions;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(final String typ) {
        this.typ = typ;
    }

    public String getRelVersion() {
        return relVersion;
    }

    public void setRelVersion(final String relVersion) {
        this.relVersion = relVersion;
    }

    public void setDateAsDate(final Date date) {
        this.date = new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    public boolean isDeployedTo(final String deployToServer) {
        for (final String deployTo : getDeployTo()) {
            if (deployTo.equals(deployToServer)) {
                return true;
            }
        }
        return false;
    }

    public String getCisiVersion() {
        return cisiVersion;
    }

    public void setCisiVersion(String cisiVersion) {
        this.cisiVersion = cisiVersion;
    }


    public String getBetreuerMailList() {
        return betreuerMailList;
    }

    public void setBetreuerMailList(String betreuerMailList) {
        this.betreuerMailList = betreuerMailList;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

}
