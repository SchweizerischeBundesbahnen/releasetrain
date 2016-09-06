/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.git;

import java.io.File;

import org.springframework.stereotype.Component;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@Component
public class GitClientImpl implements GitClient {

	private final File tempDir;

	public GitClientImpl() {
		tempDir = new File(System.getProperty("java.io.tmpdir"));
	}

	public GitClientImpl(File tempDir) {
		this.tempDir = tempDir;
	}

	static String filenameFromUrl(final String url, String branch) {
		return (url + "_" + branch).trim().replaceAll("[\\W]", "_");
	}

	@Override
	public GitRepo gitRepo(String url, String branch, String user, String password) {
		return new GitRepoImpl(url, branch, user, password, new File(tempDir, filenameFromUrl(url, branch)));
	}
}
