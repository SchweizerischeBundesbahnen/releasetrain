/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.model;

import ch.sbb.releasetrain.utils.crypt.EncryptorImpl;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;


/**
 * Config to a GIT Repo with User PW and Branch
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@AllArgsConstructor
@NoArgsConstructor
public class GitModel {

    private String prefix = "config.";

    private String configUrl;
    private String configBranch;
    private String configUser;

    @Setter @Getter
    private String configPassword;

    public String getEncPassword(){
        if(StringUtils.isEmpty(configPassword)){
            return "";
        }
        setSystemProp("password",configPassword);
        return EncryptorImpl.decrypt(configPassword);
    }

    public void setEncPassword(String password){
        setSystemProp("password",password);
        configPassword = EncryptorImpl.encrypt(password);
    }

    public String getConfigUrl() {
        setSystemProp("url",configUrl);
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        setSystemProp("url",configUrl);
        this.configUrl = configUrl;
    }

    public String getConfigBranch() {
        setSystemProp("branch",configBranch);
        return configBranch;
    }

    public void setConfigBranch(String configBranch) {
        setSystemProp("branch",configBranch);
        this.configBranch = configBranch;
    }

    public String getConfigUser() {
        setSystemProp("user",configUser);
        return configUser;
    }

    public void setConfigUser(String configUser) {
        setSystemProp("user",configUser);
        this.configUser = configUser;
    }

    private void setSystemProp(String key,String value){
        if(value == null){
            return;
        }
        System.setProperty(prefix + key,value);
    }
}