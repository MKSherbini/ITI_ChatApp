package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.services.MessageDBService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class  MainScreenController implements Initializable {
    public static final String URL_RESOURCE= "/iti/jets/gfive/icons/%s.png";
    public static final String AWAY = "away";
    public static final String AVAILABLE = "available";
    public static final String OFFLINE = "offline";
    public static final String BUSY = "busy";
    @FXML
    public ImageView ibtnAddContct;

    @FXML
    private Button btnContextMenu;
    private ContextMenu contextMenu;
    private MenuItem miExit;
    private MenuItem miLogout,miAvailable,miBusy, miAway, miOffline;
    private Menu status;
    @FXML
    private JFXListView<BorderPane> contactsListViewId;
    @FXML
    private Label notificationLabelId;

    @FXML
    private BorderPane chatAreaBorderPaneID;
    @FXML
    private Label receivernameID;
    @FXML
    private JFXButton sendBtnId;
    @FXML
    private TextField msgTxtFieldId;
    @FXML
    private ListView<AnchorPane> chatListView;
    private Label receiverNumber;
    @FXML
    private ImageView ivStatus;
    Property<Image> statusImage;

    // this method intialize the context menu and its items actions
    private void initializeContextMenu(){
        contextMenu = new ContextMenu();
        miExit = new MenuItem("Exit");
        miLogout = new MenuItem("Logout");
        status= new Menu("Status");
        miAvailable = new MenuItem("Available");
        miAvailable.setOnAction((action)-> applyMenUItemAction(AVAILABLE));
        miAvailable.setGraphic(getConfiguredImageView(new Image(getClass().getResource(String.format(URL_RESOURCE,AVAILABLE)).toString())));
        miBusy = new MenuItem("Busy");
        miBusy.setOnAction((action)-> applyMenUItemAction(BUSY));
        miBusy.setGraphic(getConfiguredImageView(new Image(getClass().getResource(String.format(URL_RESOURCE,BUSY)).toString())));
        miAway = new MenuItem("Away");
        miAway.setOnAction((action)-> applyMenUItemAction(AWAY));
        miAway.setGraphic(getConfiguredImageView(new Image(getClass().getResource(String.format(URL_RESOURCE,AWAY)).toString())));
        miOffline = new MenuItem("Offline");
        miOffline.setOnAction((action)->applyMenUItemAction(OFFLINE));
        miOffline.setGraphic(getConfiguredImageView(new Image(getClass().getResource(String.format(URL_RESOURCE,OFFLINE)).toString())));
        status.getItems().add(miAvailable);
        status.getItems().add(miBusy);
        status.getItems().add(miAway);
        status.getItems().add(miOffline);
        contextMenu.getItems().addAll(status);
        contextMenu.getItems().addAll( miExit , miLogout);
    }
    // this method define the action of the status menu items to change the status on gui and on the db.
    private void applyMenUItemAction(String statusName) {
        statusImage.setValue(new Image(getClass().getResource(String.format(URL_RESOURCE,statusName)).toString()));
        try {
            int rows = UserDBCrudService.getUserService().updateUserStatus(new UserDto(ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber(), statusName));
            System.out.println("status updated  : "+ rows);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    // this method binds the status image property on the imageview status image property
    private void bindIvStatusImage(String imageName){
        statusImage = new SimpleObjectProperty<>();
        statusImage.setValue(new Image(getClass().getResource(String.format(URL_RESOURCE,imageName)).toString()));
        statusImage.bindBidirectional(ivStatus.imageProperty());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindIvStatusImage("available");
        initializeContextMenu();
        ContactsListView c = ContactsListView.getInstance();
        c.setContactsListViewId(this.contactsListViewId);

        NotificationMsgHandler n = NotificationMsgHandler.getInstance();
        n.setNotificationLabel(notificationLabelId);
        System.out.println(notificationLabelId + "NotificationsLabel in Mainscreen client");
        NotificationMsgHandler n2 = NotificationMsgHandler.getInstance();
        System.out.println("calling the get instance again in the client");
    }
    // this method create new image view object and fit its width and height to 20 px .
    public ImageView getConfiguredImageView(Image image){
        ImageView iv = new ImageView(image);
        iv.setFitWidth(20);
        iv.setFitHeight(20);
        return iv;
    }
    // this method register the context menu on the btnContextMenu .
    public void showContextMenu(MouseEvent event) {
        contextMenu.show(btnContextMenu,Side.BOTTOM, 0,0);
    }
    // this method opens new contact dialog
    @FXML
    public void openAddNewContactDialog(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/NewContactDialog.fxml"));
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

    public void OpenNotificationDialog(MouseEvent mouseEvent) {
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
        //fetch all message from db and update the list
        //li


        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();

        MessageDBInter messageServices = MessageDBService.getMessageService();
        
        System.out.println("pressed");
        ObservableList<BorderPane> selectedContact;
        selectedContact= contactsListViewId.getSelectionModel().getSelectedItems();
        for(BorderPane borderPane:selectedContact) {
            //get the name and image of the selected contact
            VBox vBox = (VBox) borderPane.getCenter();
            Label name =(Label) vBox.getChildren().get(0);
             receiverNumber = (Label) vBox.getChildren().get(1);
           // ImageView imageView =(ImageView) borderPane.getLeft();
            System.out.println("label text is " +name.getText());
            receivernameID.setText(name.getText());
            chatAreaBorderPaneID.setVisible(true);
        }
        final  List<MessageDto> messageList = messageServices.selectAllMessages(receiverNumber.getText() ,currentUserModel.getPhoneNumber());
        System.out.println("number of list" +messageList.size());
       // messageList = messageServices.selectAllMessages(receiverNumber.getText() ,currentUserModel.getPhoneNumber());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatListView.getItems().clear();

                    try {

                        //todo still won't work with the method only by making the attribute public!
                        //controller.setLabelValue(contact.getUsername());
                        for(MessageDto messageDto:messageList) {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
                            AnchorPane anchorPane = fxmlLoader.load();
                            ChatMessageController controller = fxmlLoader.getController();
                            System.out.println("content of the message "+messageDto.getContent());
                            controller.msgLabelId.setText(messageDto.getContent());
                            msgTxtFieldId.setText("");
                            chatListView.getItems().add(anchorPane);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
           // chatListView.getItems().clear();

        }


    @FXML
    public void onClickSendButton(ActionEvent actionEvent) throws RemoteException {
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();


        MessageDBInter messageServices = MessageDBService.getMessageService();

        String messsage = msgTxtFieldId.getText();
        Date date = Date.valueOf(LocalDate.now());
        MessageDto messageDto =new MessageDto("messagename" , currentUserModel.getPhoneNumber() ,receiverNumber.getText() ,"unseen",messsage ,date);
        int rowaffected = messageServices.insertMessage(messageDto);
        System.out.println("row inserted equal "+rowaffected);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    ChatMessageController controller = fxmlLoader.getController();
                    //todo still won't work with the method only by making the attribute public!
                    //controller.setLabelValue(contact.getUsername());
                    controller.msgLabelId.setText(messsage);
                    msgTxtFieldId.setText("");
                    chatListView.getItems().add(anchorPane);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}