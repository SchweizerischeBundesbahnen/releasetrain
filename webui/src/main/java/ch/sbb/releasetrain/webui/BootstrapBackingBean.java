/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui;

import java.io.File;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.webui.git.GitClientImpl;
import ch.sbb.releasetrain.webui.git.GitRepo;

/**
 * Backing Bean to grant access to the 2 required git branches for configuration and storage
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Component
@Slf4j
@Data
public class BootstrapBackingBean {

    @Autowired
    private GitClientImpl gitClient;

    @Value("${config.url:}")
    private String urlConfig = "";
    @Value("${config.branch:}")
    private String branchConfig = "";
    @Value("${config.user:}")
    private String userConfig = "";
    @Value("${config.password:}")
    private String passwordConfig = "";

    private GitRepo repoConfig;

    private GitRepo repoStore;

    private Boolean configOk = false;

    public String checkConnectionConfig() {
        repoConfig = gitClient.gitRepo(urlConfig, branchConfig, userConfig, passwordConfig);
        repoConfig.reset();
        try {
            repoConfig.cloneOrPull();
            File dir = repoConfig.directory();
            FileUtils.writeStringToFile(new File(dir, "test.txt"), new Date().toString());
            repoConfig.addCommitPush();
            FileUtils.forceDelete(new File(dir, "test.txt"));
            repoConfig.addCommitPush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            configOk = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage()));
        }
        configOk = true;
        return "app.htm";
    }

    public String checkConnectionStorage() {
        GitRepo repo = gitClient.gitRepo(urlConfig, branchConfig, userConfig, passwordConfig);
        repo.reset();
        try {
            repo.cloneOrPull();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            configOk = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage()));
        }
        configOk = true;
        return "app.htm";
    }

}
