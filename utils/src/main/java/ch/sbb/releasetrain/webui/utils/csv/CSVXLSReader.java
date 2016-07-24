/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.utils.csv;

import java.util.List;
import java.util.Map;

/**
 * Gets access to a xls-csv file
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface CSVXLSReader {

    /**
     * return's a column as List
     */
    List<String> getColoumnAsList(String spalte, String content);

    /**
     * return's a column as List
     */
    List<List<String>> getAllRows(String content);

    /**
     * returns 2 coloums as Map getMapFrom2Coloums("id","ear")
     */
    Map<String, String> getMapFrom2Coloums(String coloumn1, String coloumn2, String content);

}