/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.utils.http;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import ch.sbb.releasetrain.utils.http.HttpUtilImpl;


public class HttpUtilTest {

    @Test
    public void testGetGoogle() throws Exception {
        HttpUtilImpl http = new HttpUtilImpl();
        String google = http.getPageAsString("https://www.google.ch");
        Assert.assertTrue(google.contains("google.ch"));
    }

    @Test
    public void testGetBasic() throws Exception {
        HttpUtilImpl http = new HttpUtilImpl();
        http.setUser("ja");
        http.setPassword("ja");
        String response = http.getPageAsString("https://httpbin.org/basic-auth/ja/ja");
        Assert.assertTrue(response.contains("authenticated\": true"));
    }

    @Test
    public void testPostBasic() throws Exception {
        HttpUtilImpl http = new HttpUtilImpl();
        String content = "sdcnsdcouansdcaosdch";
        String response = http.postContentToUrl("https://httpbin.org/post", content);
        Assert.assertTrue(response.contains(content));
    }

    @Test
    public void testGetStream() throws Exception {
        HttpUtilImpl http = new HttpUtilImpl();
        InputStream google = http.getResourceAsStream("https://www.google.ch");
        String content = IOUtils.toString(google);
        Assert.assertTrue(content.contains("google.ch"));
    }

}
