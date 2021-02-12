module ChatCommon {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.materialdesign2;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;

    requires java.sql;
    requires mysql.connector.java;
    requires java.naming;
    requires org.apache.commons.lang3;

    requires ab;
    requires java.rmi;
    requires javafx.swing;

    exports iti.jets.gfive.common.interfaces;
    exports iti.jets.gfive.common.models;
}