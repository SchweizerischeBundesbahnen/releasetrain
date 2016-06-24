/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.cisi;

import java.util.List;
import java.util.Map;

/**
 * Loads Jobs an their state from Jenkins
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.9, 2015
 */
public class EnvironmentOracleImpl implements EnvironmentOracle {
    @Override
    public List loadCheckablesFromPortsXML() {
        return null;
    }

    @Override
    public List<String> getProductIds() {
        return null;
    }

    @Override
    public List<String> getProductIdsClients() {
        return null;
    }

    @Override
    public String getMagicNumberForProduct(String product) {
        return null;
    }

    @Override
    public String getClusterForProduct(String product, String branch) {
        return null;
    }

    @Override
    public String getValueForProduct(String product, String value) {
        return null;
    }

    @Override
    public String getProductForMagicNumber(String product) {
        return null;
    }

    @Override
    public Map<String, String> getMapFromXLS(String a, String b) {
        return null;
    }

    @Override
    public String getClusterIdForProduct(String product, String version, String stage) {
        return null;
    }
    /**
     * private static final String CONFL = "https://confluence.sbb.ch/x/JIgqKg";
     * 
     *
     *         protected HttpUtil http;
     *
     *         protected GlobalConfig config;
     *
     *         protected CSVXLSReader xls;
     *         private Map<String, CheckableModel> clusterMap = new HashMap<>();
     * 
     * @Override
     *           public List<CheckableModel> loadCheckablesFromPortsXML() {
     *           // http.setSetJenkinsAutentication(true);
     * 
     *           String content = http.getPageAsString(getApiurl());
     * 
     *           String cut = StringUtils.substringBetween(content, "<clusterdefinitions>", "</clusterdefinitions>");
     * 
     *           String[] list = StringUtils.substringsBetween(cut, "<cluster name=", "type=");
     *           HashSet<CheckableModel> set = new HashSet<>();
     *           for (String string : list) {
     *           String[] neu = string.replace("\"", "").replace(" ", "").replace("=", "").split("value");
     * 
     *           boolean single = false;
     *           Map<String, String> map = this.xls.getMapFromXLS("id", "singlecluster");
     * 
     *           CheckableModel model = new CheckableModel(neu[0], neu[1], this, map, null);
     *           set.add(model);
     *           clusterMap.put(model.getProduct() + model.getBranchString(), model);
     *           }
     *           return new ArrayList<>(set);
     *           }
     * 
     *           public Map<String, String> getMapFromXLS(String a, String b) {
     *           return this.xls.getMapFromXLS(a, b);
     *           }
     * 
     *           public String getApiurl() {
     *           return config.get("cluster.config.git");
     *           }
     * 
     * @Override
     *           public CheckableModel getClusterForProduct(String product, String branch) {
     *           return this.clusterMap.get(product + branch);
     *           }
     * 
     * @Override
     *           public List<String> getProductIds() {
     *           return xls.getListFromColoumn("id");
     *           }
     * 
     * @Override
     *           public List<String> getProductIdsClients() {
     *           Map<String, String> clients = xls.getMapFromXLS("id", "client");
     *           List<String> ret = new ArrayList<>();
     *           for (String key : clients.keySet()) {
     *           if (clients.get(key).contains("ch.sbb.cisi")) {
     *           ret.add(key);
     *           }
     *           }
     *           return ret;
     *           }
     * 
     * @Override
     *           public String getValueForProduct(String product, String value) {
     *           String ret = xls.getMapFromXLS("id", value).get(product);
     *           if (ret == null || ret.isEmpty()) {
     *           ret = "na: " + CONFL;
     *           }
     *           return ret;
     *           }
     * 
     * @Override
     *           public String getMagicNumberForProduct(String product) {
     *           String ret = xls.getMapFromXLS("id", "magicnumber").get(product);
     *           if (ret == null || ret.isEmpty()) {
     *           ret = "na: " + CONFL;
     *           }
     *           return ret;
     *           }
     * 
     * @Override
     *           public String getProductForMagicNumber(String number) {
     *           String ret = xls.getMapFromXLS("magicnumber", "id").get(number);
     *           if (ret == null || ret.isEmpty()) {
     *           ret = "na: " + CONFL;
     *           }
     *           return ret;
     *           }
     * 
     *           /**
     *           public static void main(String[] args) {
     *           EnvironmentOracleImpl or = new EnvironmentOracleImpl();
     *           or.http = new HttpUtilImpl(null);
     *           or.xls = new CSVXLSReaderImpl();
     *           or.http.setSetJenkinsAutentication(true);
     *           or.config = new GlobalConfigImpl();
     *           System.out.println("mn:" + or.getProductForMagicNumber("a"));
     *           System.out.println("client:" + or.getValueForProduct("fos", "client"));
     *           System.out.println("clientids:" + or.getProductIdsClients());
     *           List<CheckableModel> clusters = or.loadCheckablesFromPortsXML();
     *           for (CheckableModel cis : clusters) {
     *           System.out.println(cis.getSecondaryCheckUrls());
     *           }
     *           }
     **/


}
