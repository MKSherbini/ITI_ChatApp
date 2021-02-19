package iti.jets.gfive.ui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class ErrorPageController {
    public Label lb_appName;

    public void onClickQuit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
