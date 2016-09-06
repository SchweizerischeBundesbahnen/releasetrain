/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state;

import static org.junit.Assert.*;

import ch.sbb.releasetrain.config.model.releaseconfig.ActionConfig;
import ch.sbb.releasetrain.config.model.releaseconfig.JenkinsActionConfig;
import ch.sbb.releasetrain.state.model.ReleaseState;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public class StateFileWriterReaderTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void testWriteState() throws IOException {
		ReleaseState releaseState = new ReleaseState("test", Collections.<ActionConfig>emptyList());
		new StateFileWriter(temporaryFolder.getRoot()).write(releaseState);
		assertTrue(new File(temporaryFolder.getRoot(), "test-state.yml").exists());
	}

	@Test
	public void testWriteReadState() throws IOException {
		ReleaseState releaseState = new ReleaseState("test", Collections.<ActionConfig>emptyList());
		new StateFileWriter(temporaryFolder.getRoot()).write(releaseState);
		assertNotNull(new StateFileReader(temporaryFolder.getRoot()).read("test"));
	}

	@Test
	public void testWriteReadStateEqual() throws IOException {
		ReleaseState releaseState = new ReleaseState("test", Collections.<ActionConfig>emptyList());
		new StateFileWriter(temporaryFolder.getRoot()).write(releaseState);
		ReleaseState releaseStateReread = new StateFileReader(temporaryFolder.getRoot()).read("test");
		assertEquals(releaseState, releaseStateReread);
	}

	@Test
	public void testWriteReadStateWithActionStatesEqual() throws IOException {
		ReleaseState releaseState = new ReleaseState("test", Arrays.asList(createActionConfig("bli", 0), createActionConfig("bla", 3), createActionConfig("blo", 1)));

		new StateFileWriter(temporaryFolder.getRoot()).write(releaseState);
		ReleaseState releaseStateReread = new StateFileReader(temporaryFolder.getRoot()).read("test");
		assertEquals(releaseState, releaseStateReread);
	}

	private ActionConfig createActionConfig(final String name, final int nrProperties) {
		final ActionConfig actionConfig = new JenkinsActionConfig();
		actionConfig.setOffseMinutes(42);
		final Map<String, String> properties = new HashMap<>();
		for (int i = 0; i < nrProperties; i++) {
			properties.put(String.format("property%d", i), String.format("value%d", i));
		}
		actionConfig.setProperties(properties);
		return actionConfig;
	}

}
