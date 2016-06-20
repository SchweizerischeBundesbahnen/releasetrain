/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.git;

/**
 * Writing and Reading Files from GIT to String
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface GitAccessor {

    boolean writeFile(String pathAndFile, String content);

    String readFileToString(String pathAndFile);

}