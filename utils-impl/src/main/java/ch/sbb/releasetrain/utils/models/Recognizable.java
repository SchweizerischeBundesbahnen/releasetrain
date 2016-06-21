/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.models;

/**
 * provides a getter for an ID
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @param <T>
 * @since 2.0.7, 2015
 */
public interface Recognizable<T> extends Comparable<T> {

    String getId();

}
