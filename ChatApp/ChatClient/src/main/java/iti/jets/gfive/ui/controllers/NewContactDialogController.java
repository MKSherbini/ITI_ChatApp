package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewContactDialogController implements Initializable {

    @FXML
    public Button btnNew;
    @FXML
    public JFXListView listView;
    @FXML
    public JFXTextField txtPhoneNumber;
    @FXML
    public Button btnCancel;
    @FXML
    public Button btnAdd;
    ObservableList<String> phones;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phones = FXCollections.observableArrayList();
        listView.getItems().addAll(phones);

    }

    public void performCancel(ActionEvent actionEvent) {
//        Platform.exit();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void performAddContact(ActionEvent actionEvent) {
    }

    public void performNewContact(ActionEvent actionEvent) {
       listView.getItems().add(txtPhoneNumber.getText());
       txtPhoneNumber.setText("");
    }

}
