package iti.jets.gfive.ui.helpers;

import com.jfoenix.controls.JFXListView;
import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.ui.controllers.ContactController;
import iti.jets.gfive.ui.controllers.NotificationViewController;
import iti.jets.gfive.ui.controllers.NotificationsDialogController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NotificationMsgHandler extends UnicastRemoteObject implements NotificationReceiveInter {
    private static NotificationMsgHandler notificationsLabel = null;
    private Label notificationLabelId;
    //private JFXListView<BorderPane> notificationsListId = NotificationsDialogController.getNotificationsListId();
    private JFXListView<BorderPane> notificationsListId = new JFXListView<>();

    private NotificationMsgHandler() throws RemoteException {
        super();
    }

    public synchronized static NotificationMsgHandler getInstance(){
        if(notificationsLabel == null){
            try {
                System.out.println("creating new obj ");
                return notificationsLabel = new NotificationMsgHandler();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        System.out.println("getting the obj");
        return notificationsLabel;
    }

    public void setNotificationLabel(Label notificationLabelId){
        this.notificationLabelId = notificationLabelId;
    }

//    public void setNotificationsListId(JFXListView notificationsListId){
//        this.notificationsListId = notificationsListId;
//        System.out.println("notificationsListId is set to: " + notificationsListId);
//    }

    public void increaseNotificationsNumber(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int notificationsNumber = Integer.parseInt(notificationLabelId.getText());
                notificationsNumber+=1;
                notificationLabelId.setText(notificationsNumber+"");
            }
        });
    }

//    @Override
//    public void receive(NotificationDto notification) throws RemoteException {
//        increaseNotificationsNumber();
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/NotificationView.fxml"));
//                try {
//                    BorderPane item = fxmlLoader.load();
//                    NotificationViewController controller = fxmlLoader.getController();
//                    controller.notificationContentId.setText(notification.getContent());
//                    notificationsListId.getItems().add(item);
//                    System.out.println(notificationsListId.getItems().size());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public void receive(NotificationDto notification) throws RemoteException {
        increaseNotificationsNumber();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/NotificationView.fxml"));
        try {
            BorderPane item = fxmlLoader.load();
            NotificationViewController controller = fxmlLoader.getController();
            controller.notificationContentId.setText(notification.getContent());
            controller.senderIdLabel.setText(notification.getSenderId());
            notificationsListId.getItems().add(item);
            System.out.println(notificationsListId.getItems().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JFXListView<BorderPane> getNotificationsToFill(){
        return this.notificationsListId;
    }

}
