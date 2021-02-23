package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.server.ClientConnectionImpl;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.HTMLEditor;

import javax.swing.text.html.HTMLEditorKit;
import java.rmi.RemoteException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnouncementView {

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Button btnAnnounce;
    @FXML
    private Label lblError;
    public static String getText(String htmlText) {
        String result = "";

        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(htmlText);
        final StringBuffer text = new StringBuffer(htmlText.length());

        while (matcher.find()) {
            matcher.appendReplacement(
                    text,
                    "");
        }

        matcher.appendTail(text);

        result = text.toString().trim();

        return result;
    }

    @FXML
    void sendAnnouncement(ActionEvent event) {
        try {
            System.out.println(htmlEditor.getHtmlText().length());
            if(getText(htmlEditor.getHtmlText()).length()>5){

                lblError.setVisible(true);
//                htmlEditor.getHtmlText().
                return ;
            }
            lblError.setVisible(false);
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

