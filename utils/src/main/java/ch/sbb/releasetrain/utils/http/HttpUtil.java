/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.http;

import java.io.InputStream;

/**
 *
 * Makes HTTP(s) Calls
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface HttpUtil {

    String getPageAsString(String url);

    InputStream getResourceAsStream(String url);

    void postJobAsXMLToJenkinsWithAuth(String url, String content);

    /**
     * use this to enable autentication incl user and password on http calls
     */
    void setSetJenkinsAutentication(boolean setBasicAutentication);

    /**
     * use this to put a body t a url
     **/
    String putBodyToURL(String url, String body);

}