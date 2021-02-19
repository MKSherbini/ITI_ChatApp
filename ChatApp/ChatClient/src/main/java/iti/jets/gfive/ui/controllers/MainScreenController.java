package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.ClientConnectionService;
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
import javafx.stage.FileChooser;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.*;
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
    private Button btnContextMenu;

    @FXML
    private JFXListView<BorderPane> contactsListViewId;
    @FXML
    private Label notificationLabelId;
    @FXML
    private Label newLabelID;
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
    @FXML
    private ImageView ivStatus;
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
        CurrentUserNameID.setText(currentUserModel.getUsername());
        profilepictureID.setImage(currentUserModel.getImage());
        chatListView.scrollTo(chatListView.getItems().size() - 1);
        System.out.println(ModelsFactory.getInstance().getCurrentUserModel().getStatus());
        changeStatusUi();
        initializeContextMenu();
        ContactsListView c = ContactsListView.getInstance();
        c.setContactsListViewId(this.contactsListViewId);

        NotificationMsgHandler n = NotificationMsgHandler.getInstance();
        n.setNotificationLabel(notificationLabelId);
        //System.out.println(notificationLabelId + "NotificationsLabel in Mainscreen client");
        n.setContactList(contactsListViewId);
        n.setListView(chatListView);
        n.setChatarea(chatAreaBorderPaneID);
        n.setname(receivernameID);
        n.setnumber(receivernumberID);
        newLabel = n.getnewLabel();
        //  n.setnewLabel(newLabelID);
        //n.setButton(contactsListViewId);

        // System.out.println(notificationLabelId + "NotificationsLabel in Mainscreen client");
        //System.out.println("notifaction label is initalizedddddd");
        System.out.println(notificationLabelId + "NotificationsLabel in Mainscreen client");
        NotificationMsgHandler n2 = NotificationMsgHandler.getInstance();
        //System.out.println("calling the get instance again in the client");
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
            ImageView receiverimage = (ImageView) borderPane.getLeft();
            System.out.println("-------------->" + receiverimage);
            //to check if there is a new message or not
            if (borderPane.getRight() != null) {
                newLabel.setVisible(false);
                System.out.println("right of borderpane equals null");
            }
            receivernameID.setText(name.getText());
            receivernumberID.setText(receiverNumber.getText());
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
        List<MessageDto> messageList = null;
        try {
            messageList = messageServices.selectAllMessages(receiverNumber.getText(), currentUserModel.getPhoneNumber());
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }

        //   System.out.println("number of list" +messageList.size());
        // messageList = messageServices.selectAllMessages(receiverNumber.getText() ,currentUserModel.getPhoneNumber());
        List<MessageDto> finalMessageList = messageList;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatListView.getItems().clear();
                try {
                    for (MessageDto messageDto : finalMessageList) {
                        if(messageDto.getMessageName().equals("text")){
                            //select picture from user_data where user_id = sender_id
                            //select picture from user_data where user_id= recevierid
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
                            AnchorPane anchorPane = fxmlLoader.load();
                            ChatMessageController controller = fxmlLoader.getController();

                            //   System.out.println("content of the message "+messageDto.getContent());
                            controller.msgLabelId.setText(messageDto.getContent());
                            if (currentUserModel.getPhoneNumber().equals(messageDto.getSenderNumber())) {
                                controller.msgLabelId.setAlignment(Pos.CENTER_RIGHT);
                                controller.msgLabelId.setStyle("-fx-background-color: #ABC8E2;");
                                // controller.msgImgId.setImage(senderimg);

                            } else {
                                //  controller.msgImgId.setImage(recevierpicyure);
                            }
                            msgTxtAreaID.setText("");
                            chatListView.getItems().add(anchorPane);
                        } else{
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
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        chatListView.scrollTo(chatListView.getItems().size() - 1);
    }

    @FXML
    public void onClickSendButton(ActionEvent actionEvent) {
        if (msgTxtAreaID.getText().equals("")) {
            return;
        }
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();


        MessageDBInter messageServices = MessageDBService.getMessageService();

        //todo must retreive the image of the sender to db and send it as paramter in sendMsg

        if(fileFlag == false){
        String messsage = msgTxtAreaID.getText();
        Date date = Date.valueOf(LocalDate.now());
        System.out.println("messagename" + currentUserModel.getPhoneNumber() +receiverNumber.getText() +"unseen"+messsage +date);
        MessageDto messageDto = new MessageDto(-1, "text", currentUserModel.getPhoneNumber(), receiverNumber.getText(), "unseen", messsage, date);
        try {
            ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
            clientConnectionInter.sendMsg(messageDto);

        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }

        try {
            int rowaffected = messageServices.insertMessage(messageDto);
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }
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
        else{
            String filePath = msgTxtAreaID.getText();
            File fileToSend = new File(filePath);
            try {
                FileInputStream fis = new FileInputStream(fileToSend);
                int fileLength = (int) fileToSend.length();
                System.out.println(fileLength + "file length");
                byte [] fileData = new byte[fileLength];
                int c = fis.read(fileData);
                System.out.println(c + "int c");
                System.out.println(fileData.toString() + "fileData byte array");
                Date date = Date.valueOf(LocalDate.now());
                MessageDto fileMessageDto = new MessageDto(-1, fileToSend.getName(), currentUserModel.getPhoneNumber(),
                        receiverNumber.getText(), "unseen", fileData,date);
                try {
                    ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
                    int msgRecordID = messageServices.insertMessage(fileMessageDto);
                    if(msgRecordID == -1){
                        System.out.println("id of the record, if -1 then failed to insert "+ msgRecordID);
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            chatModel.getMessages().add(new MessageModel(messageDto.getSenderNumber(), messageDto.getReceiverNumber(), messageDto.getContent()));
        });

        var m = Marshaltor.getInstance();
        m.marshalChat(chatModel);
    }

    public void onClickAttachFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            if((selectedFile.length()/1048576) > 10){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("File Size Error");
                a.setContentText("Cannot send more than a 10MB file");
                a.setHeaderText("Error: File is too big");
                a.show();
                return;
            }
            fileFlag = true;
            msgTxtAreaID.setText(selectedFile.getPath());
        }
    }

    public void changeContactStatus(UserDto user) {
//        ContactsListView.getInstance().changeContactStatus(user);
        System.out.println(user.getUsername() + " ------->" + user.getStatus());
    }
}