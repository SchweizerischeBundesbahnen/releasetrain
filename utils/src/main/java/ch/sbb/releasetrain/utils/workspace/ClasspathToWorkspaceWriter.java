/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.workspace;

/**
 * Util to write Files from classpath to the workspace (ex. jenkins workspace)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface ClasspathToWorkspaceWriter {
    void writeFileFromCPToWorkspace(String folder, String filename);

    void setWorkspace(String workspace);
}
