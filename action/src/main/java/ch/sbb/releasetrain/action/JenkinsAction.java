/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.action;

import java.util.HashMap;
import java.util.Map;

import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.action.jenkins.JenkinsJobThread;
import ch.sbb.releasetrain.state.model.ActionResult;
import ch.sbb.releasetrain.state.model.ActionState;
import ch.sbb.releasetrain.utils.http.HttpUtil;

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
    private HttpUtil http;

    @Getter
    private JenkinsJobThread jenkinsThread;

    @Override
    public String getName() {
        return "jenkinsAction";
    }

    @Override
    public ActionResult doWork(ActionState state, HashMap properties) {
        Map<String, String> params = new HashMap<>(state.getConfig().getProperties());

        properties.putAll(params);

        JenkinsActionConfig conf = (JenkinsActionConfig) state.getConfig();

        jenkinsThread = new JenkinsJobThread(conf.getJenkinsJobname(), "fromReleaseTrainJenkinsAction", conf.getJenkinsUrl(), conf.getJenkinsBuildToken(), http, properties);
        jenkinsThread.startBuildJobOnJenkins(true);
        state.setResultString(jenkinsThread.getBuildColor() + ": " + jenkinsThread.getJobUrl());
        if (jenkinsThread.getBuildColor().equalsIgnoreCase("green")) {
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAILED;
    }
}
