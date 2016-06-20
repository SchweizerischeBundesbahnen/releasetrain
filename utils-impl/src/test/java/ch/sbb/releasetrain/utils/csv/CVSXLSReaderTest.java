/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.csv;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.utils.io.IOUtil;
import org.junit.Assert;
import org.junit.Test;


public class CVSXLSReaderTest {

    @Test
    public void testIsBuildRUNNING() throws Exception {

        InputStream in = this.getClass().getResourceAsStream("/products.csv");
        String text = IOUtil.toString(in);

        CVSXLSReaderImpl reader = new CVSXLSReaderImpl();
        reader.setText(text);

        List<String> list = reader.getListFromColoumn("id");
        Assert.assertEquals(3, list.size());

        Map<String, String> map = reader.getMapFromXLS("id", "ear");
        Assert.assertEquals("ch.sbb.releasetrain.product1:ch.sbb.releasetrain.product1.ear", map.get("product1"));

    }

}
