/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state;

import lombok.Getter;

public class StateStoreConfig {
    @Getter
    private String user;

    @Getter
    private String password;

    @Getter
    private String url;

    @Getter
    private String branch;
}
