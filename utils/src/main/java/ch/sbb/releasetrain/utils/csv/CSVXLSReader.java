/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.csv;

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