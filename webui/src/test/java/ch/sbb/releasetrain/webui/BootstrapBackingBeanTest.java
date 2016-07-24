/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui;

import java.io.IOException;

import org.junit.Test;

import ch.sbb.releasetrain.git.GitClientImpl;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public class BootstrapBackingBeanTest {

    @Test
    public void testWriteState() throws IOException {
        BootstrapBackingBean bean = new BootstrapBackingBean();
        //bean.setGitClient(new GitClientImpl());

        //bean.setBranchConfig("test28");
        
        //bean.setUrlConfig("https://github.com/SchweizerischeBundesbahnen/releasetrain.git");
        //bean.checkConnectionConfig();

    }


}
