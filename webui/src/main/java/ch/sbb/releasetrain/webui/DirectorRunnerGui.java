/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui;

import ch.sbb.releasetrain.action.jenkins.JenkinsJobThread;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import ch.sbb.releasetrain.director.Director;
import ch.sbb.releasetrain.utils.http.HttpUtil;
import ch.sbb.releasetrain.utils.http.HttpUtilImpl;
import ch.sbb.releasetrain.webui.backingbeans.DefaultPersistence;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Mighty Director - dictator
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Component
@Slf4j
@Data
public class DirectorRunnerGui {

    private Boolean on = Boolean.FALSE;

    @Autowired
    private DefaultPersistence pers;

    @Autowired
    private HttpUtilImpl util;

    @Autowired
    private Director director;

    public String getButton(){

        if(!on){
            return "Start local";
        } else {
            return "Stop local";
        }
    }

    public void go(){
      director.setShutdown(false);
      on = !on;
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void run() {
        if(!on){
            log.info("not waking up the director ...");
            return;
        }
        log.info("wake up the director...");
        director.direct();
        log.info("director is going to sleep a while ...");
    }


    public void createJobOnJenkins(){

        JenkinsActionConfig conf = (JenkinsActionConfig) pers.getNewForName("jenkinsAction");
        util.setUser(conf.getJenkinsUser());
        util.setPassword(conf.getEncPassword());

        JenkinsJobThread th = new JenkinsJobThread("user.u203244.template2.git.nightly","Cause",conf.getJenkinsUrl(),"build",util,null);
        JenkinsJobThread th2 = new JenkinsJobThread("user.u203244.template3.git.nightly","Cause",conf.getJenkinsUrl(),"build",util,null);
        String config = th.readConfig();
        log.info(config);
        th2.writeNewConfig(config);

    }





}
