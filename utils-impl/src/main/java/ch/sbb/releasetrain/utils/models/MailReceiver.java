/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.models;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * Representation of Email receiver
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.6, 2015
 */
@Data
public class MailReceiver implements Recognizable<MailReceiver> {

    private String anrede;
    private String email;
    private boolean active = true;
    private Set<String> mailinglist = new HashSet<>();

    public MailReceiver(String anrede, String email, boolean active) {
        super();
        this.anrede = anrede;
        this.email = email;
        this.active = active;
        this.mailinglist.add("default");
    }

    public int compareTo(MailReceiver o) {
        return this.getId().compareTo(o.getId());
    }


    public String getId() {
        return email;
    }
}
