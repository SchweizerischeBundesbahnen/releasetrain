/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.cisi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ch.sbb.releasetrain.utils.models.Recognizable;

/**
 * Cisi Multiple Cluster Model (Es koennen mehrere Cluster gleichzeitig laufen)
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.9, 2015
 */
public class CheckableModel implements Recognizable<CheckableModel> {

    protected String name;
    protected int port;
    protected int branch;
    protected boolean single = false;
    private String branchString;
    private EnvironmentOracle mightyOracle;

    private String host = "wasd85cisia1.sbb.ch";

    private List<UrlCh> secondaryUrls;
    private UrlCh primaryUrl;

    public CheckableModel() {

    }

    public CheckableModel(String name, String port, EnvironmentOracle mightyOracle, Map<String, String> map,
            String host) {
        this.mightyOracle = mightyOracle;
        this.port = Integer.parseInt(port);
        this.port = this.port + 2;
        if (host != null) {
            this.host = host;
        }

        name = name.replace("_cl", "");
        try {
            branch = Integer.parseInt(name.replaceAll("\\D", ""));
        } catch (NumberFormatException e) {
            branch = 0;
        }
        this.name = name.replace("cisi", "").replace("" + branch, "");

        if (("" + branch).length() == 2) {
            branchString = StringUtils.left(("" + branch), 1) + "." + StringUtils.right(("" + branch), 1);
        }
        if (("" + branch).length() == 3) {
            branchString = StringUtils.left(("" + branch), 1) + "." + StringUtils.right(("" + branch), 2);
        }

        String sl = map.get(this.getProduct());

        if (sl != null && sl.equals("x")) {
            single = true;
        }

    }

    public String getClusterID() {

        if (host.contains("wasi85")) {
            return "-";
        }

        if (host.contains("wast85")) {
            return "-";
        }

        if (host.contains("wasp85")) {
            return "-";
        }

        if (single) {
            return "cisi_" + this.getProduct() + "_" + this.getEnvironment() + "_cl";
        }

        if (getBranchString() == null) {
            return "";
        }

        return "cisi" + "_" + getProduct() + "_" + getEnvironment() + "_" + getBranchString().replace(".", "") + "_cl";
    }

    public String getClusterIDWithoutClAtTheEnd() {
        return getClusterID().replace("_cl", "");
    }

    @Override
    public int compareTo(CheckableModel o) {
        return o.name.compareTo(name);
    }

    @Override
    public String getId() {
        return "" + port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replace("cisi", "");
    }

    public String getPort() {
        return "" + port;
    }

    public String getEnvironment() {

        if (host.contains("wasi85")) {
            return "inte";
        }

        if (host.contains("wast85")) {
            return "test";
        }

        if (host.contains("wasp85")) {
            return "prod";
        }

        if (name.endsWith("t") || name.contains("test")) {
            return "test";
        }
        if (name.endsWith("d") || name.contains("devl")) {
            return "devl";
        }
        if (name.endsWith("i") || name.contains("inte")) {
            return "inte";
        }
        return "unknown";
    }

    public String getProduct() {

        if (this.name == null) {
            return "unknown";
        }

        if (this.name.contains("_")) {
            return name.split("_")[1];
        }

        return name;

    }

    public UrlCh getPrimaryCheckUrl() {

        if (primaryUrl != null) {
            return primaryUrl;
        }

        if (getProduct().equals("mobzk")) {

            if (getEnvironment().equals("devl")) {
                primaryUrl = new UrlCh(
                        "http://" + host + ":" + port + "/cisi/" + getProduct() + "/" + this.branchString + "/check",
                        "sbb access", "check");
            } else {
                primaryUrl = new UrlCh("http://" + host + ":" + port + "/cisi/" + getProduct() + "/devtest/"
                        + this.branchString + "/check", "sbb access", "check");
            }

            return primaryUrl;
        }

        if (getProduct().equals("zfi")) {
            // TODO remove "http://wasd85cisia1.sbb.ch:" + port + "/cisi/" +
            // getProduct() + "/check";
            primaryUrl = new UrlCh("http://" + host + ":" + port + "/cisi/" + getProduct() + "_1.15/check",
                    "sbb access", "check");
            return primaryUrl;
        }

        if (single) {
            primaryUrl = new UrlCh("http://" + host + ":" + port + "/cisi/" + getProduct() + "/check", "sbb access",
                    "check");
            return primaryUrl;

        }
        primaryUrl = new UrlCh(
                "http://" + host + ":" + port + "/cisi/" + getProduct() + "_" + this.branchString + "/check",
                "sbb access", "check");
        return primaryUrl;

    }

    public List<UrlCh> getSecondaryCheckUrls() {

        if (secondaryUrls != null) {
            return secondaryUrls;
        }

        List<UrlCh> ret = new ArrayList<>();

        if (getProduct().equals("mobzkNOT")) {
            if (getEnvironment().equals("devl")) {
                ret.add(new UrlCh("http://" + host + ":" + port + "/cisi/" + getProduct() + "/" + this.branchString
                        + "/check/?filter=Checked%20Services:&level=2", "sbb access", "service"));
            } else {
                ret.add(new UrlCh("http://" + host + ":" + port + "/cisi/" + getProduct() + "/devtest/"
                        + this.branchString + "/check/?filter=Checked%20Services:&level=2", "sbb access", "service"));
            }

        }

        if (getProduct().equals("fos")) {
            ret.add(new UrlCh("http://" + host + ":" + port + "/cisi/" + getProduct() + "/process", "Eclipse Scout",
                    "process"));
            ret.add(new UrlCh("http://" + host + ":" + port + "/cisi/" + getProduct() + "/rest/check", "sbb access",
                    "rest"));
            ret.add(new UrlCh("http://" + host + ":" + port + "/cisi/" + getProduct() + "/remoting/IEvuService",
                    "Error 500: java.io.EOFException", "remoting"));

        }

        if (getProduct().equals("angebot") || getProduct().equals("betrieb")) {
            ret.add(new UrlCh(
                    "http://" + host + ":" + port + "/cisi/" + getProduct() + "_" + this.branchString + "/process",
                    "Eclipse Scout", "process"));
        }
        secondaryUrls = ret;
        return secondaryUrls;

    }

    @Override
    public String toString() {
        return "CisiClusterModel [branch=" + branch + ", getPort()=" + getPort() + ", getEnvironment()="
                + getEnvironment() + ", getProduct()=" + getProduct() + "]";
    }

    public String getBranchString() {
        if (this.single) {
            return "no-branch";
        }
        return branchString;
    }

}
