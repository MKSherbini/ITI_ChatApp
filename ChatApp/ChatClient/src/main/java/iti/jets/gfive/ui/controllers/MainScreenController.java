package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.GroupChatInter;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.common.models.GroupMessagesDto;
import iti.jets.gfive.services.*;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.services.MessageDBService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.serialization.Marshaltor;
import iti.jets.gfive.ui.models.CurrentUserModel;
import iti.jets.gfive.ui.models.chat.ChatModel;
import iti.jets.gfive.ui.models.chat.MessageModel;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import iti.jets.gfive.ui.helpers.LoginManager;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public static final String URL_RESOURCE = "/iti/jets/gfive/icons/%s.png";
    public static final String AWAY = "away";
    public static final String AVAILABLE = "available";
    public static final String OFFLINE = "offline";
    public static final String BUSY = "busy";
    @FXML
    public ImageView ibtnAddContct;
    @FXML
    public Button addGroupBtn;
    public ToggleSwitch botSwitchBtnId;
    @FXML
    private Button btnContextMenu;
    @FXML
    private JFXListView<BorderPane> contactsListViewId;
    @FXML
    private Label notificationLabelId;
    @FXML
    private TextField groupnameID;
    //    @FXML
//    private Label newLabelID;
    @FXML
    private Text CurrentUserNameID;

    @FXML
    private BorderPane chatAreaBorderPaneID;
    @FXML
    private Label receivernameID;
    @FXML
    private JFXButton sendBtnId;
    @FXML
    private TextArea msgTxtAreaID;
    @FXML
    private Label receivernumberID;
    @FXML
    private ListView<AnchorPane> chatListView;
    @FXML
    private ImageView ReceiverImgID;
    @FXML
    private ImageView profilepictureID;
    private Label receiverNumber;

    private Label newLabel;
    private boolean fileFlag = false;
    private ContextMenu contextMenu;
    private MenuItem miExit;
    private MenuItem miLogout, miAvailable, miBusy, miAway, miOffline;
    private Menu status;
    private Property<String> statusProperty = new SimpleObjectProperty<>("available");


    @FXML
    void showContxtMenu(MouseEvent event) {
        contextMenu.show(btnContextMenu.getParent(), event.getX(), event.getY());

    }

    // this method binds the status image property on the imageview status image property
    private void changeStatusUi() {
        statusProperty.bindBidirectional(ModelsFactory.getInstance().getCurrentUserModel().statusProperty());
        statusProperty.addListener((opserver, old, newval) -> ivStatus.setImage(new Image(getClass().getResource(String.format(URL_RESOURCE, newval)).toString())));
        ivStatus.setImage(new Image(getClass().getResource(String.format(URL_RESOURCE, ModelsFactory.getInstance().getCurrentUserModel().getStatus())).toString()));
    }

    @FXML
    private ImageView ivStatus;
    Property<Image> statusImage;

    // this method intialize the context menu and its items actions
    private void initializeContextMenu() {
        contextMenu = new ContextMenu();
        miExit = new MenuItem("Exit");
        miExit.setOnAction((actionEvent) -> {
            //todo fix the exit and the main  class
            LoginManager.getInstance().Exit();
            Platform.exit();
        });
        miLogout = new MenuItem("Logout");
        miLogout.setOnAction((acrionEvent) -> {
            LoginManager.getInstance().Logout();
            StageCoordinator.getInstance().switchToLoginPage();
        });
        status = new Menu("Status");
        miAvailable = new MenuItem("Available");
        miAvailable.setOnAction((action) -> applyMenUItemAction(AVAILABLE));
        miAvailable.setGraphic(getConfiguredImageView(new Image(getClass().getResource(String.format(URL_RESOURCE, AVAILABLE)).toString())));
        miBusy = new MenuItem("Busy");
        miBusy.setOnAction((action) -> applyMenUItemAction(BUSY));
        miBusy.setGraphic(getConfiguredImageView(new Image(getClass().getResource(String.format(URL_RESOURCE, BUSY)).toString())));
        miAway = new MenuItem("Away");
        miAway.setOnAction((action) -> applyMenUItemAction(AWAY));
        miAway.setGraphic(getConfiguredImageView(new Image(getClass().getResource(String.format(URL_RESOURCE, AWAY)).toString())));
        miOffline = new MenuItem("Offline");
        miOffline.setOnAction((action) -> applyMenUItemAction(OFFLINE));
        miOffline.setGraphic(getConfiguredImageView(new Image(getClass().getResource(String.format(URL_RESOURCE, OFFLINE)).toString())));
        status.getItems().add(miAvailable);
        status.getItems().add(miBusy);
        status.getItems().add(miAway);
        status.getItems().add(miOffline);
        contextMenu.getItems().addAll(status);
        contextMenu.getItems().addAll(miExit, miLogout);
    }


    // this method define the action of the status menu items to change the status on gui and on the db.
    private void applyMenUItemAction(String statusName) {
        ivStatus.setImage(new Image(getClass().getResource(String.format(URL_RESOURCE, statusName)).toString()));
        try {
            UserDto user = new UserDto(ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber(), statusName);
            user.setImage(ModelsFactory.getInstance().getCurrentUserModel().getImage());
            int rows = UserDBCrudService.getUserService().updateUserStatus(user);
            if (rows > 0) {
                ModelsFactory.getInstance().getCurrentUserModel().statusProperty().setValue(statusName);
                changeStatusUi();
                ivStatus.setImage(new Image(getClass().getResource(String.format(URL_RESOURCE, statusName)).toString()));
            }
            ClientConnectionService.getClientConnService().puplishStatus(user);

            System.out.println("status updated  : " + rows);
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        CurrentUserNameID.textProperty().bind(currentUserModel.usernameProperty());
//        CurrentUserNameID.setText(currentUserModel.getUsername());
        profilepictureID.imageProperty().bindBidirectional(currentUserModel.imageProperty());
        chatListView.scrollTo(chatListView.getItems().size() - 1);
        System.out.println(ModelsFactory.getInstance().getCurrentUserModel().getStatus());
        changeStatusUi();
        initializeContextMenu();
        ContactsListView c = ContactsListView.getInstance();
        c.setContactsListViewId(this.contactsListViewId);
        System.out.println("contactsListViewId = " + contactsListViewId);
        // make profile picture to be circled
        final Rectangle clip = new Rectangle(35, 35);
        clip.setArcWidth(180);
        clip.setArcHeight(180);
        profilepictureID.setClip(clip);


        NotificationMsgHandler n = NotificationMsgHandler.getInstance();
        n.setNotificationLabel(notificationLabelId);
        //System.out.println(notificationLabelId + "NotificationsLabel in Mainscreen client");
        n.setContactList(contactsListViewId);
        n.setListView(chatListView);
        n.setChatarea(chatAreaBorderPaneID);
        n.setname(receivernameID);
        n.setnumber(receivernumberID);
        newLabel = n.getnewLabel();
        n.setReceivernumberID(receivernumberID);
        //  n.setnewLabel(newLabelID);
        //n.setButton(contactsListViewId);

        // System.out.println(notificationLabelId + "NotificationsLabel in Mainscreen client");
        //System.out.println("notifaction label is initalizedddddd");
        //System.out.println(notificationLabelId + "NotificationsLabel in Mainscreen client");
        //NotificationMsgHandler n2 = NotificationMsgHandler.getInstance();
        //System.out.println("calling the get instance again in the client");


        // for the chat bot!!!
        // works more than it should but idgaf
        botSwitchBtnId.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ContactDBCrudService.getContactService().updateActiveChatBot(
                        receiverNumber.getText(),
                        ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber(),
                        newValue);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    // this method create new image view object and fit its width and height to 20 px .
    public ImageView getConfiguredImageView(Image image) {
        ImageView iv = new ImageView(image);
        iv.setFitWidth(20);
        iv.setFitHeight(20);
        return iv;
    }

    // this method register the context menu on the btnContextMenu .
    public void showContextMenu(MouseEvent event) {
        contextMenu.show(btnContextMenu, Side.BOTTOM, 0, 0);
    }

    // this method opens new contact dialog
    @FXML
    public void openAddNewContactDialog(MouseEvent mouseEvent) {
        openDialog("NewContactDialog");
    }

    @FXML
    public void onClickProfile(ActionEvent event) {
        System.out.println("Inside the onclick of the photo");
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToProfilePage();
    }

    @FXML
    public void openFriendRequestDialog(ActionEvent actionEvent) {
        openDialog("FriendRequestDialog");
    }

    private void openDialog(String viewName) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(String.format("/iti/jets/gfive/views/%s.fxml", viewName)));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.setMaximized(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            //todo when undecorated the window is no longer movable!
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void OpenNotificationDialog(MouseEvent mouseEvent) {
        notificationLabelId.setText("0");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/NotificationDialog.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.setMaximized(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //click on the contact to start chat with him/her
    //todo update main page with textaare instead of textfield in order to wrap long messages
    @FXML
    public void onClickonContact(MouseEvent mouseEvent) throws RemoteException {
        chatListView.getItems().clear();
        ObservableList<BorderPane> selectedContact;
        selectedContact = contactsListViewId.getSelectionModel().getSelectedItems();
        for (BorderPane borderPane : selectedContact) {
            //get the name and image of the selected contact
            VBox vBox = (VBox) borderPane.getCenter();
            HBox hbox = (HBox) vBox.getChildren().get(0);
            Label name = (Label) hbox.getChildren().get(0);
            receiverNumber = (Label) vBox.getChildren().get(1);
            ImageView receiverimage = (ImageView) ((AnchorPane) borderPane.getLeft()).getChildren().get(0);
            // System.out.println("-------------->" + receiverimage);
//            ImageView receiverimage = (ImageView) borderPane.getLeft();

            //to check if there is a new message or not
            if (borderPane.getRight() != null) {
                Label label = new Label(" ");
                borderPane.setRight(label);
                System.out.println("right of borderpane equals null");
            }

            // ImageView imageView =(ImageView) borderPane.getLeft();
            //    System.out.println("label text is " +name.getText());
            receivernameID.setText(name.getText());
            receivernumberID.setText(receiverNumber.getText());
            if (receivernumberID.getText().charAt(0) != '0') {
                receivernumberID.setVisible(false);
            }

            ReceiverImgID.setImage(receiverimage.getImage());

            chatAreaBorderPaneID.setVisible(true);
        }
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();

        MessageDBInter messageServices = MessageDBService.getMessageService();
        if (messageServices == null) return;
        if (receiverNumber == null) {
            return;
        }
        if (receivernumberID.getText().charAt(0) != '0') {
            GroupChatInter groupChatInter = GroupChatService.getGroupChatInter();
            List<GroupMessagesDto> groupMessagesDto = groupChatInter.selectAllMessages(receivernumberID.getText());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatListView.getItems().clear();
                    try {

                        //todo still won't work with the method only by making the attribute public!
                        for (GroupMessagesDto groupMessagesDto1 : groupMessagesDto) {
                            if(!groupMessagesDto1.getMessage_name().equals("text")){
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/FileMessageView.fxml"));
                                AnchorPane anchorPane = fxmlLoader.load();
                                FileMessageController controller = fxmlLoader.getController();
                                controller.fileNameLabelId.setText(groupMessagesDto1.getMessage_name());
                                controller.recordID.setText(String.valueOf(groupMessagesDto1.getId()));
                                if (currentUserModel.getPhoneNumber().equals(groupMessagesDto1.getSendernumber())) {
                                    controller.fileNameLabelId.setAlignment(Pos.CENTER_RIGHT);
                                    controller.fileNameLabelId.setStyle("-fx-background-color: #ABC8E2;");
                                }
                                msgTxtAreaID.setText("");
                                chatListView.getItems().add(anchorPane);
                            } else {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/GroupMessageView.fxml"));
                                AnchorPane anchorPane = fxmlLoader.load();
                                GroupMessageController controller = fxmlLoader.getController();

                                //   System.out.println("content of the message "+messageDto.getContent());
                                controller.msgLabelId.setText(groupMessagesDto1.getMessage());
                                controller.senderName.setText(groupChatInter.getUserName(groupMessagesDto1.getSendernumber()));
                                if (currentUserModel.getPhoneNumber().equals(groupMessagesDto1.getSendernumber())) {
                                    controller.msgLabelId.setAlignment(Pos.CENTER_RIGHT);
                                    controller.msgLabelId.setStyle("-fx-background-color: #ABC8E2;");
                                }

                                msgTxtAreaID.setText("");
                                chatListView.getItems().add(anchorPane);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {

            final List<MessageDto> messageList = messageServices.selectAllMessages(receiverNumber.getText(), currentUserModel.getPhoneNumber());

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatListView.getItems().clear();

                    try {

                        //todo still won't work with the method only by making the attribute public!
                        for (MessageDto messageDto : messageList) {

                            if (!messageDto.getMessageName().equals("text")) {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/FileMessageView.fxml"));
                                AnchorPane anchorPane = fxmlLoader.load();
                                FileMessageController controller = fxmlLoader.getController();
                                controller.fileNameLabelId.setText(messageDto.getMessageName());
                                //controller.fileNameLabelId.setAlignment(Pos.CENTER_RIGHT);
                                controller.recordID.setText(String.valueOf(messageDto.getId()));
                                if (currentUserModel.getPhoneNumber().equals(messageDto.getSenderNumber())) {
                                    controller.fileNameLabelId.setAlignment(Pos.CENTER_RIGHT);
                                    controller.fileNameLabelId.setStyle("-fx-background-color: #ABC8E2;");
                                }
                                msgTxtAreaID.setText("");
                                chatListView.getItems().add(anchorPane);

                            } else {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
                                AnchorPane anchorPane = fxmlLoader.load();
                                ChatMessageController controller = fxmlLoader.getController();

                                //   System.out.println("content of the message "+messageDto.getContent());
                                controller.msgLabelId.setText(messageDto.getContent());
                                if (currentUserModel.getPhoneNumber().equals(messageDto.getSenderNumber())) {
                                    controller.msgLabelId.setAlignment(Pos.CENTER_RIGHT);
                                    controller.msgLabelId.setStyle("-fx-background-color: #ABC8E2;");
                                }

                                msgTxtAreaID.setText("");
                                chatListView.getItems().add(anchorPane);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        chatListView.scrollTo(chatListView.getItems().size() - 1);

        // handle bot chat
        if (receivernumberID.getText().charAt(0) == '0') {
            botSwitchBtnId.setVisible(true);
            botSwitchBtnId.setSelected(
                    ContactDBCrudService.getContactService().checkActiveChatBot(
                            receiverNumber.getText(),
                            ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber()));
        } else {
            botSwitchBtnId.setVisible(false);
            botSwitchBtnId.setSelected(false);
        }
    }

    @FXML
    public void onClickSendButton(ActionEvent actionEvent) throws RemoteException {
        if (msgTxtAreaID.getText().equals("")) {
            return;
        }
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));

        if (receivernumberID.getText().charAt(0) != '0') {
            String message;
            try {
                BorderPane borderPane = fxmlLoader.load();
                ContactController contactController = fxmlLoader.getController();
                GroupChatInter groupChatInter = GroupChatService.getGroupChatInter();
                String groupid = receivernumberID.getText();
                List<String> memeber = groupChatInter.selectAllMemebers(groupid);
                //remove me from the list
                for (int i = 0; i < memeber.size(); i++) {
                    if (memeber.get(i).equals(currentUserModel.getPhoneNumber())) {
                        memeber.remove(i);
                    }
                }

                MessageDBInter messageServices = MessageDBService.getMessageService();

                message = msgTxtAreaID.getText();
                GroupMessagesDto groupMessagesDto = new GroupMessagesDto(groupid, message, currentUserModel.getPhoneNumber(), "text");
                groupChatInter.saveAllMessages(groupMessagesDto);

                ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
                clientConnectionInter.sendGroupMsg(memeber, groupid, message, currentUserModel.getUsername());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {


                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/GroupMessageView.fxml"));
                        try {
                            AnchorPane anchorPane = fxmlLoader.load();
                            GroupMessageController controller = fxmlLoader.getController();
                            //todo still won't work with the method only by making the attribute public!
                            //controller.setLabelValue(contact.getUsername());
                            controller.msgLabelId.setText(message);
                            controller.senderName.setText(currentUserModel.getUsername());

                            controller.msgLabelId.setAlignment(Pos.CENTER_RIGHT);
                            controller.msgLabelId.setStyle("-fx-background-color: #ABC8E2;");
                            //todo senderimg must update in it's chatarea
//                    controller.msgImgId.setImage(senderImag);

                            msgTxtAreaID.setText("");

                            chatListView.getItems().add(anchorPane);
                            chatListView.scrollTo(anchorPane);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("inside group chat");

        } else {


            MessageDBInter messageServices = MessageDBService.getMessageService();

            //todo must retreive the image of the sender to db and send it as paramter in sendMsg

            String messsage = msgTxtAreaID.getText();
            Date date = Date.valueOf(LocalDate.now());
            System.out.println("messagename" + currentUserModel.getPhoneNumber() + receiverNumber.getText() + "unseen" + messsage + date);
            MessageDto messageDto = new MessageDto(-1, "text", currentUserModel.getPhoneNumber(), receiverNumber.getText(), "unseen", messsage, date);
            try {
                ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
                clientConnectionInter.sendMsg(messageDto);

            } catch (RemoteException e) {
                e.printStackTrace();
                StageCoordinator.getInstance().reset();
            }

            int rowaffected = messageServices.insertMessage(messageDto);
            // System.out.println("row inserted equal "+rowaffected);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    UserDBCrudInter userServices = UserDBCrudService.getUserService();
                    Image senderImag = null;

                    //  senderImag = userServices.selectUserImage(currentUserModel.getImage());
                    senderImag = currentUserModel.getImage();


                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
                    try {
                        AnchorPane anchorPane = fxmlLoader.load();
                        ChatMessageController controller = fxmlLoader.getController();
                        //todo still won't work with the method only by making the attribute public!
                        //controller.setLabelValue(contact.getUsername());
                        controller.msgLabelId.setText(messsage);
                        //  controller.msgImgId.setImage(senderImag);


                        controller.msgLabelId.setAlignment(Pos.CENTER_RIGHT);
                        controller.msgLabelId.setStyle("-fx-background-color: #ABC8E2;");
                        //todo senderimg must update in it's chatarea
//                    controller.msgImgId.setImage(senderImag);

                        msgTxtAreaID.setText("");

                        chatListView.getItems().add(anchorPane);
                        chatListView.scrollTo(anchorPane);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public void onClickSaveChat(ActionEvent actionEvent) {
        if (receiverNumber.getText().length() == 0) return;
        CurrentUserModel currentUserModel = ModelsFactory.getInstance().getCurrentUserModel();
        MessageDBInter messageServices = MessageDBService.getMessageService();
        List<MessageDto> messageList = null;
        try {
            messageList = messageServices.selectAllMessages(receiverNumber.getText(), currentUserModel.getPhoneNumber());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (messageList == null) return;

//        Map<String, String> map = new HashMap<>();
//        map.put(receiverNumber.getText(), receivernameID.getText());
//        map.put(currentUserModel.getPhoneNumber(), currentUserModel.getUsername());

        ChatModel chatModel = new ChatModel();
        chatModel.setChatName(receivernameID.getText()); // for now it's the other guy
        chatModel.setChatOwner(currentUserModel.getUsername());
        messageList.forEach(messageDto -> {
            if (messageDto.getMessageName().equals("text")) {
                chatModel.getMessages().add(new MessageModel(messageDto.getSenderNumber(), messageDto.getReceiverNumber(), messageDto.getContent()));
            }
            chatModel.getMessages().add(new MessageModel(messageDto.getSenderNumber(), messageDto.getReceiverNumber(), messageDto.getMessageName()));
        });

        var m = Marshaltor.getInstance();
        m.marshalChat(chatModel);
    }

    public void onClickAttachFile(ActionEvent actionEvent) {
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        MessageDBInter messageServices = MessageDBService.getMessageService();
        ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            if ((selectedFile.length() / 1048576) > 10) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("File Size Error");
                a.setContentText("Cannot send more than a 10MB file");
                a.setHeaderText("Error: File is too big");
                a.show();
                return;
            }
            String filePath = selectedFile.getPath();
            File fileToSend = new File(filePath);
            try {
                FileInputStream fis = new FileInputStream(fileToSend);
                int fileLength = (int) fileToSend.length();
                byte[] fileData = new byte[fileLength];
                int c = fis.read(fileData);
                if(receivernumberID.getText().charAt(0) != '0'){
                    GroupMessagesDto fileGroupMessageDto = new GroupMessagesDto(receivernumberID.getText(), fileData, currentUserModel.getPhoneNumber(), fileToSend.getName());
                    GroupChatInter groupChatInter = GroupChatService.getGroupChatInter();
                    int fileRecordId = groupChatInter.saveAllMessages(fileGroupMessageDto);
                    fileGroupMessageDto.setId(String.valueOf(fileRecordId));
                    List<String> groupMembers = groupChatInter.selectAllMemebers(receivernumberID.getText());
                    clientConnectionInter.sendFileToGroup(fileGroupMessageDto, groupMembers);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/FileMessageView.fxml"));
                            try {
                                AnchorPane anchorPane = fxmlLoader.load();
                                FileMessageController controller = fxmlLoader.getController();
                                //todo still won't work with the method only by making the attribute public!
                                controller.fileNameLabelId.setText(fileToSend.getName());
                                controller.fileNameLabelId.setAlignment(Pos.CENTER_RIGHT);
                                controller.recordID.setText(String.valueOf(fileRecordId));
                                msgTxtAreaID.setText("");
                                chatListView.getItems().add(anchorPane);
                                chatListView.scrollTo(anchorPane);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else{
                    Date date = Date.valueOf(LocalDate.now());
                    MessageDto fileMessageDto = new MessageDto(-1, fileToSend.getName(), currentUserModel.getPhoneNumber(),
                            receiverNumber.getText(), "unseen", fileData, date);
                    try {
                        int msgRecordID = messageServices.insertMessage(fileMessageDto);
                        if (msgRecordID == -1) {
                            System.out.println("id of the record, if -1 then failed to insert " + msgRecordID);
                            return;
                        }
                        fileMessageDto.setId(msgRecordID);
                        clientConnectionInter.sendFile(fileMessageDto);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/FileMessageView.fxml"));
                                try {
                                    AnchorPane anchorPane = fxmlLoader.load();
                                    FileMessageController controller = fxmlLoader.getController();
                                    //todo still won't work with the method only by making the attribute public!
                                    controller.fileNameLabelId.setText(fileToSend.getName());
                                    controller.fileNameLabelId.setAlignment(Pos.CENTER_RIGHT);
                                    controller.recordID.setText(String.valueOf(msgRecordID));
                                    msgTxtAreaID.setText("");
                                    chatListView.getItems().add(anchorPane);
                                    chatListView.scrollTo(anchorPane);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        fis.close();
                        fileFlag = false;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void OnclickCreateGroup(ActionEvent event) throws IOException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                ObservableList<BorderPane> list = contactsListViewId.getItems();
                for (BorderPane item : list) {
                    VBox vBox = (VBox) item.getCenter();
                    HBox hbox = (HBox) vBox.getChildren().get(0);
                    Label id = (Label) vBox.getChildren().get(1);
                    VBox vBox1 = (VBox) hbox.getChildren().get(1);
                    Button addbtn = (Button) vBox1.getChildren().get(0);
                    Button deletebtn = (Button) vBox1.getChildren().get(1);
                    if (id.getText().charAt(0) != '0') {
                        addbtn.setVisible(false);
                        deletebtn.setVisible(false);
                    } else {

                        addbtn.setVisible(true);
                        deletebtn.setVisible(true);
                        deletebtn.setDisable(true);
                        addbtn.setDisable(false);
                    }


                }
                groupnameID.setVisible(true);
                addGroupBtn.setVisible(true);


            }


        });
    }

    @FXML
    void onClickAddGroup(ActionEvent event) {
        if (groupnameID.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("please enter a group name first ");
            alert.show();
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ContactView.fxml"));
                    try {
                        BorderPane borderPane = fxmlLoader.load();
                        ContactController contactController = fxmlLoader.getController();
                        VBox vBox2 = (VBox) borderPane.getCenter();
//                        ImageView imageView = (ImageView) borderPane.getLeft();
                        ImageView imageView = (ImageView) ((AnchorPane) borderPane.getLeft()).getChildren().get(0);
                        HBox hbox1 = (HBox) vBox2.getChildren().get(0);
                        Label label = (Label) hbox1.getChildren().get(0);
                        Label label1 = (Label) vBox2.getChildren().get(1);
                        label.setText(groupnameID.getText());

                        System.out.println("33333333->");


                        GroupChatInter groupChatInter = GroupChatService.getGroupChatInter();
                        ModelsFactory modelsFactory = ModelsFactory.getInstance();
                        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
                        contactController.groupChatMembers.add(currentUserModel.getPhoneNumber());
                        Image groupchat = new Image(MainScreenController.class.getResource("/iti/jets/gfive/images/groupchat.png").toString());
                        var groupid = groupChatInter.insert(groupnameID.getText(), contactController.groupChatMembers);
                        ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
                        for (int i = 0; i < contactController.groupChatMembers.size(); i++) {
                            if (contactController.groupChatMembers.get(i).equals(currentUserModel.getPhoneNumber())) {
                                contactController.groupChatMembers.remove(i);
                            }
                        }
                        clientConnectionInter.createGroupInAllMemebers(groupnameID.getText(), contactController.groupChatMembers, String.valueOf(groupid));
                        imageView.setImage(groupchat);
                        System.out.println("------>groupis " + groupid);

                        label1.setText(String.valueOf(groupid));
                        label1.setVisible(false);
                        System.out.println(label1.getText());
                        contactsListViewId.getItems().add(borderPane);

                        contactController.groupChatMembers.clear();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        //call server to update the ui of other members
//                        ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
//                        clientConnectionInter.createGroupInAllMemebers(groupnameID.getText());
//                        GroupChatInter groupChatInter = GroupChatService.getGroupChatInter();
//                        groupChatInter.insert();
//
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }

                    ObservableList<BorderPane> list = contactsListViewId.getItems();
                    for (BorderPane item : list) {

                        System.out.println("222222->");
                        VBox vBox = (VBox) item.getCenter();
                        HBox hbox = (HBox) vBox.getChildren().get(0);
                        VBox vBox1 = (VBox) hbox.getChildren().get(1);
                        Button addbtn = (Button) vBox1.getChildren().get(0);
                        Button deletebtn = (Button) vBox1.getChildren().get(1);
                        addbtn.setVisible(false);
                        deletebtn.setVisible(false);
                        groupnameID.setText("");
                        groupnameID.setVisible(false);
                        addGroupBtn.setVisible(false);
                    }

                }
            });
        }

    }

    public void changeContactStatus(UserDto user) {
//        ContactsListView.getInstance().changeContactStatus(user);
        System.out.println(user.getUsername() + " ------->" + user.getStatus());
    }
}