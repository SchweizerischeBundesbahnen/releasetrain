/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

/**
 * reads informations to a "product" out of the product.csv file and returns the values as map or coloumn list
 */
@Slf4j
@Component
public class CSVXLSReaderImpl implements CSVXLSReader {

    @Override
    public List<String> getColoumnAsList(String coloumn, String content) {
        return new ArrayList<>(getMapFrom2Coloums(coloumn, coloumn, content).values());
    }

    @Override
    public List<List<String>> getAllRows(String content) {
        List<List<String>> ret = new ArrayList<>();
        try {
            final CSVParser parser = getParser(content);

            Map<String, Integer> header = parser.getHeaderMap();

            for (CSVRecord record : parser.getRecords()) {
                List<String> row = new ArrayList<>();
                for (int i : header.values()) {
                    row.add(record.get(i));
                }
                ret.add(row);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public Map<String, String> getMapFrom2Coloums(String coloumn1, String coloumn2, String content) {

        Map<String, String> ret = new HashMap<>();
        try {

            final CSVParser parser = getParser(content);

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

    private CSVParser getParser(String content) {
        try {
            return new CSVParser(new StringReader(content), CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
