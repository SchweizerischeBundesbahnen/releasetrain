/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.emails;

/**
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public interface SMTPUtil {

     void send(final String absender, final String empfaenger, final String betreff, final String text);

}