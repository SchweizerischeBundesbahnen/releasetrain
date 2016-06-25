/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
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
