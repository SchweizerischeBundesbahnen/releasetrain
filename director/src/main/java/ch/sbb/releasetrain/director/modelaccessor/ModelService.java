/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director.modelaccessor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ch.sbb.releasetrain.utils.file.FileUtil;
import ch.sbb.releasetrain.utils.http.HttpUtil;


/**
 * Service that returns Models
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
public abstract class ModelService<T extends Recognizable<?>> {

    @Autowired
    private HttpUtil http;

    @Autowired
    private FileUtil file;

    private XstreamModelAccessor<T> xstream;

    private Map<String, String> cache = new HashMap<String, String>();

    private String getModelString(String source) {

        if (cache.containsKey(source)) {
            return cache.get(source);
        }

        // 1) http or https source
        if (source.contains("http")) {
            String temp = http.getPageAsString(source);
            cache.put(source, temp);
            return temp;
        }

        // 2) file source
        if (new File(source).exists()) {
            String temp = file.readFileToString(source);
            cache.put(source, temp);
            return temp;
        }

        // 3 from resource
        InputStream in = ModelService.class.getResourceAsStream(source);
        try {
            return IOUtils.toString(in, "UTF-8");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";

    }

    public T readModelById(String id, String source) {
        String xml = this.getModelString(source);
        List<T> list = xstream.convertEntrys(xml);
        for (T server : list) {
            if (server.retreiveIdentifier().equals(id)) {
                return server;
            }
        }
        return null;
    }

    public List<T> readAllModel(String source) {
        String xml = this.getModelString(source);
        return xstream.convertEntrys(xml);
    }

    public void writeEntrysToFile(List<T> list, String file) {
        String xml = xstream.convertEntrys(list);
        this.file.writeFile(file, xml);
        // update cache
        if (cache.containsKey(file)) {
            log.info("* will update existing cache on ModelService with \"key\" " + file);
        } else {
            log.info("* will update not existing \"key\" in cache: " + file);
        }
        cache.put(file, xml);
    }

}
