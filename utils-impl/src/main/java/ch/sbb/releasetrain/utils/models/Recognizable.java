/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.models;

/**
 * provides a getter for an ID
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @param <T>
 * @since 2.0.7, 2015
 */
public interface Recognizable<T> extends Comparable<T> {

    String getId();

}
