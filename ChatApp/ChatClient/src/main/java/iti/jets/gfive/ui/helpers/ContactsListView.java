package iti.jets.gfive.ui.helpers;

import com.jfoenix.controls.JFXListView;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.ui.controllers.ContactController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ContactsListView {
    private static ContactsListView contactsListView;
    private JFXListView<BorderPane> contactsListViewId;
    FXMLLoader fxmlLoader;

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
    public void changeContactStatus(UserDto user){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                contactsListViewId.getItems().forEach(item->{
                    VBox v= (VBox) item.getChildren().get(1);
                    Label lblPhone = (Label) v.getChildren().get(1);
                    if(lblPhone.getText().equals(user.getPhoneNumber())){
                        Label statusLabel= (Label)item.getChildren().get(2);
                        statusLabel.setText(user.getStatus());
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
//                    ContactController contactController = new ContactController();
//                    contactController.setLabelValue(contact.getUsername());
                    //System.out.println(contactController.getLabelValue());
                    fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));
                    try {
                        BorderPane item = fxmlLoader.load();
                        ContactController controller = fxmlLoader.getController();
                        //todo still won't work with the method only by making the attribute public!
                        //controller.setLabelValue(contact.getUsername());
                        controller.contactNameLabel.setText(contact.getUsername());
                        //System.out.println(item.getChildren().get(1).toString() + " chh");
                        controller.contactNumberLabel.setText(contact.getPhoneNumber());
                        controller.lblStatus.setText(contact.getStatus());
                        //System.out.println(item.getChildren().get(1).toString() + " chh");
                        contactsListViewId.getItems().add(item);

                        System.out.println(item.getChildren().get(0).toString() + " <------- borderPane");
                        System.out.println(contact.getStatus() + " <------- staus"+ contact.getUsername());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}
