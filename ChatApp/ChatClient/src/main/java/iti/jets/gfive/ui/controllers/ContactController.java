package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactController implements Initializable {
    @FXML
    private BorderPane contactComponent;

    @FXML
    private ImageView contactImg;

    @FXML
    public Label contactNameLabel;
    @FXML
    public Label contactNumberLabel;
    @FXML
    public Button newButton;
    @FXML
    public Label newLabelID;

    public ContactController(){}

    public void setLabelValue(String name){
        //contactNameLabel = new Label();
        //System.out.println("name in cons: " + name);
        contactNameLabel.setText(name);
    }
    public String getLabelValue(){
        return contactNameLabel.getText();
    }

    public BorderPane getBorderPane(){
        return this.contactComponent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //contactNameLabel = new Label();
        NotificationMsgHandler n = NotificationMsgHandler.getInstance();

    }

}
