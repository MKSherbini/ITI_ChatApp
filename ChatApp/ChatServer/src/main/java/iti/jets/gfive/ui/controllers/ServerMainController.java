package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import iti.jets.gfive.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ServerMainController {

    @FXML
    private Label lb_appName;

    @FXML
    private Label lbl_serverStatusColor;

    @FXML
    private Label lbl_serverStatusText;

    @FXML
    private JFXButton btn_toggleService;

    @FXML
    private JFXButton btn_viewStatus;

    @FXML
    private JFXButton btn_SendAnnouncement;

    @FXML
    private JFXButton btn_quit;


    @FXML
    void onClickQuit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onClickSendAnnouncement(ActionEvent event) {

    }

    @FXML
    void onClickToggleService(ActionEvent event) {
        toggleServerState();
    }

    @FXML
    void onClickViewStatus(ActionEvent event) {

    }

    private void toggleServerState() {
        Server server = Server.getInstance();
        var state = server.toggleServerRunning();

        if (state) {
            lbl_serverStatusText.setText("Online");
            lbl_serverStatusColor.getStyleClass().clear();
            lbl_serverStatusColor.getStyleClass().add("online-label");
            btn_toggleService.setText("Stop Service");
        } else {
            lbl_serverStatusText.setText("Offline");
            lbl_serverStatusColor.getStyleClass().clear();
            lbl_serverStatusColor.getStyleClass().add("offline-label");
            btn_toggleService.setText("Start Service");
        }
    }

}
