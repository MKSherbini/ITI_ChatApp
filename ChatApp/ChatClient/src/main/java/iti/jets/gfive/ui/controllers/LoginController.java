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
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        // validate field

        UserDto userDto = new UserDto();
        boolean allFieldsValid = validateFields();
        if (!allFieldsValid) return;

        //validate login with DB
        //todo validation on textfiled of login
        //todo get and set the picture
        try {
            UserDBCrudInter userServices = UserDBCrudService.getUserService();
            System.out.println("befor");
            Image image = new Image(RegisterController.class.getResource("/iti/jets/gfive/images/personal.jpg").toString());
            userDto = userServices.selectFromDB(txt_loginPhone.getText(), txt_loginPass.getText());
            System.out.println("name  " + userDto.getUsername());
            System.out.println("imag  " + userDto.getImage());
            userDto.setPhoneNumber(txt_loginPhone.getText());

            //todo when login feature is merged then the hardcoded values will be replaced with the returned userDto obj
//        UserDto user = new UserDto("01234555555", "Mm1@"); //mahameho user
            //after validation register this client to the server
            ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
            try {
                clientConnectionInter.register(userDto);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            // todo call the thread that gets the contacts list and display in the listView
            // same thread or method to be called after adding a new contact aka --> a friend request accept
            ContactDBCrudInter contactDBCrudInter = ContactDBCrudService.getContactService();
            ArrayList<UserDto> contacts = null;
            try {
                contacts = contactDBCrudInter.getContactsList(userDto.getPhoneNumber());
                for (UserDto contact : contacts) {
                    System.out.println(contact);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
//            ContactsListView c = ContactsListView.getInstance();
//            c.fillContacts(contacts); // Sherbini: todo this was null for me, should be handled
            ModelsFactory modelsFactory = ModelsFactory.getInstance();
            CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
            currentUserModel.setPhoneNumber(txt_loginPhone.getText());
            currentUserModel.setUsername(userDto.getUsername());
            //in case the user did not enter the date in registeration
            Date date = userDto.getBirthDate();
            if (date != null) {
                currentUserModel.setDate(userDto.getBirthDate().toLocalDate());
            }
            currentUserModel.setCountry(userDto.getCountry());
            currentUserModel.setGender(userDto.getGender());
            currentUserModel.setEmail(userDto.getEmail());
            currentUserModel.setPassword(txt_loginPass.getText());
            currentUserModel.setBio(userDto.getBio());
            currentUserModel.setImage(userDto.getImage());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
//        stageCoordinator.switchToMainPage();
        stageCoordinator.switchToProfilePage();
    }

    public boolean validateFields() {
        return txt_loginPass.validate() & txt_loginPhone.validate();
    }

    public void resetFieldsValidation() {
        txt_loginPass.resetValidation();
        txt_loginPhone.resetValidation();

        // clear left for the binding
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

