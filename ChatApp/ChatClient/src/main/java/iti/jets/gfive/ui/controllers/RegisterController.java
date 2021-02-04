package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import iti.jets.gfive.db.UserDao;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    }

    @FXML
    void onClickRegisterSubmit(ActionEvent event) {
        // validate fields
        boolean allFieldsValid = txt_registerPhone.validate()
                & txt_registerPass.validate()
                & txt_displayName.validate()
                & txt_registerPassRepeat.validate();
        if (!allFieldsValid) return;

        Date d = Date.valueOf(txt_bDate.getValue());
        //System.out.println(d + " <-- date");
        RadioButton selectedRadioButton = (RadioButton) Gender.getSelectedToggle();
        String selectedGenderValue = selectedRadioButton.getText();
        //System.out.println(selectedGenderValue);

        UserDao userDao = new UserDao();
        //rest of the user data not represeted yet
        int rowsAffected = userDao.insertUserRecord(txt_registerPhone.getText(), txt_displayName.getText(),
                txt_registerPass.getText(), selectedGenderValue, d);
        System.out.println("number of affected rows after insert: " + rowsAffected);
        if(rowsAffected == 0) return;

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

        validator.buildPhoneValidation(txt_registerPhone);
        validator.buildPasswordValidation(txt_registerPass);
        validator.buildNameValidation(txt_displayName);
        validator.buildRepeatPasswordValidation(txt_registerPassRepeat, txt_registerPass);
//        validator.buildDateValidation(txt_bDate); //todo if not fixed un require date in register
    }
}
