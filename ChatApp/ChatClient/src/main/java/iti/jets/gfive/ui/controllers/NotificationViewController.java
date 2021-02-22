package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.NotificationCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.services.NotificationDBCrudService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class NotificationViewController {
    @FXML
    public Label notificationContentId;

    @FXML
    public Label senderIdLabel;

    @FXML
    private Button acceptBtnId;

    @FXML
    private Button declineBtnId;

    public int notificationId;



    NotificationMsgHandler notificationMsgHandler = NotificationMsgHandler.getInstance();
    NotificationCrudInter notificationDBCrudService = NotificationDBCrudService.getNotificationService();

    public void acceptContact(ActionEvent actionEvent) {
        notificationMsgHandler.decreaseNotificationsNumber();
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        ContactDBCrudInter contactDBCrudService = ContactDBCrudService.getContactService();
        try {
            int rowsAffected = contactDBCrudService.insertContactRecord(senderIdLabel.getText(),
                    currentUserModel.getPhoneNumber());
            System.out.println("number of affected rows after contact insert: " + rowsAffected);
            if(rowsAffected == 0) return;
            ContactDBCrudInter contactDBCrudInter =  ContactDBCrudService.getContactService();
            ArrayList<UserDto> contacts = null;
            contacts = contactDBCrudInter.getContactsList(currentUserModel.getPhoneNumber());
            ContactsListView c = ContactsListView.getInstance();
            c.fillContacts(contacts);

            notificationDBCrudService.updateNotificationStatus(this.notificationId);
            contactDBCrudInter.updateUserContacts(senderIdLabel.getText());
            acceptBtnId.setDisable(true);
            declineBtnId.setDisable(true);
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }
    }

    public void declineContact(ActionEvent actionEvent) {
        notificationMsgHandler.decreaseNotificationsNumber();
        try {
            notificationDBCrudService.updateNotificationStatus(this.notificationId);
            acceptBtnId.setDisable(true);
            declineBtnId.setDisable(true);
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }
    }
}
