package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.*;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.UserDBCrudService;
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
import java.rmi.RemoteException;
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
        Date date=null;
        //check for email validation
        boolean emailvalidation = false;
        if(EmailID!=null)
        {
            emailvalidation =  EmailID.validate();
        }
        else
        {
            emailvalidation = true;
        }
        boolean fieldsValidation = NameID.validate() & PasswordID.validate()
                & ReEnterPasswordID.validate();
        if (fieldsValidation && emailvalidation ) {
            try {
                if (BirthDateID.getValue() != null) {
                    date = Date.valueOf(BirthDateID.getValue());
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


            UserDto user = new UserDto(PhoneID.getText(), NameID.getText(),
                    PasswordID.getText(), gender,date ,CountryID.getValue() ,EmailID.getText() ,BioID.getText());

            UserDBCrudInter userServices = UserDBCrudService.getUserService();

            try {

                int rowsAffected = userServices.updateUserRecord(user);
                System.out.println("rows affected " + rowsAffected);
                if(rowsAffected == 0) return;
            }catch (RemoteException e)
            {
                e.printStackTrace();
            }
            


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


        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();


        PhoneID.setText(currentUserModel.getPhoneNumber());
        NameID.setText(currentUserModel.getUsername());
        EmailID.setText(currentUserModel.getEmail());
        PasswordID.setText(currentUserModel.getPassword());
        ReEnterPasswordID.setText(currentUserModel.getPassword());


        if (currentUserModel.getGender().equals("male")) {
            System.out.print("inside male check");
            MaleRadioButton.setSelected(true);

        } else if (currentUserModel.getGender().equals("female")) {
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
        //if(!EmailID.getText().equals(""))
        validator.buildEmailValidation(EmailID);


    }

    public void fillCountryCombobox() {

        CountryID.getItems().addAll("Albania", "Algeria", "Andorra", "Argentia", "Austalia", "Bahrain", "Belize", "Bolivia",
                "Cambodia", "Cameroon", "Canada", "Dominica", "Egypt", "Estonia", "Finland", "Greece", "Grenada", "Haiti", "Iceland", "Japan", "Laos", "Lebanon",
                "Libya", "Mali", "Panama", "Saudi Arabia", "serbia", "Turkey", "Vietnam", "Yemen", "Zambia", "Zimbabwe");

        CountryID.getSelectionModel().select("Egypt");
    }
}

