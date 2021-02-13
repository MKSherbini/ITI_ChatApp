package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
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

    public void acceptContact(ActionEvent actionEvent) {
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        ContactDBCrudInter contactDBCrudService = ContactDBCrudService.getContactService();
        try {
            int rowsAffected = contactDBCrudService.insertContactRecord(senderIdLabel.getText(),
                    currentUserModel.getPhoneNumber());
            System.out.println("number of affected rows after contact insert: " + rowsAffected);

            ContactDBCrudInter contactDBCrudInter =  ContactDBCrudService.getContactService();
            ArrayList<UserDto> contacts = null;
            try {
                //5-update the contacts listView dunno yet
                contacts = contactDBCrudInter.getContactsList(currentUserModel.getPhoneNumber());
                ContactsListView c = ContactsListView.getInstance();
                c.fillContacts(contacts);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(rowsAffected == 0) return;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
