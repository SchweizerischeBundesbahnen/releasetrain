/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.email;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import ch.sbb.releasetrain.director.modelaccessor.Recognizable;

/**
 * Represents a Email Receiver
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public class MailReceiver implements Recognizable<MailReceiver> {

    private String email;
    private Set<String> mailinglist = new HashSet<String>();

    @Override
    public int compareTo(MailReceiver o) {
        return this.retreiveIdentifier().compareTo(o.retreiveIdentifier());
    }

    @Override
    public String retreiveIdentifier() {
        return email;
    }

}
