package iti.jets.gfive.ui.helpers;

import com.jfoenix.controls.JFXListView;
import iti.jets.gfive.common.models.GroupDto;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.ui.controllers.ContactController;
import iti.jets.gfive.ui.controllers.RegisterController;
import iti.jets.gfive.ui.controllers.MainScreenController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactsListView {
    private static ContactsListView contactsListView;
    private JFXListView<BorderPane> contactsListViewId;

    private ContactsListView(){}

    public synchronized static ContactsListView getInstance(){
        if(contactsListView == null){
            contactsListView = new ContactsListView();

        }
        return contactsListView;
    }

    public void setContactsListViewId(JFXListView contactsListViewId){
        System.out.println("setContactsListViewId.contactsListViewId = " + contactsListViewId);

        this.contactsListViewId = contactsListViewId;
    }

    public JFXListView<BorderPane> getContactsListViewId(){
        if(this.contactsListViewId == null) return null;
        else return this.contactsListViewId;
    }

    /*
        this method update the contact status in the contact list view
        when he changes it
     */
    public void changeContactStatus(UserDto user){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                contactsListViewId.getItems().forEach(item->{

                    VBox v= (VBox) item.getChildren().get(0);
                    Label lblPhone = (Label) v.getChildren().get(1);
                    if(lblPhone.getText().equals(user.getPhoneNumber())){
                       StackPane stackPane = (StackPane) ((AnchorPane) item.getChildren().get(1)).getChildren().get(1);
                       ((ImageView)(stackPane.getChildren().get(1))).setImage(new Image(getClass().getResource(String.format(MainScreenController.URL_RESOURCE,user.getStatus())).toString()));
                    }
                });
            }
        });

    }

    public void changeContactPicture(UserDto user){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                contactsListViewId.getItems().forEach(item->{
                    VBox v= (VBox) item.getChildren().get(0);
                    Label lblPhone = (Label) v.getChildren().get(1);
                    if(lblPhone.getText().equals(user.getPhoneNumber())){
                        AnchorPane anchorPane= (AnchorPane) item.getLeft();
                        ImageView img = (ImageView)  anchorPane.getChildren().get(0);
                        img.setImage(user.getImage());
                    }
                });
            }
        });

    }

    public void fillContacts(ArrayList<UserDto> contacts){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    contactsListViewId.getItems().clear();
                for (UserDto contact : contacts) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));
                    try {
                        BorderPane item = fxmlLoader.load();
                        ContactController controller = fxmlLoader.getController();

                        //todo still won't work with the method only by making the attribute public!
                        //controller.setLabelValue(contact.getUsername());
                        controller.contactNameLabel.setText(contact.getUsername());
                        controller.contactNumberLabel.setText(contact.getPhoneNumber());
                       // if(controller.contactNumberLabel.getText().equals())
                        controller.ivStatus.setImage(new Image(getClass().getResource(String.format(MainScreenController.URL_RESOURCE,contact.getStatus())).toString()));
//                        controller.lblStatus.setText(contact.getStatus());
                        controller.contactImg.setImage(contact.getImage());
                        contactsListViewId.getItems().add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ////////////
            }
        });


    }

    public void fillGroups(List<GroupDto> groups){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (GroupDto group : groups) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));
                    try {
                        BorderPane item = fxmlLoader.load();
                        ContactController controller = fxmlLoader.getController();

                        //todo still won't work with the method only by making the attribute public!
                        //controller.setLabelValue(contact.getUsername());
                        controller.contactNameLabel.setText(group.getGroupname());
                        controller.contactNumberLabel.setText(String.valueOf(group.getId()));
                        controller.contactNumberLabel.setVisible(false);
                        controller.stackID.setVisible(false);
                       Image groupchat = new Image(ContactsListView.class.getResource("/iti/jets/gfive/images/groupchat.png").toString());
                        controller.contactImg.setImage(groupchat);
                        contactsListViewId.getItems().add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public boolean contactExist(String contactNumber){
        VBox v;
        Label phoneNumber;
        for(int i = 0; i < contactsListViewId.getItems().size(); i++){
            v = (VBox) contactsListViewId.getItems().get(i).getChildren().get(0);
            phoneNumber = (Label) v.getChildren().get(1);
            if(phoneNumber.getText().equals(contactNumber)){
                System.out.println(phoneNumber.getText() + "phone number value");
                return true;
            }
        }
        return false;
    }



}
