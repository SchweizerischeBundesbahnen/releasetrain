/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class ConfigAccessorImplTest {

	@Configuration
	@ComponentScan({"ch.sbb.releasetrain.config","ch.sbb.releasetrain.git","ch.sbb.releasetrain.utils"})
    static class ContextConfiguration {
		@Bean
		public static PropertySourcesPlaceholderConfigurer propertiesResolver() {
		    return new PropertySourcesPlaceholderConfigurer();
		}
    }
	
	@Autowired
	private GITConfigAccessorThread th;

	
    @Test
    public void testReadConfigFromGitRepo() throws Exception {

    
        
        
    }

}
