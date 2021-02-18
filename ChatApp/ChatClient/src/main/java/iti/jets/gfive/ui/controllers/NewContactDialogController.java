package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.NotificationCrudInter;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.common.models.NotifDBRecord;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.services.NotificationDBCrudService;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.validation.FieldIconBinder;
import iti.jets.gfive.ui.helpers.validation.Validator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ResourceBundle;

public class NewContactDialogController implements Initializable {

    @FXML
    public Button btnNew;
    @FXML
    public JFXListView listView;
    @FXML
    public JFXTextField txtPhoneNumber;
    @FXML
    private FontIcon icon_loginPhone;
    @FXML
    public Button btnCancel;
    @FXML
    public Button btnAdd;
    ObservableList<String> phones;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FieldIconBinder iconBinder = FieldIconBinder.getInstance();
        iconBinder.bind(txtPhoneNumber, icon_loginPhone);

        Validator validator = Validator.getInstance();
        validator.buildPhoneContactValidation(txtPhoneNumber); // todo check this

        phones = FXCollections.observableArrayList();
        listView.getItems().addAll(phones);

    }

    public void performCancel(ActionEvent actionEvent) {
//        Platform.exit();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void performAddContact(ActionEvent actionEvent) {
        for (Object phoneNumber : listView.getItems()) {
            addingContacts(phoneNumber.toString());
        }
        listView.getItems().clear();
    }

    public void addingContacts(String contactNum){
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        if(contactNum.equals(currentUserModel.getPhoneNumber())){
            System.out.println("Are you trying to add yourself!!");
            return;
        }
        UserDBCrudInter userServices = UserDBCrudService.getUserService();
        boolean registered = true;
        try {
            registered = userServices.checkUserId(contactNum);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(!registered){
            System.out.println("user doesn't exists, registered: " + registered);
            return;
        }
        //todo dialog or validation: user already exists in the contacts list
        ContactsListView c = ContactsListView.getInstance();
        boolean contactExist = c.contactExist(contactNum);
        if(contactExist){
            System.out.println("Contact already exists in your contacts list");
            return;
        }
        NotificationCrudInter notificationServices = NotificationDBCrudService.getNotificationService();
        try {
            boolean notificationExists = notificationServices.pendingNotification(currentUserModel.getPhoneNumber(),
                    contactNum);
            //todo dialog or validation: pending notification (a notification already exist)
            if(notificationExists){
                System.out.println("pending notification");
                return;
            }
            String notificationContent = (currentUserModel.getUsername() +" with the phone number "
                    +currentUserModel.getPhoneNumber() +" is trying to add you");
            long millis=System.currentTimeMillis();
            Date currentDate = new Date(millis);
            NotifDBRecord notifRecord = notificationServices.insertNotification(notificationContent,
                    currentUserModel.getPhoneNumber(), currentDate, false, contactNum);
            System.out.println("rows affected after notification: " + notifRecord.getRowsAffected());
            if(notifRecord.getRowsAffected() <= 1) return;
            //todo tell the server to go and increase the label
            NotificationDto notif = new NotificationDto(notifRecord.getNotifId(), notificationContent, currentUserModel.getPhoneNumber(),
                    currentDate, false, contactNum);
            notificationServices.sendNotification(notif);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void performNewContact(ActionEvent actionEvent) {
        boolean validField= validateFields();
        if (!validField) return;
       listView.getItems().add(txtPhoneNumber.getText());
       txtPhoneNumber.setText("");
    }
    public boolean validateFields() {
        return txtPhoneNumber.validate();
    }
}
