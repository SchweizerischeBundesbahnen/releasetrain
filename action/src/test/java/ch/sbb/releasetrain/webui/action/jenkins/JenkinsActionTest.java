/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.action.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.sbb.releasetrain.webui.action.JenkinsAction;
import ch.sbb.releasetrain.webui.config.model.email.MailReceiver;
import ch.sbb.releasetrain.webui.config.releasecalendar.ConfigAccessorImpl;
import ch.sbb.releasetrain.webui.state.model.ActionResult;
import ch.sbb.releasetrain.webui.state.model.ActionState;
import ch.sbb.releasetrain.webui.utils.http.HttpUtilImpl;
import ch.sbb.releasetrain.webui.utils.yaml.YamlModelAccessor;

/**
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 **/
@RunWith(MockitoJUnitRunner.class)
public class JenkinsActionTest {

    @Mock
    private ConfigAccessorImpl config;

    @Mock
    private MailReceiver mailRec;

    private ActionState jenkinsState;

    private JenkinsAction action;

    @org.junit.Before
    public void setUp() throws Exception {

        String in = IOUtils.toString(getClass().getResourceAsStream("/test-jenkins-action-state.yml"));
        YamlModelAccessor<ActionState> yaml = new YamlModelAccessor<>();
        jenkinsState = yaml.convertEntry(in);
        action = new JenkinsAction();

    }

    @org.junit.Test
    public void testGetName() throws Exception {
        assertEquals("jenkinsAction", action.getName());
    }

    @org.junit.Test
    public void testDoWork() throws Exception {

        HttpUtilImpl http = new HttpUtilImpl();
        http.setUser("releasetrain");
        http.setPassword("releasetrain11");

        action.setJenkinsBuildtoken("build");
        String jenkins = "http://87.230.15.247:8080/";
        action.setJenkinsUrl(jenkins);
        action.setHttp(http);


        ActionResult result = action.doWork(jenkinsState, "releaseVersion", "snapshotVersion", "maintenanceVersion");
        assertNotNull(result);
        assertNotNull(result == ActionResult.SUCCESS);

        String user = action.getJenkinsThread().getLatestUserForJob();
        assertTrue(user.contains("Started by remote host"));
    }
}