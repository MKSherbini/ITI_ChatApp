module ChatClient {
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
    requires ChatCommon;
    requires java.rmi;
    requires jakarta.xml.bind;
    requires org.apache.commons.io;
    requires javafx.swing;
    requires javafx.web;


    exports iti.jets.gfive;
    opens iti.jets.gfive.ui.controllers  to javafx.fxml, javafx.web;
    opens iti.jets.gfive.ui.models.chat to jakarta.xml.bind;
}