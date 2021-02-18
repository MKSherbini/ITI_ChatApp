package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.*;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.event.ActionEvent;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.LocalDate;
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
        boolean allFieldsValid = validateFields();
        if (!allFieldsValid) return;

        Date birthDate = Date.valueOf(txt_bDate.getValue());
        //System.out.println(birthDate + " <-- date");
        RadioButton selectedRadioButton = (RadioButton) Gender.getSelectedToggle();
        String selectedGenderValue = selectedRadioButton.getText();
        //System.out.println(selectedGenderValue + " <-- gender");

//        Image image = new Image(RegisterController.class.getResource("/iti/jets/gfive/images/personal.jpg").toString());
        Image sponge = new Image(RegisterController.class.getResource("/iti/jets/gfive/images/sponge.png").toString());
        // Image image = new Image("/iti/jets/gfive/images/personal.jpg");
        //(1) creating the UserDto obj that will be transferred to the server.
        UserDto user = new UserDto(txt_registerPhone.getText(), txt_displayName.getText(),
                txt_registerPass.getText(), selectedGenderValue, birthDate, sponge);

        //(2) getting the singleton UserDBCrudService instance that has the server's obj.
        UserDBCrudInter userServices = UserDBCrudService.getUserService();

        try {
            //(3) calling the insert user service that inserts the user to the db
            int rowsAffected = userServices.insertUserRecord(user);
            System.out.println("number of affected rows after insert: " + rowsAffected);
            //(4) return from the method if the number of the affected rows are 0
            // which means no record were inserted.
            if (rowsAffected == 0) return;
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }

        // validate data and submit
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToLoginPage();
        // todo validate fields again in login, to avoid wrong errors
        // only login will sign in
    }

    public boolean validateFields() {
        return txt_registerPhone.validate()
                & txt_registerPass.validate()
                & txt_displayName.validate()
                & txt_registerPassRepeat.validate();
    }

    public void resetFields() {
        txt_registerPhone.resetValidation();
        txt_registerPass.resetValidation();
        txt_displayName.resetValidation();
        txt_registerPassRepeat.resetValidation();

//        txt_registerPhone.clear(); // left for the binding
//        txt_registerPass.clear();

        txt_displayName.clear();
        txt_registerPassRepeat.clear();
        txt_bDate.setValue(null);
        Gender.getToggles().forEach(toggle -> {
            JFXRadioButton t = (JFXRadioButton) toggle;
            if (t.getText().equals("Male")) t.setSelected(true);
        });
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

        validator.buildPhoneRegisterValidation(txt_registerPhone); // todo validate if exists using DB procedure
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
//        txt_registerPass.setText("!Q1q");
//        txt_registerPassRepeat.setText("!Q1q");
//        txt_registerPhone.setText("01234567895");
//        txt_bDate.setValue(LocalDate.now());
//        txt_displayName.setText("mmmm");

    }
}
