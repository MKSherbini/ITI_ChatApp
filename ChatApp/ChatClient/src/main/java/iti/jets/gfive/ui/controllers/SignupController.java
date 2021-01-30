package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfEmail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();

        tfUsername.textProperty().bindBidirectional(currentUserModel.usernameProperty());
        tfPassword.textProperty().bindBidirectional(currentUserModel.passwordProperty());
        tfEmail.textProperty().bindBidirectional(currentUserModel.emailProperty());
    }

    public void handleLoginBtnClick() {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToLoginScene();
    }

    public void handleSignupBtnClick() {
        System.out.println("Send to Server Signup data and show errors if any");
    }

}
