/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.csv;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * CVSXLS Test
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public class CSVXLSReaderTest {

    @Test
    public void testIsBuildRUNNING() throws Exception {

        InputStream in = this.getClass().getResourceAsStream("/products.csv");
        String text = IOUtils.toString(in, "UTF-8");

        CSVXLSReaderImpl reader = new CSVXLSReaderImpl();

        List<String> list = reader.getColoumnAsList("id", text);
        Assert.assertEquals(3, list.size());

        Map<String, String> map = reader.getMapFrom2Coloums("id", "ear", text);
        Assert.assertEquals("ch.sbb.releasetrain.product1:ch.sbb.releasetrain.product1.ear", map.get("product1"));

        List<List<String>> rows = reader.getAllRows(text);

        Assert.assertEquals(3, rows.size());
        Assert.assertEquals(5, rows.get(2).size());

    }

}
