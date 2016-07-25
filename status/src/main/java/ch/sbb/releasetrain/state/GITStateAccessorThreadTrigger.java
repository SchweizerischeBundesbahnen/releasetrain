/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state;

import java.io.File;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.git.GITAccessorThread;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * calling @Async rest Method on  GITStateAccessorThread
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */

@Component
public class GITStateAccessorThreadTrigger {

	@Autowired
	private GITStateAccessorThread thread;
	
	@PostConstruct
	public void init(){
		thread.resetAsync();
	}
	
    
}
