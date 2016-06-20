/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.file;

/**
 * Writing and Reading Files
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface FileUtil {

    boolean writeFile(String path, String content);

    String readFileToString(String pathAndFile);

    String readFileToStringFromClasspath(String pathAndFile);

}