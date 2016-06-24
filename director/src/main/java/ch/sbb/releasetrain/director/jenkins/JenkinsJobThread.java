/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director.jenkins;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import ch.sbb.releasetrain.utils.http.HttpUtil;

/**
 * Wraper Thread for Jenkins Builds
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
public final class JenkinsJobThread extends Thread {

    // private final GlobalConfig config;
    boolean waiting = true;
    boolean running = false;
    private String params = "";
    private long start = Long.MAX_VALUE;
    private String apiLatestBuildURL = "";
    private String jobUrl = "";
    @Getter
    private String jobId = "";
    private String startBuildnumber = "";
    private boolean finished = false;
    private HttpUtil http;
    private Map<String, String> config;

    /**
     * Constructor for jobs without parameters
     */
    public JenkinsJobThread(String job, Map<String, String> config, HttpUtil http) {
        this.http = http;
        this.config = config;
        apiLatestBuildURL = config.get("jenkins.url") + "/job/" + job + "/lastBuild/api/xml";
        jobUrl = config.get("jenkins.url") + "/job/" + job + "/build?token=" + config.get("jenkins.buildtoken");
        jobId = job;
        startBuildnumber = this.getBuildnumber();
    }

    /**
     * Constructor for jenkins builds with parameters
     */
    public JenkinsJobThread(final String job, final String cause, Map<String, String> config, HttpUtil http, final String... parameters) {
        this(job, config, http);
        for (final String param : parameters) {
            final String poormanUrlEncoded = param.replace(" ", "+");
            params = params + "&" + poormanUrlEncoded;
        }
        jobUrl = jobUrl + params;
        jobUrl = jobUrl.replace("/build?", "/buildWithParameters?");

        if (cause != null && !cause.isEmpty()) {
            try {
                jobUrl = jobUrl + "&cause=" + URLEncoder.encode(cause, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public String getBuildColor() {

        final String xmlApiString = callURL(apiLatestBuildURL);

        if (this.finished && this.startBuildnumber.equals(this.getBuildnumber())) {
            log.warn("danger, build was running, but buildnumber is the same as in the beginning: " + this.startBuildnumber + " - " + getBuildnumber());
            return "red";
        }

        if (isBuildIsGreen(xmlApiString)) {
            return "green";
        }

        if (isBuildBlueInternal(xmlApiString)) {
            return "blue";
        }

        if (isBuildYellow(xmlApiString)) {
            return "yellow";
        }

        if (isBuildRed(xmlApiString)) {
            return "red";
        }

        log.warn("!!! RED -> : " + apiLatestBuildURL);
        log.warn("!!! RED -> : " + xmlApiString);

        return "red";
    }

    private String callURL(final String urlin) {
        if (urlin.contains("badge/icon") || urlin.contains("/queue/api/json")) {
            if (log.isTraceEnabled()) {
                log.trace("calling state url: " + urlin);
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("calling url: " + urlin);
            }
            if (urlin.contains("/buildWithParameters")) {
                log.info("GO TO JOB:          " + StringUtils.substringBefore(urlin, "/buildWithParameters"));
                log.info("------!!!>          " + urlin);
            }
        }
        return http.getPageAsString(urlin);
    }

    private boolean isBuildIsGreen(String str) {
        return str.contains("<result>SUCCESS</result>");
    }

    private boolean isBuildBlueInternal(String str) {
        return (start + 1000) > System.currentTimeMillis() || str.contains("<building>true</building>");
    }

    private boolean isBuildRed(String str) {
        return (start + 1000) > System.currentTimeMillis() || str.contains("<result>FAILURE</result>");
    }

    private boolean isBuildYellow(String str) {
        return (start + 1000) <= System.currentTimeMillis() && str.contains("<result>UNSTABLE</result>");
    }

    private boolean isBuildInQueueInternal() {

        if ((start + 1000) > System.currentTimeMillis()) {
            return true;
        }
        final String str = callURL(config.get("jenkins.queue.url"));
        return str.contains(this.jobId);
    }

    public boolean isBuildInQueue() {
        return waiting;
    }

    private String getBuildnumber() {
        final String str = callURL(apiLatestBuildURL);
        return org.apache.commons.lang3.StringUtils.substringBetween(str, "<number>", "</number>");
    }

    public String getLatestUserForJob() {
        String url = config.get("jenkins.url") + "/job/" + this.jobId + "/lastBuild";
        String result = this.callURL(url);
        String user = org.apache.commons.lang3.StringUtils.substringBetween(result, "user <a href=\"/user/", "\">");

        if (user == null) {
            if (result.contains("Started by timer")) {
                return "timer";
            }

            String url2 = config.get("jenkins.url") + "/job/" + this.jobId + "/lastBuild/changes";
            String result2 = this.callURL(url2);
            String user2 = org.apache.commons.lang3.StringUtils.substringBetween(result2, "by <a href=\"/user/", "/\">");

            return user2;
        }
        return user;
    }

    @Override
    public void run() {

        // waiting in queue
        waiting = true;
        while (waiting) {
            if (isBuildInQueueInternal()) {
                log.info("job: waiting in queu...");
                sleep(5);
            } else {
                waiting = false;
            }
        }

        // give time to jenkins to set build in running state
        sleep(5);

        // waiting for job to finish
        running = true;
        while (running) {
            String color = getBuildColor();
            if (color.equals("blue")) {
                log.info("job: is executing...");
                sleep(5);
            } else {
                running = false;
                log.info("job: finished! color: " + color);
            }
        }

        // give 10 seconds to jenkins to witch his state
        sleep(10);

        this.finished = true;
    }

    public void start() {
        throw new RuntimeException("please use startBuildJobOnJenkins(false)");
    }

    public Thread startBuildJobOnJenkins(final boolean blocking) {
        callURL(jobUrl);
        start = System.currentTimeMillis();
        super.start();
        running = true;
        try {
            if (blocking) {
                this.join();
            }
        } catch (final InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return this;
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

}
