/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.git.GITAccessorThread;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides Acces to the Release Configs, stored in a storage like GIT
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Slf4j
@Component
@Data
public class GITConfigAccessorThread extends GITAccessorThread {
	
    @Value("${state.url:https://github.com/SchweizerischeBundesbahnen/releasetrain.git}")
	private String configUrl;
    @Value("${state.branch:test26}")
	private String configBranch;
    @Value("${state.user:marthaler}")
	private String configUser;
    @Value("${state.password:}")
	private String configPassword;
	
	@PostConstruct
	protected void init() {
		super.url = configUrl;
		super.branch = configBranch;
		super.user = configUser;
		super.password = configPassword;
		super.asyncInit();
	}

}
