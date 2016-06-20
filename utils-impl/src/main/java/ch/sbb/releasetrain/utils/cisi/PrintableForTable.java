/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.cisi;

public interface PrintableForTable {

    String getComputedCheckURL();

    AbstractCheckable getServlet();

    CheckableModel getCluster();

}