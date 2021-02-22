package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


import java.net.URL;
import java.util.ResourceBundle;

public class ContactController implements Initializable {
    @FXML
    public
    ImageView ivStatus;
    @FXML
    private BorderPane contactComponent;

    @FXML
    public ImageView contactImg;

    @FXML
    public Label contactNameLabel;
    @FXML
    public Label contactNumberLabel;
    @FXML
    public Label lblStatus;
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
        // this to make the image view like circle
        final Rectangle clip = new Rectangle(60, 60);
        clip.setArcWidth(180);
        clip.setArcHeight(180);
        contactImg.setClip(clip);
        NotificationMsgHandler n = NotificationMsgHandler.getInstance();

    }

}
