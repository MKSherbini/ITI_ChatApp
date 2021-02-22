package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.server.ClientConnectionImpl;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.HTMLEditor;

import javax.swing.text.html.HTMLEditorKit;
import java.rmi.RemoteException;

public class AnnouncementView {

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Button btnAnnounce;

    @FXML
    void sendAnnouncement(ActionEvent event) {
        try {
            ClientConnectionImpl clientConnection = new ClientConnectionImpl();
            clientConnection.publishAnnouncement(htmlEditor.getHtmlText());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(htmlEditor.getHtmlText());
    }

    public void onBackBtn(ActionEvent actionEvent) {
        StageCoordinator.getInstance().switchToServerMain();
    }
}

