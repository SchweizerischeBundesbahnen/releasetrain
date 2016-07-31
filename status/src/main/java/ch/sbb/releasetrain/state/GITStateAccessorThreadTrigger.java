/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
