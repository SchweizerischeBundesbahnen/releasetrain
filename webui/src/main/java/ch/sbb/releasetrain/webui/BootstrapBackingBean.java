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

import ch.sbb.releasetrain.git.GitClientImpl;
import ch.sbb.releasetrain.git.GitRepo;
import ch.sbb.releasetrain.state.GITStateAccessorThread;

/**
 * Backing Bean to grant access to the 2 required git branches for configuration and storage
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Component
public class BootstrapBackingBean {

    private Boolean configOk = false;
	
    @Autowired
    private GITStateAccessorThread stateAccessor;

    @Autowired
    private GITStateAccessorThread configAccessor;
   
   
    // state accessors
    public String getStateUrl() {
		return stateAccessor.getConfigUrl();
	}

	public void setStateUrl(String configUrl) {
		stateAccessor.setConfigUrl(configUrl);
	}

	public String getStateBranch() {
		return stateAccessor.getConfigBranch();
	}

	public void setStateBranch(String configBranch) {
		stateAccessor.setConfigBranch(configBranch);
	}

	public String getStateUser() {
		return stateAccessor.getConfigUser();
	}

	public void setStateUser(String configUser) {
		stateAccessor.setConfigUser(configUser);
	}

	public String getStatePassword() {
		return stateAccessor.getConfigPassword();
	}

	public void setStatePassword(String configPassword) {
		stateAccessor.setConfigPassword(configPassword);
	}
    
    
   // config accessor
    public String getConfigUrl() {
		return configAccessor.getConfigUrl();
	}

	public void setConfigUrl(String configUrl) {
		configAccessor.setConfigUrl(configUrl);
	}

	public String getConfigBranch() {
		return configAccessor.getConfigBranch();
	}

	public void setConfigBranch(String configBranch) {
		configAccessor.setConfigBranch(configBranch);
	}

	public String getConfigUser() {
		return configAccessor.getConfigUser();
	}

	public void setConfigUser(String configUser) {
		configAccessor.setConfigUser(configUser);
	}

	public String getConfigPassword() {
		return configAccessor.getConfigPassword();
	}

	public void setConfigPassword(String configPassword) {
		configAccessor.setConfigPassword(configPassword);
	}

	
	// others
	public Boolean getConfigOk() {
		return configOk;
	}

	public void setConfigOk(Boolean configOk) {
		this.configOk = configOk;
	}


}
