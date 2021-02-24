package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.server.ClientConnectionImpl;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.HTMLEditor;

import javax.swing.text.html.HTMLEditorKit;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnouncementView  implements Initializable {

    public Label lblSent;
    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Button btnAnnounce;
    @FXML
    private Label lblError;

    private String getInputlength(String htmlText) {
        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(htmlText);
        final StringBuffer sb = new StringBuffer(htmlText.length());
        while(matcher.find()) {
            matcher.appendReplacement(sb, " ");
        }
        matcher.appendTail(sb);
        String s = sb.toString().replace("&nbsp;"," ");
//        System.out.println(s.toString().trim()+ " with length :" + s.toString().trim().length());
        return s.trim();
    }

    @FXML
    void sendAnnouncement(ActionEvent event) {
        try {
            if(getInputlength(htmlEditor.getHtmlText()).length()<10){
                System.out.println(getInputlength(htmlEditor.getHtmlText()));
                lblError.setVisible(true);
                Platform.runLater(()->{
                    new Thread(()->{
                            try {
                                Thread.sleep(1000);
                                lblError.setVisible(false);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
               });
                return ;
            }else{

                lblError.setVisible(false);
                ClientConnectionImpl clientConnection = new ClientConnectionImpl();
                clientConnection.publishAnnouncement(htmlEditor.getHtmlText());
                lblSent.setVisible(true);
                Platform.runLater(()->{
                    new Thread(()->{
                        try {
                            Thread.sleep(1000);
                            lblSent.setVisible(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                });
                System.out.println("announce sent .....");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
   }

    public void onBackBtn(ActionEvent actionEvent) {
        StageCoordinator.getInstance().switchToServerMain();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

