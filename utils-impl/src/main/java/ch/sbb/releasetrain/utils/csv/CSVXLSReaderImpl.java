/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.csv;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import ch.sbb.releasetrain.utils.config.GlobalConfig;
import ch.sbb.releasetrain.utils.http.HttpUtil;

import com.google.inject.Inject;

/**
 * reads informations to a "product" out of the product.csv file and returns the values as map or coloumn list
 */
@Slf4j
public class CSVXLSReaderImpl implements CSVXLSReader {

    @Inject
    private HttpUtil http;

    @Inject
    private GlobalConfig config;

    @Setter
    private String text;

    @Setter
    private String fileUrlKey = "product.config.url";

    @Override
    public List<String> getListFromColoumn(String spalte) {
        return new ArrayList<>(getMapFromXLS(spalte, spalte).values());
    }

    @Override
    public Map<String, String> getMapFromXLS(String coloumn1, String coloumn2) {

        Map<String, String> ret = new HashMap<>();
        try {

            if (text == null) {
                text = http.getPageAsString(config.get(fileUrlKey));
            }

            final CSVParser parser = new CSVParser(new StringReader(text), CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader());

            for (CSVRecord record : parser.getRecords()) {
                String c1 = record.get(coloumn1);
                String c2 = record.get(coloumn2);
                ret.put(c1, c2);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ret;
    }

}
