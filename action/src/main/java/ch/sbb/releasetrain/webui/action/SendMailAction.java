/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.webui.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.webui.config.model.email.MailReceiver;
import ch.sbb.releasetrain.webui.config.releasecalendar.ConfigAccessor;
import ch.sbb.releasetrain.webui.state.model.ActionResult;
import ch.sbb.releasetrain.webui.state.model.ActionState;
import ch.sbb.releasetrain.webui.utils.emails.SMTPUtil;

/**
 * Sending Mails to Mailinglists (ex: send pre release info to the team)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
@Component
public class SendMailAction extends AbstractAction {

    @Autowired
    @Setter
    private SMTPUtil smtpUtil;

    @Autowired
    @Setter
    private ConfigAccessor config;

    @Override
    public String getName() {
        return "emailAction";
    }

    @Override
    public ActionResult doWork(ActionState state, String releaseVersion, String snapshotVersion, String maintenanceVersion) {

        Map<String, String> params = new HashMap<>(state.getConfig().getProperties());

        for (String key : params.keySet()) {
            String value = state.getConfig().getProperties().get(key);
            value = replaceVersions(value, "${releaseVersion}", releaseVersion);
            value = replaceVersions(value, "${snapshotVersion}", snapshotVersion);
            value = replaceVersions(value, "${maintenanceVersion}", maintenanceVersion);
            params.put(key, value);
        }

        String subject = state.getConfig().getProperties().get("subject");
        String body = state.getConfig().getProperties().get("body");
        String sender = state.getConfig().getProperties().get("sender");
        String mailinglist = state.getConfig().getProperties().get("mailinglist");

        List<MailReceiver> receiver = config.readMailReveiverForMailinglist(mailinglist);

        for (MailReceiver rec : receiver) {
            smtpUtil.send(sender, rec.getEmail(), subject, body);
        }
        return ActionResult.SUCCESS;

    }

    private String replaceVersions(String in, String marker, String replacer) {
        return in.replace(marker, replacer);
    }
}
