/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.exception;

/**
 * Releasetrain Exception
 * 
 * @author u203244 (Daniel Marthaler)
 * @since 2.0.6, 2015
 */
public class ReleasetrainException extends Exception {

    private static final long serialVersionUID = 1L;

    public ReleasetrainException(String message) {
        super(message);
    }

    public ReleasetrainException(String message, Throwable e) {
        super(message, e);
    }

}
