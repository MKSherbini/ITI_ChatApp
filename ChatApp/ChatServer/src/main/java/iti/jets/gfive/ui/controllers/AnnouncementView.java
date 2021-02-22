package iti.jets.gfive.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.HTMLEditor;

import javax.swing.text.html.HTMLEditorKit;

public class AnnouncementView {

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Button btnAnnounce;

    @FXML
    void sendAnnouncement(ActionEvent event) {
        System.out.println(htmlEditor.getHtmlText());
    }

}

