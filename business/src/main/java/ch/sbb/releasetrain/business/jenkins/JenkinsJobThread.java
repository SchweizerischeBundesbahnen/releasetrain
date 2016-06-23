/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.business.jenkins;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import ch.sbb.releasetrain.business.guice.GuiceInjectorWrapper;
import ch.sbb.releasetrain.utils.config.GlobalConfig;
import ch.sbb.releasetrain.utils.http.HttpUtil;

import com.google.inject.Inject;


/**
 * Wraper Thread for Jenkins Builds
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
public class JenkinsJobThread extends Thread {

    boolean waiting = true;
    boolean running = false;
    private String params = "";
    private long start = Long.MAX_VALUE;
    private String apiLatestBuildURL = "";
    private String jobUrl = "";
    @Inject
    private GlobalConfig config;
    @Inject
    private HttpUtil http;
    @Getter
    private String jobId = "";
    private String startBuildnumber = "";
    private boolean finished = false;


    /**
     * Constructor for jobs without parameters
     */
    public JenkinsJobThread(String job) {
        GuiceInjectorWrapper.injectMembers(this);
        apiLatestBuildURL = config.get("jenkins.url") + "/job/" + job + "/lastBuild/api/xml";
        jobUrl = config.get("jenkins.url") + "/job/" + job + "/build?token=" + config.get("jenkins.buildtoken");
        jobId = job;
        startBuildnumber = this.getBuildnumber();
    }

    /**
     * Constructor for jenkins builds with parameters
     */
    public JenkinsJobThread(final String job, final String cause, final String... parameters) {
        this(job);
        for (final String param : parameters) {
            final String poormanUrlEncoded = param.replace(" ", "+");
            params = params + "&" + poormanUrlEncoded;
        }
        jobUrl = jobUrl + params;
        jobUrl = jobUrl.replace("build?", "buildWithParameters?");

        if (cause != null && !cause.isEmpty()) {
            try {
                jobUrl = jobUrl + "&cause=" + URLEncoder.encode(cause, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
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

        if (isBuildIsYellow(xmlApiString)) {
            return "yellow";
        }

        if (isBuildRed(xmlApiString)) {
            return "red";
        }

        log.warn("!!! RED -> : " + apiLatestBuildURL);
        log.warn("!!! RED -> : " + xmlApiString);

        return "red";
    }

    private boolean isBuildIsGreen(String str) {
        if (str.contains("<result>SUCCESS</result>")) {
            return true;
        }
        return false;
    }

    private boolean isBuildBlueInternal(String str) {
        if ((start + 1000) > System.currentTimeMillis()) {
            return true;
        }
        if (str.contains("<building>true</building>")) {
            return true;
        }
        return false;
    }

    private boolean isBuildRed(String str) {
        if ((start + 1000) > System.currentTimeMillis()) {
            return true;
        }
        if (str.contains("<result>FAILURE</result>")) {
            return true;
        }
        return false;
    }


    protected boolean isBuildIsYellow(String str) {
        if ((start + 1000) > System.currentTimeMillis()) {
            return false;
        }
        if (str.contains("<result>UNSTABLE</result>")) {
            return true;
        }
        return false;
    }

    protected boolean isBuildInQueueInternal() {

        if ((start + 1000) > System.currentTimeMillis()) {
            return true;
        }
        final String str = callURL(config.get("jenkins.queue.url"));
        if (str.contains(this.jobId)) {
            return true;
        }
        return false;
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
