/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.inject.Singleton;

/**
 * Get String from http(s) url
 * 
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.6, 2015
 */
@Singleton
@CommonsLog
public class HttpUtilImpl implements HttpUtil {

    @Setter
    private String user = "";

    @Setter
    private String password = "";

    /**
     * set user and password for basic authentication
     */
    @Override
    public String getPageAsString(String url) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);

            HttpClientContext context = initAuthIfNeeded(url);

            HttpResponse response = httpclient.execute(httpget, context);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return "n/a";
    }

    /**
     * set user and password for basic authentication
     */
    public String postContentToUrl(String url, String content) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/xml");
        HttpEntity entity = null;

        HttpClientContext context = initAuthIfNeeded(url);

        try {
            entity = new StringEntity(content);
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }

        post.setEntity(entity);
        HttpResponse response;
        try {
            response = httpclient.execute(post, context);
            if (response.getStatusLine().getStatusCode() != 200) {
                log.error("response code not ok: " + response.getStatusLine().getStatusCode());
            }
            entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            log.error(e);
        }
        return "";
    }

    /**
     * authenticates the context if user and password are set
     */
    private HttpClientContext initAuthIfNeeded(String url) {

        HttpClientContext context = HttpClientContext.create();
        if (this.user.isEmpty() || this.password.isEmpty()) {
            log.info("http connection without autentication, because no user / password ist known to HttpUtilImpl ...");
            return context;
        }

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(AuthScope.ANY),
                new UsernamePasswordCredentials(user, password));
        URL url4Host;
        try {
            url4Host = new URL(url);
        } catch (MalformedURLException e) {
            log.fatal(e.getMessage(), e);
            return context;
        }

        HttpHost targetHost = new HttpHost(url4Host.getHost(), url4Host.getPort(), url4Host.getProtocol());
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
        return context;
    }

    @Override
    public InputStream getResourceAsStream(String url) {
        HttpClientContext context = initAuthIfNeeded(url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(get, context);
            return response.getEntity().getContent();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

}
