package iti.jets.gfive.ui.helpers;

import com.jfoenix.controls.JFXListView;
import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.ui.controllers.NotificationViewController;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
                //System.out.println("creating new obj ");
                return notificationsLabel = new NotificationMsgHandler();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("getting the obj");
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

    public void decreaseNotificationsNumber(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int notificationsNumber = Integer.parseInt(notificationLabelId.getText());
                if(notificationsNumber == 0) return;
                notificationsNumber-=1;
                notificationLabelId.setText(notificationsNumber+"");
            }
        });
    }

    public void receive(NotificationDto notification) throws RemoteException {
        increaseNotificationsNumber();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/NotificationView.fxml"));
        try {
            BorderPane item = fxmlLoader.load();
            NotificationViewController controller = fxmlLoader.getController();
            controller.notificationContentId.setText(notification.getContent());
            controller.senderIdLabel.setText(notification.getSenderId());
            controller.notificationId = notification.getId();
            notificationsListId.getItems().add(item);
            //System.out.println(notificationsListId.getItems().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateContactsList() throws RemoteException {
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        ContactDBCrudInter contactDBCrudInter =  ContactDBCrudService.getContactService();
        ArrayList<UserDto> contacts;
        try {
            contacts = contactDBCrudInter.getContactsList(currentUserModel.getPhoneNumber());
            ContactsListView c = ContactsListView.getInstance();
            c.fillContacts(contacts);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public JFXListView<BorderPane> getNotificationsToFill(){
        return this.notificationsListId;
    }

    public void addNotifications(ArrayList<NotificationDto> notifications){
        System.out.println(notifications.size() + "<-------------1");
        for (NotificationDto notification: notifications) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/NotificationView.fxml"));
            try {
                BorderPane item = fxmlLoader.load();
                NotificationViewController controller = fxmlLoader.getController();
                System.out.println(notification.getContent() + "<-------------2");
                controller.notificationContentId.setText(notification.getContent());
                controller.senderIdLabel.setText(notification.getSenderId());
                controller.notificationId = notification.getId();
                notificationsListId.getItems().add(item);
                //System.out.println(notificationsListId.getItems().size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
