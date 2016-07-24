/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.git;

/**
 * Provide access to git repository.
 *
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public interface GitClient {

    GitRepo gitRepo(final String repo, final String branch, final String user, final String password);
}
