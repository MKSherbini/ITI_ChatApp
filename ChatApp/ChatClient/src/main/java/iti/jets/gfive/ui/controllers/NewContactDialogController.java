package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.NotificationCrudInter;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.services.NotificationDBCrudService;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NewContactDialogController implements Initializable {

    @FXML
    public Button btnNew;
    @FXML
    public JFXListView listView;
    @FXML
    public JFXTextField txtPhoneNumber;
    @FXML
    private FontIcon icon_loginPhone;
    @FXML
    public Button btnCancel;
    @FXML
    public Button btnAdd;
    ObservableList<String> phones;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FieldIconBinder iconBinder = FieldIconBinder.getInstance();
        iconBinder.bind(txtPhoneNumber, icon_loginPhone);

        Validator validator = Validator.getInstance();
        validator.buildPhoneContactValidation(txtPhoneNumber); // todo check this

        phones = FXCollections.observableArrayList();
        listView.getItems().addAll(phones);

    }

    public void performCancel(ActionEvent actionEvent) {
//        Platform.exit();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void performAddContact(ActionEvent actionEvent) {
        for (Object phoneNumber : listView.getItems()) {
            addingContacts(phoneNumber.toString());
            System.out.println(phoneNumber.toString() + " Added to db");
        }
    }

    public void addingContacts(String contactNum){
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        //System.out.println("item 0 in list view: " + listView.getItems().get(0).toString());
        //todo dialog or validation: user is stupid and trying to add him/herself!!
        if(contactNum.equals(currentUserModel.getPhoneNumber())){
            System.out.println("Are you trying to add yourself!!");
            return;
        }
        //1-check if this phone number is registered in the db?
        UserDBCrudInter userServices = UserDBCrudService.getUserService();
        boolean registered = true;
        try {
            registered = userServices.checkUserId(contactNum);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(!registered){
            // todo dialog or validation: user is not registered??
            System.out.println("user doesn't exists, registered: " + registered);
            return;
        }
        //todo add the same user twice - CHECK
        //2-if registered go and send notification LATER, go and insert the notification in db
        //2-go insert the notification in db
        NotificationCrudInter notificationServices = NotificationDBCrudService.getNotificationService();
        String notificationContent = (currentUserModel.getUsername() +" with the phone number "
                 +currentUserModel.getPhoneNumber() +" is trying to add you");
        long millis=System.currentTimeMillis();
        Date currentDate = new Date(millis);
        //System.out.println("current date: " + currentDate.toString());
        try {
            int notifyRowsAffected = notificationServices.insertNotification(notificationContent,
                    currentUserModel.getPhoneNumber(), currentDate, false, contactNum);
            System.out.println("rows affected after notification: " + notifyRowsAffected);
            if(notifyRowsAffected <= 1) return;
            //todo tell the server to go and increase the label
            //notificationServices.sendNotification(contactNum);
            notificationServices.sendNotification(currentUserModel.getPhoneNumber());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //3-assume notification is sent and accepted
        //4-take the phone number and the current user's phone_number
        //and send as strings to server to add in the contacts table
        ContactDBCrudInter contactDBCrudService = ContactDBCrudService.getContactService();
        try {
            int rowsAffected = contactDBCrudService.insertContactRecord(contactNum,
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

    public void performNewContact(ActionEvent actionEvent) {

       listView.getItems().add(txtPhoneNumber.getText());
       txtPhoneNumber.setText("");
    }

}
