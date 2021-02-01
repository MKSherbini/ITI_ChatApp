package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.event.ActionEvent;

import java.net.URL;
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


        // validation
        Validator validator = Validator.getValidator();

        validator.addPhoneValidationEvt(txt_registerPhone);
        validator.validateWithBounds(txt_registerPass);
        validator.validateWithBounds(txt_displayName);
        validator.addRequiredFieldValidationEvt(txt_bDate);
    }
}
