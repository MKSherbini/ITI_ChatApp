package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.application.Platform;
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
        validator.buildPhoneValidation(txtPhoneNumber);

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
        /*//1-check if this phone number is in the db registered?
        UserDBCrudInter userServices = UserDBCrudService.getUserService();
        boolean registered = true;
        try {
            registered = userServices.checkUserId(txtPhoneNumber.getText());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(!registered){
            // todo dialog or validation: already registered
            System.out.println("user doesn't exists, registered: " + registered);
            return;
        }
        //2-if registered go and send notification LATER :(
        //3-assume notification is sent and accepted
        //4-take the phone number and the current user's phone_number
        //and send as strings to server to add in the contacts table
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();

        ContactDBCrudInter contactDBCrudService = ContactDBCrudService.getContactService();
        try {
            int rowsAffected = contactDBCrudService.insertContactRecord(txtPhoneNumber.getText(),
                    //currentUserModel.getPhoneNumber()
                    "01234555555");
            System.out.println("number of affected rows after contact insert: " + rowsAffected);
            if(rowsAffected == 0) return;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //5-update the contacts listView dunno how :( */
    }

    public void addingContacts(String contactNum){
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        System.out.println("item 0 in list view: " + listView.getItems().get(0).toString());
        //todo dialog or validation: user is stupid and trying to add his/herself!!
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
        //2-if registered go and send notification LATER
        //3-assume notification is sent and accepted
        //4-take the phone number and the current user's phone_number
        //and send as strings to server to add in the contacts table
        ContactDBCrudInter contactDBCrudService = ContactDBCrudService.getContactService();
        try {
            int rowsAffected = contactDBCrudService.insertContactRecord(contactNum,
                    //currentUserModel.getPhoneNumber()
                    "01234555555");
            System.out.println("number of affected rows after contact insert: " + rowsAffected);
            ContactDBCrudInter contactDBCrudInter =  ContactDBCrudService.getContactService();
            ArrayList<UserDto> contacts = null;
            try {
                //5-update the contacts listView dunno yet
                contacts = contactDBCrudInter.getContactsList("01234555555");
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
