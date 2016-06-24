/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director.modelaccessor;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;

/**
 * Marshaling / unmarsahling models from / to xstream strings
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
public class XstreamModelAccessor<T extends Recognizable> {

    private XStream xstream;

    public String convertEntrys(Object in) {
        return xstream.toXML(in);
    }

    public List<T> convertEntrys(String in) {
        List<T> list = (List<T>) xstream.fromXML(in, "UTF-8");
        Collections.sort(list);
        return list;
    }

    public void saveEntrys(List<T> obj, String file) {
        String xml = convertEntrys(obj);
        try {
            FileUtils.writeStringToFile(new File(file), xml, "UTF-8");
        } catch (IOException e) {
            log.error("saving log model", e);
        }
    }
}
