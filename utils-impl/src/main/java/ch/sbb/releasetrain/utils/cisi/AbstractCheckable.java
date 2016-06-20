/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.cisi;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Ein Checkbarer Eintrag in der Bootstrap Liste, kann sein eine WAS App / Ein
 * Batch oder ein Hazlecast Cluster
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.4, 2015
 * 
 */
public abstract class AbstractCheckable implements PrintableForTable {

    protected String source;

    protected String url;

    protected Date buildDate;

    protected String product;

    protected String color;
    protected String umgebung = "";
    private CheckableModel cisiClusterModel;
    private String checktimeInMilliSeconds = "";
    private boolean available = true;

    public AbstractCheckable(String http) {
        source = http;
    }

    public abstract String getBuildDate();

    public abstract String getBuildVersion();

    public abstract String getBuildNumber();

    public abstract boolean isValid();

    @Override
    public AbstractCheckable getServlet() {
        return this;
    }

    @Override
    public String getComputedCheckURL() {

        if (!getStatusString().isEmpty()) {
            return getStatusString();
        }

        String ret = "";
        if (cisiClusterModel.getPrimaryCheckUrl().getOk()) {
            ret = ret + "<a href=\"" + cisiClusterModel.getPrimaryCheckUrl().getUrl() + "\" target=\"_blank\" title=\""
                    + cisiClusterModel.getPrimaryCheckUrl().getName() + " \">"
                    + cisiClusterModel.getPrimaryCheckUrl().getName() + "</a>";
        } else {
            ret = ret + "<a href=\"" + cisiClusterModel.getPrimaryCheckUrl().getUrl() + "\" target=\"_blank\" title=\""
                    + cisiClusterModel.getPrimaryCheckUrl().getName() + " \"><b>"
                    + cisiClusterModel.getPrimaryCheckUrl().getName() + "</b></a>";
        }

        if (cisiClusterModel.getSecondaryCheckUrls() != null) {
            for (UrlCh ur : cisiClusterModel.getSecondaryCheckUrls()) {
                if (ur.getOk()) {
                    ret = ret + "&nbsp;<a href=\"" + ur.getUrl() + "\" target=\"_blank\" title=\"" + ur.getName()
                            + " \">" + ur.getName() + "</a>";
                } else {
                    ret = ret + "&nbsp;<a href=\"" + ur.getUrl() + "\" target=\"_blank\" title=\"" + ur.getName()
                            + " \"><b>" + ur.getName() + "</b></a>";
                }
            }
        }

        return ret;
    }

    public String getBuildDateAsString() {
        if (buildDate == null) {
            return "-";
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return fmt.format(buildDate);
    }

    public abstract String getStatusString();

    public String getUmgebung() {

        if (!umgebung.isEmpty()) {
            return umgebung;
        }

        String urlt = cisiClusterModel.getPrimaryCheckUrl().getUrl();


        if (StringUtils.containsIgnoreCase(urlt, "wasi85")) {
            return "om-was-inte";
        }

        if (StringUtils.containsIgnoreCase(urlt, "wast85")) {
            return "om-was-test";
        }

        if (StringUtils.containsIgnoreCase(urlt, "wasp85")) {
            return "om-was-prod";
        }

        if (StringUtils.containsIgnoreCase(urlt, "cisi") && this.isValid()) {
            return "cisi-was";
        }
        return "";
    }

    public String getProduct() {

        if (product != null && !product.isEmpty()) {
            return product;
        }

        if (StringUtils.containsIgnoreCase(source, "mobzk")) {
            return "mobzk";
        }

        if (StringUtils.containsIgnoreCase(source, "CIS Infra Angebot")) {
            return "angebot";
        }

        if (StringUtils.containsIgnoreCase(source, "CIS Infra Betrieb")) {
            return "betrieb";
        }

        if (StringUtils.containsIgnoreCase(source, "CISI ZFI")) {
            return "zfi";
        }

        if (StringUtils.containsIgnoreCase(source, "fos")) {
            return "fos";
        }
        return "";
    }

    public String getColor() {

        if (color != null && !color.isEmpty()) {
            return color;
        }

        String tempcolor = "success";

        if (this.getCluster().getSecondaryCheckUrls() != null) {
            for (UrlCh url : this.getCluster().getSecondaryCheckUrls()) {
                if (!url.getOk()) {
                    tempcolor = "danger";
                }
            }
        }

        if (!this.getCluster().getPrimaryCheckUrl().getOk()) {
            tempcolor = "danger";
        }

        return tempcolor;
    }

    public String getTyp() {
        return "unknown";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CheckableModel getCluster() {
        return this.cisiClusterModel;
    }

    public void setCluster(CheckableModel cisiClusterModel) {
        this.cisiClusterModel = cisiClusterModel;
    }

    public String getClusterString() {
        if (this.cisiClusterModel != null) {
            if (this.cisiClusterModel.getClusterID() != null) {
                return this.cisiClusterModel.getClusterID();
            }
        }
        return "";
    }

    public String getChecktimeInMilliSeconds() {
        return checktimeInMilliSeconds;
    }

    public void setChecktimeInMilliSeconds(String checktimeInSeconds) {
        this.checktimeInMilliSeconds = checktimeInSeconds;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}
