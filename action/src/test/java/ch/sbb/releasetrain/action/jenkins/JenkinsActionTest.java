/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.action.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.sbb.releasetrain.action.JenkinsAction;
import ch.sbb.releasetrain.config.model.email.MailReceiver;
import ch.sbb.releasetrain.config.releasecalendar.ConfigAccessorImpl;
import ch.sbb.releasetrain.state.model.ActionResult;
import ch.sbb.releasetrain.state.model.ActionState;
import ch.sbb.releasetrain.utils.http.HttpUtilImpl;
import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;

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

        String in = IOUtils.toString(getClass().getResourceAsStream("/test-jenkins-action-state.yaml"));
        YamlModelAccessor<ActionState> yaml = new YamlModelAccessor<>();
        jenkinsState = yaml.convertEntry(in);
        action = new JenkinsAction();

        List<MailReceiver> receiverList = new ArrayList<>();

        when(mailRec.getEmail()).thenReturn("bla@bla.bl");
        receiverList.add(mailRec);
        when(config.readMailReveiverForMailinglist(anyString())).thenReturn(receiverList);

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
        action.setQueueURL(jenkins + "/queue/api/json");
        action.setHttp(http);


        ActionResult result = action.doWork(jenkinsState, "releaseVersion", "snapshotVersion", "maintenanceVersion");
        assertNotNull(result);
        assertNotNull(result == ActionResult.SUCCESS);

        String user = action.getJenkinsThread().getLatestUserForJob();
        assertTrue(user.contains("Startet by remote host"));
    }
}