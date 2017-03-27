/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.action;

import ch.sbb.releasetrain.action.jenkins.JenkinsJobThread;
import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.EmailActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;
import ch.sbb.releasetrain.state.model.ActionResult;
import ch.sbb.releasetrain.state.model.ActionState;
import ch.sbb.releasetrain.utils.emails.SMTPUtil;
import ch.sbb.releasetrain.utils.emails.SMTPUtilImpl;
import ch.sbb.releasetrain.utils.http.HttpUtilImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Marshaling / unmarsahling models from / to xstream strings
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
@Component
public class JenkinsAction extends AbstractAction {

	@Autowired
	@Setter
	private HttpUtilImpl http;

	private boolean warnedOnError = false;

	@Autowired
	private SMTPUtilImpl sender;

	@Getter
	private JenkinsJobThread jenkinsThread;

	@Autowired
	private ConfigAccessor accessor;

	@Override
	public String getName() {
		return "jenkinsAction";
	}

	@Override
	public ActionResult doWork(ActionState state, HashMap properties) {
		Map<String, String> params = new HashMap<>(state.getConfig().getProperties());

		properties.putAll(params);
		properties.put("getJobAdminMail",((JenkinsActionConfig)state.getConfig()).getJobAdminMail());

		JenkinsActionConfig conf = (JenkinsActionConfig) state.getConfig();

		http.setPassword(conf.getEncPassword());
		http.setUser(conf.getJenkinsUser());

		jenkinsThread = new JenkinsJobThread(conf.getJenkinsJobname(), "fromReleaseTrainJenkinsAction", conf.getJenkinsUrl(), conf.getJenkinsBuildToken(), http, properties);
		jenkinsThread.startBuildJobOnJenkins(true);

		while(jenkinsThread.getBuildColor().equals("blue") || jenkinsThread.getBuildColor().equals("red") ){
			log.info("job is running or red, will stay in loop");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(jenkinsThread.getBuildColor().equals("red")&& !warnedOnError){
				log.info("job is red, will send warning mail");

				ReleaseConfig configuration = accessor.readConfig("defaultConfig");
				List<ActionConfig> actions= configuration.getActions();
				for(ActionConfig act : actions){
					if(act instanceof EmailActionConfig){
						EmailActionConfig mailConf = (EmailActionConfig) act;
						sender.setMailhost(mailConf.getSmtpServer());
						try {
							String mail = (String) properties.get("getJobAdminMail");
							sender.send("noreply@noreply.com", mail, "Releasetrain: failed jenkins build detected", properties.toString());
						} catch (Throwable e){
							log.error(e.getMessage(),e);
							warnedOnError = true;
						}
						}

				}

				warnedOnError = true;
			}

		}


		state.setResultString(jenkinsThread.getBuildColor() + ": " + jenkinsThread.getJobUrl());
		if (jenkinsThread.getBuildColor().equalsIgnoreCase("green")) {
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAILED;
	}

}
