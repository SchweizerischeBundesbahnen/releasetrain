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
public interface CVSXLSReader {

    /**
     * return's a column as List
     */
    List<String> getListFromColoumn(String spalte);

    /**
     * returns 2 coloums as Map getMapFromXLS("id","ear")
     */
    Map<String, String> getMapFromXLS(String spalte1, String spalte2);

}