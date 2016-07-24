/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.utils.emails;

/**
 * Send Emails
 * 
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface SMTPUtil {

    void send(final String sender, final String receiver, final String subject, final String body);

}