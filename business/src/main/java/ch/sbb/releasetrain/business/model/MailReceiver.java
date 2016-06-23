/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.business.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import ch.sbb.releasetrain.business.modelaccessor.Recognizable;

/**
 * Represents a Email Receiver
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Data
public class MailReceiver implements Recognizable<MailReceiver> {

    private String anrede;
    private String email;
    private boolean active = true;
    private Set<String> mailinglist = new HashSet<String>();

    @Override
    public int compareTo(MailReceiver o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String getId() {
        return email;
    }

}
