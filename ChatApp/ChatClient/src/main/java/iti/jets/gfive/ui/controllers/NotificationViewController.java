package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXListView;
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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NotificationViewController implements Initializable {
    @FXML
    public Label notificationContentId;

    @FXML
    public Label senderIdLabel;

    @FXML
    private Button acceptBtnId;

    @FXML
    private Button declineBtnId;

    public int notificationId;

    private ListView<BorderPane> contactsList;

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
            if (rowsAffected == 0) return;
            ContactDBCrudInter contactDBCrudInter = ContactDBCrudService.getContactService();
            UserDto contactInfo = contactDBCrudInter.getContactInfo(senderIdLabel.getText());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));
                    try {
                        BorderPane item = fxmlLoader.load();
                        ContactController controller = fxmlLoader.getController();
                        controller.contactNameLabel.setText(contactInfo.getUsername());
                        controller.contactNumberLabel.setText(contactInfo.getPhoneNumber());
                        controller.ivStatus.setImage(new Image(getClass().getResource(String.format(MainScreenController.URL_RESOURCE,contactInfo.getStatus())).toString()));
                        controller.contactImg.setImage(contactInfo.getImage());
                        contactsList.getItems().add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            notificationDBCrudService.updateNotificationStatus(this.notificationId);
            UserDto currUserInfo = new UserDto();
            currUserInfo.setPhoneNumber(currentUserModel.getPhoneNumber());
            currUserInfo.setUsername(currentUserModel.getUsername());
            currUserInfo.setStatus(currentUserModel.getStatus());
            currUserInfo.setImage(currentUserModel.getImage());
            contactDBCrudInter.updateUserContacts(senderIdLabel.getText(), currUserInfo);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactsListView c = ContactsListView.getInstance();
        JFXListView<BorderPane> list = c.getContactsListViewId();
        if (list == null) System.out.println("THE LIST IS NULL IN THE NOTIFICATION-VIEW-CONTROLLER");
        this.contactsList = list;
    }
}
