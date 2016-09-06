/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.git;

import java.io.File;

/**
 * Clone/Pull a git repo or update the remote by adding/committing/pushing
 *
 * @author u206123 (Florian Seidl)
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface GitRepo {
	/**
	 * Clone the repo if it does not exist or pull if it does
	 */
	void cloneOrPull();

	/**
	 * add all unadded files, commit to local repo and push to remote
	 */
	void addCommitPush();

	/**
	 * Return the current workspace directory
	 */
	File directory();

	/**
	 * clearing the local git Repo
	 */
	void reset();

}
