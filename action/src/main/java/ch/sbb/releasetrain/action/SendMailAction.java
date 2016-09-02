/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.action;

import java.util.HashMap;
import java.util.Map;

import ch.sbb.releasetrain.config.model.releaseconfig.EmailActionConfig;
import ch.sbb.releasetrain.utils.emails.SMTPUtilImpl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.sbb.releasetrain.config.ConfigAccessor;
import ch.sbb.releasetrain.state.model.ActionResult;
import ch.sbb.releasetrain.state.model.ActionState;

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
    private SMTPUtilImpl smtpUtil;

    @Autowired
    @Setter
    private ConfigAccessor config;

    @Override
    public String getName() {
        return "emailAction";
    }

    @Override
    public ActionResult doWork(ActionState state, HashMap properties) {

        EmailActionConfig config = (EmailActionConfig) state.getConfig();

        Map<String, String> params = new HashMap<>(config.getProperties());

        properties.putAll(params);

        String subject = replaceVars(config.getSubject(),properties);
        String text = replaceVars(config.getText(),properties);

        // List<MailReceiver> receiver = config.readMailReveiverForMailinglist(mailinglist);

        String[] arr = config.getReceiver().split(",");

        smtpUtil.setMailhost(config.getSmtpServer());
        for (String rec : arr) {
            smtpUtil.send(config.getSender(), rec, subject, text);
        }
        return ActionResult.SUCCESS;

     }

    private String replaceVars(String in, Map<String, String> params) {
        for(String key : params.keySet()){
            if(key == null || params.get(key) == null){
                continue;
            }
           in = in.replace("@"+key+"@", params.get(key));
        }
        return in;
    }
}
