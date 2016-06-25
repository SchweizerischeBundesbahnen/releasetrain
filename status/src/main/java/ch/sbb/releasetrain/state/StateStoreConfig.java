/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class StateStoreConfig {

    @Value("${store.user:default}")
    private String user;

    @Value("${store.password:default}")
    private String password;

    @Value("${store.url:default}")
    private String url;

    @Value("${store.branch:default}")
    private String branch;
}
