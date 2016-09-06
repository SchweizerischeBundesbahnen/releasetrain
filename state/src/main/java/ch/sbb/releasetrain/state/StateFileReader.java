/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state;

import ch.sbb.releasetrain.state.model.ReleaseState;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@Slf4j
public class StateFileReader extends AbstractStateFileAccess {

	public StateFileReader(File dir) {
		super(dir);
	}

	public ReleaseState read(String releaseIdentifier) {
		try {
			File file = file(releaseIdentifier);

			if (!file.exists()) {
				log.debug("File %s not existing", file);
				return null;
			}

			return converter.convertEntry(FileUtils.readFileToString(file));
		} catch (IOException e) {
			throw new RuntimeException(String.format("Error reading state from file %s", file(releaseIdentifier)), e);
		}
	}
}
