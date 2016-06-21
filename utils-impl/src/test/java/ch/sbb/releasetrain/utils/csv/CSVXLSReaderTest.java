/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.csv;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.utils.io.IOUtil;
import org.junit.Assert;
import org.junit.Test;


public class CSVXLSReaderTest {

    @Test
    public void testIsBuildRUNNING() throws Exception {

        InputStream in = this.getClass().getResourceAsStream("/products.csv");
        String text = IOUtil.toString(in);

        CSVXLSReaderImpl reader = new CSVXLSReaderImpl();
        reader.setText(text);

        List<String> list = reader.getListFromColoumn("id");
        Assert.assertEquals(3, list.size());

        Map<String, String> map = reader.getMapFromXLS("id", "ear");
        Assert.assertEquals("ch.sbb.releasetrain.product1:ch.sbb.releasetrain.product1.ear", map.get("product1"));

    }

}
