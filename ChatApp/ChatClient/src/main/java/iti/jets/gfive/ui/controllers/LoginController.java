package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.NotificationCrudInter;
import iti.jets.gfive.common.interfaces.*;
import iti.jets.gfive.common.models.GroupDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.*;
import iti.jets.gfive.ui.helpers.*;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.event.ActionEvent;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.sql.Date;
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
        boolean allFieldsValid = validateFields();
        if (!allFieldsValid) return;
        // validate field
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.handleLogin(txt_loginPhone.getText(), txt_loginPass.getText());
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToMainPage();
    }


    public void getNotifications(UserDto user) {
        new Thread(() -> {
            NotificationCrudInter notificationCrudInter = NotificationDBCrudService.getNotificationService();
            try {
                ArrayList<NotificationDto> notificationsList = notificationCrudInter.getNotificationList(user.getPhoneNumber());
                NotificationMsgHandler notificationMsgHandler = NotificationMsgHandler.getInstance();
                notificationMsgHandler.addNotifications(notificationsList);
            } catch (RemoteException e) {
                e.printStackTrace();
                StageCoordinator.getInstance().reset();
                return;
            }
        }).start();
    }

    public boolean validateFields() {
        return txt_loginPass.validate() & txt_loginPhone.validate();
    }

    public void resetFieldsValidation() {
        FieldIconBinder fieldIconBinder = FieldIconBinder.getInstance();
        fieldIconBinder.resetValidation(txt_loginPhone);
        fieldIconBinder.resetValidation(txt_loginPass);

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

        validator.buildPhoneLoginValidation(txt_loginPhone);
        validator.buildLoginPasswordValidation(txt_loginPass, txt_loginPhone);

    }
}

