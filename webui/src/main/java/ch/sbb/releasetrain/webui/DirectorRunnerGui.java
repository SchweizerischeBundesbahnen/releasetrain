/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui;

import ch.sbb.releasetrain.action.jenkins.JenkinsJobThread;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import ch.sbb.releasetrain.director.Director;
import ch.sbb.releasetrain.git.GITAccessor;
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

    @Autowired
    private GITAccessor git;



    private String templateJob = "user.u203244.template.git.custom";

    private String templateText;

    private String newJob = "user.u203244.releasetrain-01.git.custom";


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

    public Boolean isTemplateAvailable(){
        JenkinsActionConfig conf = (JenkinsActionConfig) pers.getNewForName("jenkinsAction");
        util.setUser(conf.getJenkinsUser());
        util.setPassword(conf.getEncPassword());
        JenkinsJobThread th = new JenkinsJobThread(this.templateJob,"Cause",conf.getJenkinsUrl(),"build",util,null);
        return th.isJobPresent();
    }

    public Boolean isJobAvailable(){
        JenkinsActionConfig conf = (JenkinsActionConfig) pers.getNewForName("jenkinsAction");
        util.setUser(conf.getJenkinsUser());
        util.setPassword(conf.getEncPassword());
        JenkinsJobThread th = new JenkinsJobThread(this.newJob,"Cause",conf.getJenkinsUrl(),"build",util,null);
        return th.isJobPresent();
    }

    public void loadTemplate(){
        JenkinsActionConfig conf = (JenkinsActionConfig) pers.getNewForName("jenkinsAction");
        util.setUser(conf.getJenkinsUser());
        util.setPassword(conf.getEncPassword());
        JenkinsJobThread th = new JenkinsJobThread(this.templateJob,"Cause",conf.getJenkinsUrl(),"build",util,null);
        this.templateText = th.readConfig();
    }

    public void createJobOnJenkins(){

        String tempText = templateText;

        StringBuilder builder = new StringBuilder();
        builder.append("ch.sbb.releasetrain:mavenmojos:0.0.27:releasetrain\n");
        builder.append("-Dconfig.url="+git.getModel().getConfigUrl()+"\n");
        builder.append("-Dconfig.branch="+git.getModel().getConfigBranch()+"\n");
        builder.append("-Dconfig.user="+git.getModel().getConfigUser()+"\n");
        builder.append("-Dconfig.password="+git.getModel().getConfigPassword()+"\n");

        String targetMarker = "${mavenmojo}";

        String mavenMarker = "(Default)";

        tempText = tempText.replace(targetMarker,builder.toString());

        tempText = tempText.replace(mavenMarker,"Apache Maven 3.3");

        JenkinsActionConfig conf = (JenkinsActionConfig) pers.getNewForName("jenkinsAction");
        util.setUser(conf.getJenkinsUser());
        util.setPassword(conf.getEncPassword());

        JenkinsJobThread th = new JenkinsJobThread(this.newJob,"Cause",conf.getJenkinsUrl(),"build",util,null);

        if(this.isJobAvailable()){
            th.writeConfig(tempText,this.newJob);
        } else {
            th.writeNewConfig(tempText,this.newJob);
        }
        th.enable(this.newJob);
    }

}
