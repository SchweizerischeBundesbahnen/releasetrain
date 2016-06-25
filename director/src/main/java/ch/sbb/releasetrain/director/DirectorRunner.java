/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director;

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
@Profile("springboot")
@Slf4j
public class DirectorRunner {

    @Autowired
    private Director director;

    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        log.debug("wake up the director");
        director.direct();
    }

}
