package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.UserDBCrudService;
import com.jfoenix.controls.*;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.event.ActionEvent;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private Label lb_appName;

    @FXML
    private FontIcon icon_registerPhone;

    @FXML
    private JFXTextField txt_registerPhone;

    @FXML
    private FontIcon icon_displayName;

    @FXML
    private JFXTextField txt_displayName;

    @FXML
    private FontIcon icon_registerPass;

    @FXML
    private JFXPasswordField txt_registerPass;

    @FXML
    private FontIcon icon_registerPassRepeat;

    @FXML
    private JFXPasswordField txt_registerPassRepeat;

    @FXML
    private FontIcon icon_bDate;

    @FXML
    private JFXDatePicker txt_bDate;

    @FXML
    private ToggleGroup Gender;

    @FXML
    private JFXButton btn_loginSwitch;

    @FXML
    private JFXButton btn_registerSubmit;

    @FXML
    void onClickLoginSwitch(ActionEvent event) {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToLoginPage();

        //check if the user if registered or not
        //check if the password is correct
        //if all correct then go fetch this phone_number's data from the db
        //it will return a UserDto that we're going to take its values and set the UserModel's properties
        //then switch to main page
    }

    @FXML
    void onClickRegisterSubmit(ActionEvent event) {
        // validate fields
        boolean allFieldsValid = txt_registerPhone.validate()
                & txt_registerPass.validate()
                & txt_displayName.validate()
                & txt_registerPassRepeat.validate();
        if (!allFieldsValid) return;

        Date birthDate = Date.valueOf(txt_bDate.getValue());
        //System.out.println(birthDate + " <-- date");
        RadioButton selectedRadioButton = (RadioButton) Gender.getSelectedToggle();
        String selectedGenderValue = selectedRadioButton.getText();
        //System.out.println(selectedGenderValue + " <-- gender");

        //(1) creating the UserDto obj that will be transferred to the server.
        UserDto user = new UserDto(txt_registerPhone.getText(), txt_displayName.getText(),
                txt_registerPass.getText(), selectedGenderValue, birthDate);

        //(2) getting the singleton UserDBCrudService instance that has the server's obj.
        UserDBCrudInter userServices = UserDBCrudService.getUserService();
        // todo here have to check if the user is already registered!!!
        try {
            //(3) calling the insert user service that inserts the user to the db
            int rowsAffected = userServices.insertUserRecord(user);
            System.out.println("number of affected rows after insert: " + rowsAffected);
            //(4) return from the method if the number of the affected rows are 0
            // which means no record were inserted.
            if(rowsAffected == 0) return;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // validate data and submit
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToLoginPage();
        // only login will sign in
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // binding
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();

        txt_registerPhone.textProperty().bindBidirectional(currentUserModel.phoneNumberProperty());
        txt_registerPass.textProperty().bindBidirectional(currentUserModel.passwordProperty());


        // colors
        FieldIconBinder iconBinder = FieldIconBinder.getInstance();
        iconBinder.bind(txt_registerPhone, icon_registerPhone);
        iconBinder.bind(txt_registerPass, icon_registerPass);
        iconBinder.bind(txt_displayName, icon_displayName);
        iconBinder.bind(txt_registerPassRepeat, icon_registerPassRepeat);
//        txt_bDate.styleProperty().bind(icon_bDate.styleProperty());
//        txt_bDate.setDefaultColor(Color.BLUE);


        // validation
        Validator validator = Validator.getInstance();

        validator.buildPhoneValidation(txt_registerPhone); // todo validate if exists using DB procedure
        validator.buildPasswordValidation(txt_registerPass);
        validator.buildNameValidation(txt_displayName);
        validator.buildRepeatPasswordValidation(txt_registerPassRepeat, txt_registerPass);
//        validator.buildDateValidation(txt_bDate); //todo if not fixed un require date in register


//        // test
//        ((JFXRadioButton) Gender.getSelectedToggle()).getText();
//
//        Gender.getToggles().forEach(toggle -> {
//            JFXRadioButton t = (JFXRadioButton) toggle;
//            if (t.getText().equals("Female")) t.setSelected(true);
//        });

    }
}
