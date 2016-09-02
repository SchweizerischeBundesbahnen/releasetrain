/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.yaml;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

/**
 * Yaml Util with field access
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public class YamlUtil {

    public static String marshall(Object obj){
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml.dump(obj);
    }

    public static Object unMarshall(String str){
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml.load(str);
    }

    public static Object clone(Object obj){
        return unMarshall(marshall(obj));
    }

}
