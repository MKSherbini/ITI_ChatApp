package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ClientConnectionService;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;


import java.awt.event.ActionEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ContactController implements Initializable {
    @FXML
    private BorderPane contactComponent;

    @FXML
    public ImageView contactImg;

    @FXML
    public Label contactNameLabel;
    @FXML
    public Label contactNumberLabel;
    @FXML
    public Button newButton;
    @FXML
    public Label newLabelID;
    @FXML
    public JFXButton deleteBtnID;
    @FXML
    public JFXButton addBtnID;
    static public List<String> groupChatMembers = new ArrayList<>();
    ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();

    @FXML
    void onClickRemoveContact() {
        addBtnID.setDisable(false);
        deleteBtnID.setDisable(true);
        System.out.println("remove contact from a group");
        System.out.println("size of list "+groupChatMembers.size());
        groupChatMembers.remove(contactNumberLabel.getText());

//        UserDto userDto = new UserDto();
//        userDto.setUsername(contactNameLabel.getText());
//        userDto.setPhoneNumber(contactNumberLabel.getText());
//        try {
//            clientConnectionInter.RemoveMemeberFromChatGroup(contactNumberLabel.getText());
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    @FXML
    void onClickAddContatc() {
        addBtnID.setDisable(true);
        deleteBtnID.setDisable(false);
        groupChatMembers.add(contactNumberLabel.getText());
        System.out.println("size of list "+groupChatMembers.size());
        System.out.println("inside add contact button ");
        System.out.println("name-->" + contactNameLabel.getText());
        System.out.println("number--->" + contactNumberLabel.getText());
//        UserDto userDto = new UserDto();
//        userDto.setUsername(contactNameLabel.getText());
//        userDto.setPhoneNumber(contactNumberLabel.getText());
//        try {
//            clientConnectionInter.addMemberToGroupChat(contactNumberLabel.getText());
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

//        groupChatMembers.add(userDto);
//        System.out.println(groupChatMembers.size());

    }

    public ContactController() {
    }

    public void setLabelValue(String name) {
        //contactNameLabel = new Label();
        //System.out.println("name in cons: " + name);
        contactNameLabel.setText(name);
    }

    public String getLabelValue() {
        return contactNameLabel.getText();
    }

    public BorderPane getBorderPane() {
        return this.contactComponent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //contactNameLabel = new Label();
        NotificationMsgHandler n = NotificationMsgHandler.getInstance();

    }

    public void setButtonsVisibilty() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                addBtnID.setVisible(true);
                deleteBtnID.setVisible(true);
            }
        });

    }

}
