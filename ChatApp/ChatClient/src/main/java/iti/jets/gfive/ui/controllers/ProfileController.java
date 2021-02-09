package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private Label BioID;

    @FXML
    private ImageView ProfileImageID;

    @FXML
    private Label PhoneID;

    @FXML
    private Label NameID;

    @FXML
    private Label GenderID;

    @FXML
    private Label CountryID;

    @FXML
    private Label DateOfBirthID;

    @FXML
    private Label EmailID;

    @FXML
    private Button choosephotobtn;

    @FXML
    void OnClickChangePhoto(ActionEvent event) {
        //wait to update db
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile.getAbsoluteFile());
                ProfileImageID.setImage(new Image(fileInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //update user object
        ModelsFactory modelsFactory =ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        currentUserModel.setImage(ProfileImageID.getImage());

        //update db

        //int affectedRows = userDao.updateUserPicture(PhoneID.getText() ,ProfileImageID.getImage())



    }

    @FXML
    void OnClickUpdateProfile(ActionEvent event) {

        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToUpdateProfilePage();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //still wait to bind the photo
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE ;

        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            @Override
            public LocalDate fromString(String string) {
                return string == null || string.isEmpty() ? null : LocalDate.parse(string, formatter);
            }
            @Override
            public String toString(LocalDate date) {
                return date == null ? null : formatter.format(date);
            }
        };

        ModelsFactory modelsFactory =ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        PhoneID.textProperty().bind(currentUserModel.phoneNumberProperty());
        NameID.textProperty().bind(currentUserModel.usernameProperty());
        EmailID.textProperty().bind(currentUserModel.emailProperty());
        GenderID.textProperty().bind(currentUserModel.genderProperty());
        CountryID.textProperty().bind(currentUserModel.countryProperty());
        BioID.textProperty().bind(currentUserModel.bioProperty());
        //birthdate can't be bind bidirectional
        DateOfBirthID.textProperty().bindBidirectional(currentUserModel.dateProperty() , converter);
        //must set the image by default image if it return null in the login
       // ProfileImageID.imageProperty().bind(currentUserModel.imageProperty());


    }

}

