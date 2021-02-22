package iti.jets.gfive.ui.helpers;

import com.jfoenix.controls.JFXListView;
import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.GroupChatInter;
import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.services.GroupChatService;
import iti.jets.gfive.ui.controllers.ChatMessageController;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.ui.controllers.ContactController;
import iti.jets.gfive.ui.controllers.GroupMessageController;
import iti.jets.gfive.ui.controllers.NotificationViewController;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;

import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;

public class NotificationMsgHandler extends UnicastRemoteObject implements NotificationReceiveInter {
    private static NotificationMsgHandler notificationsLabel = null;
    private Label notificationLabelId;
    //private JFXListView<BorderPane> notificationsListId = NotificationsDialogController.getNotificationsListId();
    private JFXListView<BorderPane> notificationsListId = new JFXListView<>();
    private ListView<AnchorPane> listView;
    private Button newButton;
    private ListView<BorderPane> contactsList;
    private BorderPane borderPane;
    private Label name;
    private Label number;
    //private  Label newLabel;
    Label label = new Label("new");


    private NotificationMsgHandler() throws RemoteException {
        super();
    }

    public synchronized static NotificationMsgHandler getInstance() {
        if (notificationsLabel == null) {
            try {
                //System.out.println("creating new obj ");
                return notificationsLabel = new NotificationMsgHandler();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("getting the obj");
        return notificationsLabel;
    }

    public void setNotificationLabel(Label notificationLabelId) {
        this.notificationLabelId = notificationLabelId;
    }

    //    public void setNotificationsListId(JFXListView notificationsListId){
//        this.notificationsListId = notificationsListId;
//        System.out.println("notificationsListId is set to: " + notificationsListId);
    public void setListView(ListView<AnchorPane> list) {
        listView = list;
    }

    public void setContactList(ListView<BorderPane> list) {
        contactsList = list;
    }

    public void setChatarea(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setButton(Button b) {

        newButton = b;
    }

    public void setname(Label name) {
        this.name = name;
    }

    public void setnumber(Label number) {
        this.number = number;
    }

    //    public void setnewLabel(Label newLabel)
//    {
//        this.newLabel = newLabel;
//    }
    public Label getnewLabel() {
        return this.label;
    }
//    public void increaseNotificationsNumber(){
////        int notificationsNumber = Integer.parseInt(this.notificationLabelId.getText());
////        notificationsNumber+=1;
////        System.out.println(notificationsNumber);
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                int notificationsNumber = Integer.parseInt(notificationLabelId.getText());
//                notificationsNumber+=1;
//                notificationLabelId.setText(notificationsNumber+"");
//            }
//        });
//    }

    public void increaseNotificationsNumber() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int notificationsNumber = Integer.parseInt(notificationLabelId.getText());
                notificationsNumber += 1;
                notificationLabelId.setText(notificationsNumber + "");
            }
        });
    }

    //todo must make another fxml to receive a message with photo in the leftside
    @Override
    public void receiveMsg(MessageDto messageDto) throws RemoteException {
        List<MessageDto> messageList = new ArrayList<>();
        label = new Label("new");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                ObservableList<BorderPane> list = contactsList.getItems();


                System.out.println("inside the condition  " + number.getText() + "  " + messageDto.getSenderNumber());
                if (borderPane.isVisible() && number.getText().equals(messageDto.getSenderNumber())) {

                    try {

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();
                        ChatMessageController controller = fxmlLoader.getController();
                        System.out.println("content of the message " + messageDto.getContent());
                        //el mafrod akhod el senderid w aro7 ageb sorto
                        controller.msgLabelId.setText(messageDto.getContent());

                        System.out.println("meesage  " + messageDto.getContent());
                        listView.getItems().add(anchorPane);
                        listView.scrollTo(anchorPane);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("inside else");
                    for (BorderPane item : list) {
                        VBox vBox = (VBox) item.getCenter();
                        HBox hbox = (HBox) vBox.getChildren().get(0);
                        Label senderName = (Label) hbox.getChildren().get(0);

                        Label receiverNumber = (Label) vBox.getChildren().get(1);
                        if (receiverNumber.getText().equals(messageDto.getSenderNumber())) {
                            System.out.println("inside the match buttton");
                            item.setRight(label);
                            label.setStyle("-fx-background-color: green;");
                            label.setVisible(true);

                            break;
                        }


                    }
                }
            }
        });

        //todo still won't work with the method only by making the attribute public!


    }

    public void decreaseNotificationsNumber() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int notificationsNumber = Integer.parseInt(notificationLabelId.getText());
                if (notificationsNumber == 0) return;
                notificationsNumber -= 1;
                notificationLabelId.setText(notificationsNumber + "");
            }
        });
    }

    public void receive(NotificationDto notification) throws RemoteException {
        increaseNotificationsNumber();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/NotificationView.fxml"));
        try {
            BorderPane item = fxmlLoader.load();
            NotificationViewController controller = fxmlLoader.getController();
            controller.notificationContentId.setText(notification.getContent());
            controller.senderIdLabel.setText(notification.getSenderId());
            controller.notificationId = notification.getId();
            notificationsListId.getItems().add(item);
            //System.out.println(notificationsListId.getItems().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateContactsList() throws RemoteException {
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        ContactDBCrudInter contactDBCrudInter = ContactDBCrudService.getContactService();
        ArrayList<UserDto> contacts;
        try {
            contacts = contactDBCrudInter.getContactsList(currentUserModel.getPhoneNumber());
            ContactsListView c = ContactsListView.getInstance();
            c.fillContacts(contacts);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public JFXListView<BorderPane> getNotificationsToFill() {
        return this.notificationsListId;
    }

    public void addNotifications(ArrayList<NotificationDto> notifications) {
        //System.out.println(notifications.size() + "<-------------1");
        for (NotificationDto notification : notifications) {
            increaseNotificationsNumber();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/NotificationView.fxml"));
            try {
                BorderPane item = fxmlLoader.load();
                NotificationViewController controller = fxmlLoader.getController();
                //System.out.println(notification.getContent() + "<-------------2");
                controller.notificationContentId.setText(notification.getContent());
                controller.senderIdLabel.setText(notification.getSenderId());
                controller.notificationId = notification.getId();
                notificationsListId.getItems().add(item);
                //System.out.println(notificationsListId.getItems().size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addGroupInMembersList(String groupname) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("inside server");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));
                try {
                    BorderPane borderPane = fxmlLoader.load();
                    VBox vBox2 = (VBox) borderPane.getCenter();
                    HBox hbox1 = (HBox) vBox2.getChildren().get(0);
                    Label label = (Label) hbox1.getChildren().get(0);
                    label.setText(groupname);
                    contactsList.getItems().add(borderPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void receiveGroupMessage(String id, String message, String name) throws RemoteException {
        Label label = new Label("new");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                ObservableList<BorderPane> list = contactsList.getItems();


                System.out.println("inside the condition  ");
                if (borderPane.isVisible() && number.getText().equals(id)) {

                    try {
                        GroupChatInter groupChatInter = GroupChatService.getGroupChatInter();

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/GroupMessageView.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();
                        GroupMessageController controller = fxmlLoader.getController();
                        System.out.println("content of the message " + message);
                        controller.senderName.setText(name);
                        controller.msgLabelId.setText(message);
                        listView.getItems().add(anchorPane);
                        listView.scrollTo(anchorPane);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("inside else");
                    for (BorderPane item : list) {
                        VBox vBox = (VBox) item.getCenter();
                        HBox hbox = (HBox) vBox.getChildren().get(0);
                        Label senderName = (Label) hbox.getChildren().get(0);

                        Label receiverNumber = (Label) vBox.getChildren().get(1);
                        if (receiverNumber.getText().equals(id)) {
                            System.out.println("inside the match buttton");

                            label.setStyle("-fx-background-color: green;");


                            item.setRight(label);
                            label.setVisible(true);

                            break;
                        }


                    }
                }
            }
        });


        //todo still won't work with the method only by making the attribute public!

    }
}
