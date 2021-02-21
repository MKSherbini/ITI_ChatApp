package iti.jets.gfive.ui.helpers;

import com.jfoenix.controls.JFXListView;
import iti.jets.gfive.common.models.GroupDto;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.ui.controllers.ContactController;
import iti.jets.gfive.ui.controllers.RegisterController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
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
        this.contactsListViewId = contactsListViewId;
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
                        controller.contactImg.setImage(contact.getImage());
                      //  controller.addBtnID.setVisible(true);
                        //System.out.println(item.getChildren().get(1).toString() + " chh");
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
                       Image groupchat = new Image(ContactsListView.class.getResource("/iti/jets/gfive/images/groupchat.png").toString());
                        controller.contactImg.setImage(groupchat);
                        //  controller.addBtnID.setVisible(true);
                        //System.out.println(item.getChildren().get(1).toString() + " chh");
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
            v = (VBox) contactsListViewId.getItems().get(i).getChildren().get(1);
            phoneNumber = (Label) v.getChildren().get(1);
            if(phoneNumber.getText().equals(contactNumber)){
                System.out.println(phoneNumber.getText() + "phone number value");
                return true;
            }
        }
        return false;
    }



}
