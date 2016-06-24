/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

/**
 * Provides Acces to the persisted (ex. in Git Repo) states acording to a running
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
public interface StateAccessor {

    Object readState(String identifier);

    void writeState(String identifier, Object state);

}

