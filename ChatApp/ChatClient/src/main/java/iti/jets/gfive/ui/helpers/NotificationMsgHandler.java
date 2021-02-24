package iti.jets.gfive.ui.helpers;

import com.jfoenix.controls.JFXListView;
import iti.jets.gfive.AIML.BotsManager;
import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.GroupChatInter;
import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.GroupMessagesDto;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.ui.controllers.*;
import iti.jets.gfive.services.ClientConnectionService;
import iti.jets.gfive.services.GroupChatService;
import iti.jets.gfive.services.MessageDBService;
import iti.jets.gfive.ui.controllers.ChatMessageController;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.ui.controllers.GroupMessageController;
import iti.jets.gfive.ui.controllers.MainScreenController;
import iti.jets.gfive.ui.controllers.NotificationViewController;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;

import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;

public class NotificationMsgHandler extends UnicastRemoteObject implements NotificationReceiveInter {
    transient private static NotificationMsgHandler notificationsLabel = null;
    transient private Label notificationLabelId;
    //private JFXListView<BorderPane> notificationsListId = NotificationsDialogController.getNotificationsListId();
    transient private JFXListView<BorderPane> notificationsListId = new JFXListView<>();
    transient private ListView<AnchorPane> listView;
    transient private Button newButton;
    transient private ListView<BorderPane> contactsList;
    transient private BorderPane borderPane;
    transient private Label name;
    transient private Label number;
    transient ArrayList<UserDto> contacts;
    //private  Label newLabel;
    transient Label label = new Label("new");
    transient private Label receivernumberID;


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

    public Label getnewLabel() {
        return this.label;
    }

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

                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        handleBotMessage(messageDto);
                    } catch (RemoteException e) {
                        e.printStackTrace();
//            StageCoordinator.getInstance().reset();
                    }
                }).start();
            }
        });

        //todo still won't work with the method only by making the attribute public!


    }

    @Override
    public void receiveFile(MessageDto messageDto) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<BorderPane> list = contactsList.getItems();
                //System.out.println("inside the condition  " + number.getText() + "  " + messageDto.getSenderNumber());
                if (borderPane.isVisible() && number.getText().equals(messageDto.getSenderNumber())) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/FileMessageView.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();
                        FileMessageController controller = fxmlLoader.getController();
                        controller.fileNameLabelId.setText(messageDto.getMessageName());
                        controller.recordID.setText(String.valueOf(messageDto.getId()));
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
                            // newLabel.setVisible(true);
                            label.setStyle("-fx-background-color: green;");
                            item.setRight(label);
                            label.setVisible(true);
                            break;
                        }
                    }
                }
            }
        });
    }

    void handleBotMessage(MessageDto messageDto) throws RemoteException {
        if (!(messageDto.getMessageName().equals("text") ||
                messageDto.getMessageName().equals("messagename"))) return;
        var activeBot = ContactDBCrudService.getContactService().checkActiveChatBot(
                messageDto.getSenderNumber(),
                messageDto.getReceiverNumber());
        System.out.printf("activeBot? %s from %s to %s%n", activeBot, messageDto.getReceiverNumber(), messageDto.getSenderNumber());
        if (!activeBot) return;
        var chatBot = BotsManager.getInstance();
        var reply = chatBot.askBots(messageDto.getContent());
        var replyDTO = new MessageDto();
        replyDTO.setContent(reply);
        replyDTO.setMessageName("text");
        replyDTO.setSenderNumber(messageDto.getReceiverNumber());
        replyDTO.setReceiverNumber(messageDto.getSenderNumber());

        ClientConnectionService.getClientConnService().sendMsg(replyDTO);
        MessageDBService.getMessageService().insertMessage(replyDTO);

        System.out.println("bot reply to " + messageDto + " is " + replyDTO);
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatMessageController controller = fxmlLoader.getController();
            controller.msgLabelId.setText(replyDTO.getContent());
            controller.msgLabelId.setAlignment(Pos.CENTER_RIGHT);
            controller.msgLabelId.setStyle("-fx-background-color: #ABC8E2;");
            listView.getItems().add(anchorPane);
            listView.scrollTo(anchorPane);
        });


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
        if (notification.getSenderId().equals("01000000000")) {
            Platform.runLater(() -> {
                AnnouncementLoader.getInstance().showDialog(notification.getContent());

            });

            System.out.println(notification.getContent());
            return;
        }
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
    public void updateContactsList(UserDto contactInfo) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));
                try {
                    BorderPane item = fxmlLoader.load();
                    ContactController controller = fxmlLoader.getController();
                    controller.contactNameLabel.setText(contactInfo.getUsername());
                    controller.contactNumberLabel.setText(contactInfo.getPhoneNumber());
                    controller.ivStatus.setImage(new Image(getClass().getResource(String.format(MainScreenController.URL_RESOURCE, contactInfo.getStatus())).toString()));
                    controller.contactImg.setImage(contactInfo.getImage());
                    contactsList.getItems().add(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateStatus(UserDto user) throws RemoteException {
//        ContactsListView con = ContactsListView.getInstance();
//        con.setContactsListViewId();
        System.out.println(user.getPhoneNumber() + "-----------> " + user.getStatus());

        ContactsListView c = ContactsListView.getInstance();
        c.changeContactStatus(user);
        StageCoordinator.getInstance().changeStatus(user);
//        c.fillContacts(contacts);

    }

    @Override
    public void updatePicture(UserDto user) throws RemoteException {
        System.out.println(user.getPhoneNumber() + "-----------> " + user.getStatus());
        ContactsListView c = ContactsListView.getInstance();
        c.changeContactPicture(user);
        StageCoordinator.getInstance().changeStatus(user);
    }

    public JFXListView<BorderPane> getNotificationsToFill() {
        return this.notificationsListId;
    }

    public void addNotifications(ArrayList<NotificationDto> notifications) {
        notificationsListId.getItems().clear(); // for the logout case
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
    public void addGroupInMembersList(String groupname, String id) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("inside server");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));
                try {
                    BorderPane borderPane = fxmlLoader.load();
                    ContactController controller = fxmlLoader.getController();
//                    ImageView imageView = (ImageView) borderPane.getLeft();
                    ImageView imageView = (ImageView) ((AnchorPane) borderPane.getLeft()).getChildren().get(0);
                    VBox vBox2 = (VBox) borderPane.getCenter();
                    HBox hbox1 = (HBox) vBox2.getChildren().get(0);
                    Label label = (Label) hbox1.getChildren().get(0);
                    Label groupid = (Label) vBox2.getChildren().get(1);
                    label.setText(groupname);
                    Image groupchat = new Image(NotificationMsgHandler.class.getResource("/iti/jets/gfive/images/groupchat.png").toString());
                    imageView.setImage(groupchat);
                    groupid.setText(id);
                    groupid.setVisible(false);
                    controller.stackID.setVisible(false);

                    contactsList.getItems().add(borderPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void receiveGroupMessage(String id, String message, String name, boolean file, String fileRecordId) throws RemoteException {
        Label label = new Label("new");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<BorderPane> list = contactsList.getItems();
                System.out.println("inside the condition  ");
                if (borderPane.isVisible() && number.getText().equals(id)) {
                    if (file) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/FileMessageView.fxml"));
                            AnchorPane anchorPane = fxmlLoader.load();
                            FileMessageController controller = fxmlLoader.getController();
                            controller.fileNameLabelId.setText(message);
                            controller.recordID.setText(fileRecordId);

                            listView.getItems().add(anchorPane);
                            listView.scrollTo(anchorPane);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
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
                    }
                } else {
                    System.out.println("inside else");
                    for (BorderPane item : list) {
                        VBox vBox = (VBox) item.getCenter();
                        HBox hbox = (HBox) vBox.getChildren().get(0);
                        Label senderName = (Label) hbox.getChildren().get(0);
                        Label receiverNumber = (Label) vBox.getChildren().get(1);
                        System.out.println("receiverNumber <>" + receiverNumber.getText());
                        System.out.println("id <>" + id);
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

    @Override
    public void receivePing() throws RemoteException {
        // doesn't have to do anything here, tameny 3alek bas :)
    }

    @Override
    public void receiveFileToGroup(GroupMessagesDto groupMessageDto) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<BorderPane> list = contactsList.getItems();
                //System.out.println("inside the condition  " + number.getText() + "  " + messageDto.getSenderNumber());
                if (borderPane.isVisible() && number.getText().equals(groupMessageDto.getSendernumber())) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/FileMessageView.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();
                        FileMessageController controller = fxmlLoader.getController();
                        controller.fileNameLabelId.setText(groupMessageDto.getMessage_name());
                        controller.recordID.setText(String.valueOf(groupMessageDto.getId()));
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
                        if (receiverNumber.getText().equals(groupMessageDto.getSendernumber())) {
                            System.out.println("inside the match buttton");
                            // newLabel.setVisible(true);
                            label.setStyle("-fx-background-color: green;");
                            item.setRight(label);
                            label.setVisible(true);
                            break;
                        }
                    }
                }
            }
        });
    }

    public void setReceivernumberID(Label receivernumberID) {
        this.receivernumberID = receivernumberID;
    }

    public String getReceivernumberID() {
        return this.receivernumberID.getText();
    }
}
