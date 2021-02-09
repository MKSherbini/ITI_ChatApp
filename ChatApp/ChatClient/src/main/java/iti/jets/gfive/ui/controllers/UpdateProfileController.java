package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.*;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class UpdateProfileController implements Initializable {

    @FXML
    private JFXTextField PhoneID;

    @FXML
    private JFXTextField NameID;

    @FXML
    private JFXTextField EmailID;

    @FXML
    private JFXPasswordField PasswordID;

    @FXML
    private JFXComboBox<String> CountryID;

    @FXML
    private JFXDatePicker BirthDateID;

    @FXML
    private JFXTextArea BioID;

    @FXML
    private JFXPasswordField ReEnterPasswordID;

    @FXML
    private Button UpdateBtnID;

    @FXML
    private JFXRadioButton FemaleRadioButton;

    @FXML
    private JFXRadioButton MaleRadioButton;

    @FXML
    private ToggleGroup Gender;

    @FXML
    void OnCLickUpdateProfile(ActionEvent event) {
        boolean fieldsValidation = NameID.validate() & EmailID.validate() & PasswordID.validate()
                & ReEnterPasswordID.validate();
        if (fieldsValidation) {
            try {
                if (BirthDateID.getValue() != null) {
                    Date date = Date.valueOf(BirthDateID.getValue());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            String gender = "";
            try {
                RadioButton SelectedRadioButton = (RadioButton) Gender.getSelectedToggle();
                gender = SelectedRadioButton.getText();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            //UserDao userDao = new UserDao();

            //give to it phone number to update its record
            //  int affectedRows = userDao.updateUserRecord(PhoneID.getText() ,  NameID.getText() , EmailID.getText() , PasswordID.getText()
            //  , gender , CountryID.getValue() ,date ,BioID.getText());
            //update user object
            ModelsFactory modelsFactory = ModelsFactory.getInstance();
            CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
            currentUserModel.setUsername(NameID.getText());
            currentUserModel.setDate(BirthDateID.getValue());
            currentUserModel.setCountry(CountryID.getValue());
            currentUserModel.setGender(gender);
            currentUserModel.setEmail(EmailID.getText());
            currentUserModel.setPassword(PasswordID.getText());
            currentUserModel.setBio(BioID.getText());


            StageCoordinator stageCoordinator = StageCoordinator.getInstance();
            stageCoordinator.switchToProfilePage();

        }

    }

    @FXML
    void onClickBack(ActionEvent event) {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToProfilePage();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillCountryCombobox();

        //binding to fill the texts in the screen
      /*  ModelsFactory modelsFactory =ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        PhoneID.textProperty().bindBidirectional(currentUserModel.phoneNumberProperty());
        //currentUserModel.phoneNumberProperty().bind(PhoneID.textProperty());
        NameID.textProperty().bindBidirectional(currentUserModel.usernameProperty());
        //currentUserModel.usernameProperty().bind(NameID.textProperty());
        EmailID.textProperty().bindBidirectional(currentUserModel.emailProperty());
        //currentUserModel.emailProperty().bind(EmailID.textProperty());
        BioID.textProperty().bindBidirectional(currentUserModel.bioProperty());
        CountryID.valueProperty().bindBidirectional(currentUserModel.countryProperty());
        //not tested
         BirthDateID.valueProperty().bindBidirectional(currentUserModel.dateProperty());
        System.out.println("GENDER "+ currentUserModel.getGender());
      /*  if(currentUserModel.getGender().equals("Male"))
        {
            MaleRadioButton.setSelected(true);
        }
        else if(currentUserModel.getGender().equals("Female"))
        {
           FemaleRadioButton.setSelected(true);
        }*/

        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();


        PhoneID.setText(currentUserModel.getPhoneNumber());
        NameID.setText(currentUserModel.getUsername());
        EmailID.setText(currentUserModel.getEmail());
        PasswordID.setText(currentUserModel.getPassword());
        ReEnterPasswordID.setText(currentUserModel.getPassword());

        if (currentUserModel.getGender().equals("Male")) {
            MaleRadioButton.setSelected(true);

        } else if (currentUserModel.getGender().equals("Female")) {
            FemaleRadioButton.setSelected(true);
        }

        CountryID.setValue(currentUserModel.getCountry());
        BirthDateID.setValue(currentUserModel.getDate());
        BioID.setText(currentUserModel.getBio());


        // validation
        Validator validator = Validator.getInstance();

        validator.buildNameValidation(NameID);
        validator.buildPasswordValidation(PasswordID);
        validator.buildRepeatPasswordValidation(ReEnterPasswordID, PasswordID);
        validator.buildEmailValidation(EmailID);


    }

    public void fillCountryCombobox() {

        CountryID.getItems().addAll("Albania", "Algeria", "Andorra", "Argentia", "Austalia", "Bahrain", "Belize", "Bolivia",
                "Cambodia", "Cameroon", "Canada", "Dominica", "Egypt", "Estonia", "Finland", "Greece", "Grenada", "Haiti", "Iceland", "Japan", "Laos", "Lebanon",
                "Libya", "Mali", "Panama", "Saudi Arabia", "serbia", "Turkey", "Vietnam", "Yemen", "Zambia", "Zimbabwe");

        CountryID.getSelectionModel().select("Egypt");
    }
}

