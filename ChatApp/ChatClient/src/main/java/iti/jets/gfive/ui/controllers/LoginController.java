package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ClientConnectionService;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label lb_appName;

    @FXML
    private FontIcon icon_loginPhone;

    @FXML
    private JFXTextField txt_loginPhone;

    @FXML
    private FontIcon icon_loginPass;

    @FXML
    private JFXPasswordField txt_loginPass;

    @FXML
    private JFXButton btn_signSubmit;

    @FXML
    private JFXButton btn_registerSwitch;

    @FXML
    void onClickLoginSubmit(ActionEvent event) {
        // validate fields
        boolean allFieldsValid = txt_loginPass.validate() & txt_loginPhone.validate();
//        if (!allFieldsValid) return;
        //validate login with DB

        //todo when login feature is merged then the hardcoded values will be replaced with the returned userDto obj
        UserDto user = new UserDto("01234555555", "Mm1@"); //mahameho user
        //after validation register this client to the server
        ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
        try {
            clientConnectionInter.register(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // todo call the thread that gets the contacts list and display in the listView
        // same thread or method to be called after adding a new contact aka --> a friend request accept
        ContactDBCrudInter contactDBCrudInter =  ContactDBCrudService.getContactService();
        ArrayList<UserDto> contacts = null;
        try {
            contacts = contactDBCrudInter.getContactsList("01234555555");
            for (UserDto contact : contacts) {
                System.out.println(contact);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ContactsListView c = ContactsListView.getInstance();
        c.fillContacts(contacts);

        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToMainPage();
    }

    @FXML
    void onClickRegisterSwitch(ActionEvent event) {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToRegisterPage();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // binding
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();

        txt_loginPhone.textProperty().bindBidirectional(currentUserModel.phoneNumberProperty());
        txt_loginPass.textProperty().bindBidirectional(currentUserModel.passwordProperty());
        btn_signSubmit.requestFocus();

        // colors
        FieldIconBinder iconBinder = FieldIconBinder.getInstance();
        iconBinder.bind(txt_loginPhone, icon_loginPhone);
        iconBinder.bind(txt_loginPass, icon_loginPass);

        // validation
        Validator validator = Validator.getInstance();

        validator.buildPhoneValidation(txt_loginPhone);
        validator.buildRequiredPasswordValidation(txt_loginPass);

    }
}

