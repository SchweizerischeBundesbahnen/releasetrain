/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Writing and Reading Files
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.10, 2015
 */
@Slf4j
public class FileUtilImpl implements FileUtil {

    @Override
    public boolean writeFile(String path, String content) {
        try {
            FileUtils.writeStringToFile(new File(path), content);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public String readFileToString(String pathAndFile) {
        try {
            return FileUtils.readFileToString(new File(pathAndFile));
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
        return "";
    }

    @Override
    public String readFileToStringFromClasspath(String pathAndFile) {
        pathAndFile = pathAndFile.replace("///", "/");
        String ret = "";

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = getClass().getResourceAsStream(pathAndFile);
        try {
            ret = IOUtils.toString(in);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        if (ret == null || ret.isEmpty()) {
            try {
                ret = FileUtils.readFileToString(new File(classLoader.getResource(pathAndFile).getFile()));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        if (ret == null || ret.isEmpty()) {
            InputStream in2 = this.getClass().getResourceAsStream(pathAndFile);
            try {
                ret = IOUtils.toString(in2);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        return ret;
    }
}

