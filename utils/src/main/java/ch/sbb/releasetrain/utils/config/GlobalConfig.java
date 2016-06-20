/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.config;

/**
 * Global Config for all the Mojos
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
public interface GlobalConfig {

    String get(String id);

}
