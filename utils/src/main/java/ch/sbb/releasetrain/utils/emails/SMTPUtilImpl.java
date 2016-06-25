/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.emails;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Email Util
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Component
@Slf4j
public class SMTPUtilImpl implements SMTPUtil {

    @Value("${smtp.port:25}")
    private int mailport;
    @Value("${smtp.host: }")
    private String mailhost = "smtpout.sbb.ch";

    @Override
    public void send(String absender, String empfaenger, String betreff, String text) {
        try {
            final Email email = new SimpleEmail();
            email.setHostName(mailhost);
            email.setSmtpPort(mailport);
            email.setFrom(absender);
            email.setSubject(betreff);
            email.setMsg(text);
            email.addTo(empfaenger);
            email.send();
            log.info("mail sent to: " + empfaenger);
        } catch (final EmailException e) {
            log.error(e.getMessage(), e);
        }
    }


}
