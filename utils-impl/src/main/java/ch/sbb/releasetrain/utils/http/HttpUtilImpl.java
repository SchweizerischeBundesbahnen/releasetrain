/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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
import org.apache.http.client.methods.HttpPut;
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


    private boolean setJenkinsAutentication = false;

    public static void main(String[] args) {

    }

    /**
     * set setSetJenkinsAutentication to true for setting the autentication on jenkins
     */
    @Override
    public String getPageAsString(String url) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpClientContext context = HttpClientContext.create();
            if (setJenkinsAutentication) {
                initAutenticationOnContect(context);
            }
            HttpResponse response = httpclient.execute(httpget, context);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return "n/a";
    }

    @Override
    public void postJobAsXMLToJenkinsWithAuth(String url, String content) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/xml");
        HttpEntity entity = null;

        HttpClientContext context = HttpClientContext.create();
        initAutenticationOnContect(context);

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
        } catch (IOException e) {
            log.error(e);
        }

    }

    /**
     * set setSetJenkinsAutentication to true for setting the autentication on jenkins
     */
    private void initAutenticationOnContect(HttpClientContext context) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(AuthScope.ANY),
                new UsernamePasswordCredentials("***", "***"));

        HttpHost targetHost = new HttpHost("ci.sbb.ch", 443, "https");

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
    }

    public boolean isSetJenkinsAutentication() {
        return setJenkinsAutentication;
    }

    @Override
    public void setSetJenkinsAutentication(boolean setJenkinsAutentication) {
        this.setJenkinsAutentication = setJenkinsAutentication;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String putBodyToURL(String url, String body) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut post = new HttpPut(url);
        // post.setHeader("Content-Type", "application/xml");
        HttpEntity entity = null;

        HttpClientContext context = HttpClientContext.create();
        initAutenticationOnContect(context);

        try {
            entity = new StringEntity(body);
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

            HttpEntity entity2 = response.getEntity();
            return EntityUtils.toString(entity2);

        } catch (IOException e) {
            log.error(e);
        }
        return "";
    }

    @Override
    public InputStream getResourceAsStream(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(get);
            return response.getEntity().getContent();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

}
