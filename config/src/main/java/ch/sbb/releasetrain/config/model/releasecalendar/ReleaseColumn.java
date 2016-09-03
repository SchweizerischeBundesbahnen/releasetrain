/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config.model.releasecalendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a Calendar
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Slf4j
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseColumn {
    private String name;
    private Boolean on;
}
