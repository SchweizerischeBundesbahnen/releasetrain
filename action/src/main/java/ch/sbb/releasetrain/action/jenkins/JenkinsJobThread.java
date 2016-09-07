/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.action.jenkins;

import ch.sbb.releasetrain.utils.http.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Wraper Thread for Jenkins Builds
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
public final class JenkinsJobThread extends Thread {

	private boolean waiting = true;
	private boolean running = false;
	private String jenkinsBuildtoken;
	private String jenkinsQueueUrl;
	private long start = Long.MAX_VALUE;
	private String apiLatestBuildURL = "";
	@Getter
	private String jobUrl = "";
	private String jobId = "";
	private String startBuildnumber = "";
	private boolean finished = false;
	private HttpUtil http;
	private String jenkinsUrl;

	/**
	 * Constructor for jenkins builds with parameters
	 */
	public JenkinsJobThread(final String job, final String cause, String jenkinsUrl, String jenkinsBuildtoken, HttpUtil http, final Map<String, String> params) {

		this.http = http;
		this.jenkinsUrl = jenkinsUrl;
		this.jenkinsBuildtoken = jenkinsBuildtoken;
		this.jenkinsQueueUrl = jenkinsUrl + "/queue/api/json";
		apiLatestBuildURL = jenkinsUrl + "/job/" + job + "/lastBuild/api/xml";
		jobUrl = jenkinsUrl + "/job/" + job + "/build?token=" + jenkinsBuildtoken;
		jobId = job;
		startBuildnumber = this.getBuildnumber();

		String par = "";
		if (params != null) {
			for (final String key : params.keySet()) {
				if (key == null || params.get(key) == null) {
					continue;
				}
				final String poormanUrlEncoded = key.replace(" ", "+") + "=" + params.get(key).replace(" ", "+");
				par = par + "&" + poormanUrlEncoded;
			}
			jobUrl = jobUrl + par;
			jobUrl = jobUrl.replace("/build?", "/buildWithParameters?");
		}

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
		final String str = callURL(this.jenkinsQueueUrl);
		return str.contains(this.jobId);
	}

	private String getBuildnumber() {
		final String str = callURL(apiLatestBuildURL);
		return org.apache.commons.lang3.StringUtils.substringBetween(str, "<number>", "</number>");
	}

	public String getLatestUserForJob() {
		String url = this.jenkinsUrl + "/job/" + this.jobId + "/lastBuild";
		String result = this.callURL(url);
		String user = org.apache.commons.lang3.StringUtils.substringBetween(result, "user <a href=\"/user/", "\">");

		if (user != null) {
			return user;
		}

		String remote = org.apache.commons.lang3.StringUtils.substringBetween(result, "Started by remote host", "</span>");
		if (remote != null) {
			return "Started by remote host " + remote;
		}

		if (result.contains("Started by timer")) {
			return "timer";
		}

		return "n/a";
	}

	@Override
	public void run() {

		// waiting in queue
		waiting = true;
		while (waiting) {
			if (isBuildInQueueInternal()) {
				log.info("------!!!>          waiting in queu...");
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
				log.info("------!!!>          is executing...");
				sleep(5);
			} else {
				running = false;
				log.info("------!!!>          finished! color: " + color);
				return;
			}
		}

		// give 10 seconds to jenkins to witch his state
		sleep(10);

		this.finished = true;
	}

	public void start() {
		throw new RuntimeException("please use startBuildJobOnJenkins(false)");
	}

	public void startBuildJobOnJenkins(final boolean blocking) {
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
	}

	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000L);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}

	public String readConfig() {
		String url = this.jenkinsUrl + "/job/" + this.jobId + "/config.xml";
		String result = this.callURL(url);
		return result;
	}

	public Boolean isJobPresent() {
		String result = "";
		try {
			String url = this.jenkinsUrl + "/job/" + this.jobId + "/config.xml";
			result = this.callURL(url);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return Boolean.FALSE;
		}
		if (result.isEmpty() || result.contains("404 Not Found")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public String writeConfig(String config, String job) {
		String url = this.jenkinsUrl + "/job/" + job + "/config.xml";
		String result = http.postContentToUrl(url, config);
		return result;
	}

	public String writeNewConfig(String config, String job) {
		String url = this.jenkinsUrl + "/createItem?name=" + job;
		String result = http.postContentToUrl(url, config);
		return result;
	}

	public String disable(String job) {
		String url = this.jenkinsUrl + "/job/"+job +"/disable";
		String result = http.postContentToUrl(url,"");
		return result;
	}

	public String enable(String job) {
		String url = this.jenkinsUrl  + "/job/"+job + "/enable";
		String result = http.postContentToUrl(url,"");
		return result;
	}

}
