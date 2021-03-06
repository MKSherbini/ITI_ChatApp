package iti.jets.gfive.ui.controllers;

import com.mysql.cj.jdbc.Blob;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ClientConnectionService;
import iti.jets.gfive.services.UserDBCrudService;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.rmi.RemoteException;
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
    void OnClickChangePhoto(ActionEvent event) throws FileNotFoundException {
        //todo check the edit button, doesn't work with the picture!!
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

        //update user db

        UserDto user = new UserDto(PhoneID.getText(), ProfileImageID.getImage());

        UserDBCrudInter userServices = UserDBCrudService.getUserService();

        try {
            //System.out.println("imaaage " +user.getImage());
            //System.out.println("imaaage " +user.getPhoneNumber());
            int rowsAffected = userServices.updateUserPhoto(user);
            if (rowsAffected == 0) return;
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }
        //update userobject
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        currentUserModel.setImage(ProfileImageID.getImage());

        UserDto userPic = new UserDto();
        userPic.setPhoneNumber(currentUserModel.getPhoneNumber());
        userPic.setUsername(currentUserModel.getUsername());
        System.out.println(currentUserModel.getImage() + " UPDATE PIC USER");
        userPic.setImage(currentUserModel.getImage());
        userPic.setStatus(currentUserModel.getStatus());
        ClientConnectionInter cci = ClientConnectionService.getClientConnService();
        try {
            cci.publishPicture(userPic);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void OnClickUpdateProfile(ActionEvent event) {

        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToUpdateProfilePage();

    }

    @FXML
    void onClickBack(ActionEvent event) {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToMainPage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final Rectangle clip = new Rectangle(ProfileImageID.getFitWidth(), ProfileImageID.getFitHeight());
        clip.setArcWidth(180);
        clip.setArcHeight(180);
        ProfileImageID.setSmooth(true);
//        ProfileImageID.setPreserveRatio(true);
        ProfileImageID.setClip(clip);
//        ProfileImageID.
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

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

        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        PhoneID.textProperty().bind(currentUserModel.phoneNumberProperty());
        NameID.textProperty().bind(currentUserModel.usernameProperty());
        EmailID.textProperty().bind(currentUserModel.emailProperty());
        GenderID.textProperty().bind(currentUserModel.genderProperty());
        CountryID.textProperty().bind(currentUserModel.countryProperty());
        BioID.textProperty().bind(currentUserModel.bioProperty());
        DateOfBirthID.textProperty().bindBidirectional(currentUserModel.dateProperty(), converter);
        ProfileImageID.imageProperty().bindBidirectional(currentUserModel.imageProperty());

    }

}


