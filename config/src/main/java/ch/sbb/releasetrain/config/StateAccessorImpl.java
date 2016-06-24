/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.config.model.ReleaseConfig;
import ch.sbb.releasetrain.config.model.email.MailReceiver;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseEvent;
import ch.sbb.releasetrain.director.modelaccessor.XstreamModelAccessor;
import ch.sbb.releasetrain.state.git.GitAccessor;

/**
 * Provides Acces to the Release Configs, stored in a storage like GIT
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Slf4j
@Component
public class StateAccessorImpl implements ConfigAccessor {

    XstreamModelAccessor<ReleaseConfig> xstream = new XstreamModelAccessor<>();
    @Value("config.branch")
    private String branch;
    @Value("config.gitrepo")
    private String repo;
    @Value("config.user")
    private String user;
    @Value("config.password")
    private String password;
    @Autowired
    private GitAccessor git;

    @PostConstruct
    private void init() {
        git.connectToRepoAndBranch(user, password, branch);
    }

    @Override
    public ReleaseConfig readConfig(String name) {
        git.readFileToString("releasetrain/" + name + ".xml");
        return null;
    }

    @Override
    public List<MailReceiver> readMailReceiver() {
        return null;
    }

    @Override
    public List<MailReceiver> readMailReveiverForMailinglist(String... mailinglists) {
        return null;
    }

    @Override
    public List<ReleaseEvent> readReleaseCalendars() {
        return null;
    }
}
