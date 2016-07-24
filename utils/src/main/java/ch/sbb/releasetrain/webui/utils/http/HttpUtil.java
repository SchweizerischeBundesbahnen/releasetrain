/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.utils.http;

import java.io.InputStream;

/**
 * Makes HTTP(s) Calls
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface HttpUtil {

    String getPageAsString(String url);

    InputStream getResourceAsStream(String url);

    String postContentToUrl(String url, String content);

}