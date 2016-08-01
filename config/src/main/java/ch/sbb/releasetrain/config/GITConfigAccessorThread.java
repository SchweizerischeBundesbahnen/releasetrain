/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.git.GITAccessorThread;

/**
 * Connecting config fields with the Real GITAccessorThread 
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Component
@Data
public class GITConfigAccessorThread {
	
    @Value("${config.url:https://github.com/SchweizerischeBundesbahnen/releasetrain.git}")
	private String configUrl;
    @Value("${config.branch:test26}")
	private String configBranch;
    @Value("${config.user:marthaler}")
	private String configUser;
    @Value("${config.password:}")
	private String configPassword;
    
    @Autowired
    private GITAccessorThread thread;

	public void reset(){
		thread.setUrl(configUrl);
		thread.setBranch(configBranch);
		thread.setUser(configUser);
		thread.setPassword(configPassword);
		thread.reset();
	}
	
	@Async
	public void resetAsync(){
		reset();
	}

}
