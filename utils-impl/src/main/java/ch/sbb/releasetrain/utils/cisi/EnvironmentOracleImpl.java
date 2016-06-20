/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.cisi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import ch.sbb.releasetrain.utils.config.GlobalConfig;
import ch.sbb.releasetrain.utils.csv.CVSXLSReader;
import ch.sbb.releasetrain.utils.http.HttpUtil;

/**
 * Loads Jobs an their state from Jenkins
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.9, 2015
 */
public class EnvironmentOracleImpl implements EnvironmentOracle {

    private static final String CONFL = "https://confluence.sbb.ch/x/JIgqKg";
    @Inject
    protected HttpUtil http;
    @Inject
    protected GlobalConfig config;
    @Inject
    protected CVSXLSReader xls;
    private Map<String, CheckableModel> clusterMap = new HashMap<>();

    @Override
    public List<CheckableModel> loadCheckablesFromPortsXML() {
        http.setSetJenkinsAutentication(true);

        String content = http.getPageAsString(getApiurl());

        String cut = StringUtils.substringBetween(content, "<clusterdefinitions>", "</clusterdefinitions>");

        String[] list = StringUtils.substringsBetween(cut, "<cluster name=", "type=");
        HashSet<CheckableModel> set = new HashSet<>();
        for (String string : list) {
            String[] neu = string.replace("\"", "").replace(" ", "").replace("=", "").split("value");

            boolean single = false;
            Map<String, String> map = this.xls.getMapFromXLS("id", "singlecluster");

            CheckableModel model = new CheckableModel(neu[0], neu[1], this, map, null);
            set.add(model);
            clusterMap.put(model.getProduct() + model.getBranchString(), model);
        }
        return new ArrayList<>(set);
    }

    public Map<String, String> getMapFromXLS(String a, String b) {
        return this.xls.getMapFromXLS(a, b);
    }

    public String getApiurl() {
        return config.get("cluster.config.git");
    }

    @Override
    public CheckableModel getClusterForProduct(String product, String branch) {
        return this.clusterMap.get(product + branch);
    }

    @Override
    public List<String> getProductIds() {
        return xls.getListFromColoumn("id");
    }

    @Override
    public List<String> getProductIdsClients() {
        Map<String, String> clients = xls.getMapFromXLS("id", "client");
        List<String> ret = new ArrayList<>();
        for (String key : clients.keySet()) {
            if (clients.get(key).contains("ch.sbb.cisi")) {
                ret.add(key);
            }
        }
        return ret;
    }

    @Override
    public String getValueForProduct(String product, String value) {
        String ret = xls.getMapFromXLS("id", value).get(product);
        if (ret == null || ret.isEmpty()) {
            ret = "na: " + CONFL;
        }
        return ret;
    }

    @Override
    public String getMagicNumberForProduct(String product) {
        String ret = xls.getMapFromXLS("id", "magicnumber").get(product);
        if (ret == null || ret.isEmpty()) {
            ret = "na: " + CONFL;
        }
        return ret;
    }

    @Override
    public String getProductForMagicNumber(String number) {
        String ret = xls.getMapFromXLS("magicnumber", "id").get(number);
        if (ret == null || ret.isEmpty()) {
            ret = "na: " + CONFL;
        }
        return ret;
    }

    /**
     * public static void main(String[] args) {
     * EnvironmentOracleImpl or = new EnvironmentOracleImpl();
     * or.http = new HttpUtilImpl(null);
     * or.xls = new CVSXLSReaderImpl();
     * or.http.setSetJenkinsAutentication(true);
     * or.config = new GlobalConfigImpl();
     * System.out.println("mn:" + or.getProductForMagicNumber("a"));
     * System.out.println("client:" + or.getValueForProduct("fos", "client"));
     * System.out.println("clientids:" + or.getProductIdsClients());
     * List<CheckableModel> clusters = or.loadCheckablesFromPortsXML();
     * for (CheckableModel cis : clusters) {
     * System.out.println(cis.getSecondaryCheckUrls());
     * }
     * }
     **/

    @Override
    public String getClusterIdForProduct(String product, String version, String stage) {

        if (stage.equals("d") || stage.equals("dev")) {
            stage = "devl";
        }

        if (stage.equals("i") || stage.equals("int") || stage.equals("intg")) {
            stage = "inte";
        }

        if (stage.equals("t")) {
            stage = "test";
        }

        // singlecluster
        Map<String, String> ret = xls.getMapFromXLS("id", "singlecluster");

        if (ret.get(product).equals("x")) {
            return "cisi_" + product + "_" + stage;
        }

        String[] versArr = version.split("\\.");
        String vers = versArr[0] + versArr[1];

        return "cisi_" + product + "_" + stage + "_" + vers;

    }

}
