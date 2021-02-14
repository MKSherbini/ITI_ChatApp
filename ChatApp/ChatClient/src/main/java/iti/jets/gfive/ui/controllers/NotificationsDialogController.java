package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXListView;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationsDialogController implements Initializable {

    @FXML
    private JFXListView<BorderPane> notificationsListId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NotificationMsgHandler notificationMsgHandler = NotificationMsgHandler.getInstance();
//        notificationMsgHandler.setNotificationsListId(notificationsListId);
        JFXListView<BorderPane> notificationsList =notificationMsgHandler.getNotificationsToFill();
        fillInTheList(notificationsList);

    }

    public void fillInTheList(JFXListView<BorderPane> notificationsList){
        for (BorderPane notifComponent: notificationsList.getItems()) {
            notificationsListId.getItems().add(notifComponent);
        }
    }
//    public static JFXListView<BorderPane> getNotificationsListId(){
//        System.out.println("**inside the getNotificationListId**");
//        return notificationsListId;
//    }
}