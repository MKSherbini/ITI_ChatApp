package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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

import java.net.URL;
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

        validator.buildPhoneLoginValidation(txt_loginPhone);
        validator.buildRequiredPasswordValidation(txt_loginPass);
    }
}

