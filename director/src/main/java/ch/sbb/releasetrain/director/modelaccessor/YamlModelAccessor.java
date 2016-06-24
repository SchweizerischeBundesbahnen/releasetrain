/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.director.modelaccessor;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.yaml.snakeyaml.Yaml;

/**
 * Marshaling / unmarsahling models from / to xstream strings
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
@SuppressWarnings("unchecked")
public class YamlModelAccessor<T extends Recognizable> {

    Yaml yaml = new Yaml();

    public String convertEntrys(List<T> in) {
        return yaml.dump(in);
    }

    public List<T> convertEntrys(String in) {
        List<T> list = (List<T>) yaml.load(in);
        Collections.sort(list);
        return list;
    }

    public String convertEntry(T in) {
        return yaml.dump(in);
    }

    public T convertEntry(String in) {
        return (T) yaml.load(in);
    }

}
