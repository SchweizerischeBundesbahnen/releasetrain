/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.state.git;

/**
 * Wrapps JGit GitAPIException or IO Exception or a GitRepo specific exception.
 */
public class GitException extends RuntimeException {

    public GitException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitException(String message) {
        super(message);
    }
}
