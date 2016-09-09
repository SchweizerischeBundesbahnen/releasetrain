/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.git;

import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Base Thread to initialize a GIT Connection read / write
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Slf4j
@Component
public class GITPusherThread {

	@Autowired
	private GITAccessor th;

	@Scheduled(fixedRate = 10 * 1000)
	public void commit() {
		th.getRepo().addCommitPush();
	}

	@PreDestroy
	private void shutdown() {
		commit();
	}

}
