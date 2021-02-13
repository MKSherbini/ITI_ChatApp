package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.services.ClientConnectionService;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.services.MessageDBService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
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
    @FXML
    public ImageView ibtnAddContct;
    @FXML
    private ImageView ivContextMenu;
    @FXML
    private Button btnContextMenu;
    private ContextMenu contextMenu;
    JFXPopup popupMenu ;
    @FXML
    private MenuItem miExit;
    @FXML
    private MenuItem miLogout,miAvailable,miBusy,miSleep,miOut;
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
   /* @FXML
    void showContxtMenu(MouseEvent event) {
    contextMenu.show(btnContextMenu.getParent(),event.getX(),event.getY());
        popupMenu.show(btnContextMenu.getParent(), JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT ,btnContextMenu.getLayoutX(),btnContextMenu.getLayoutY());
    }

*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contextMenu = new ContextMenu();
        miExit = new MenuItem("Exit");
        miLogout = new MenuItem("Logout");
        status= new Menu("Status");
        miAvailable = new MenuItem("Available");
        miBusy = new MenuItem("Busy");
        miSleep = new MenuItem("Sleep");
        miOut = new MenuItem("Out");
        status.getItems().add(miAvailable);
        status.getItems().add(miOut);
        status.getItems().add(miSleep);
        status.getItems().add(miBusy);
        contextMenu.getItems().addAll(status);
        contextMenu.getItems().addAll( miExit , miLogout);

        btnContextMenu.setContextMenu(contextMenu);
        initPopup();

        ContactsListView c = ContactsListView.getInstance();
        c.setContactsListViewId(this.contactsListViewId);

        NotificationMsgHandler n = NotificationMsgHandler.getInstance();
        n.setNotificationLabel(notificationLabelId);
        n.setContactList(contactsListViewId);
        n.setListView(chatListView);
       // n.setChatarea(chatAreaBorderPaneID);
        //n.setButton(contactsListViewId);

        System.out.println(notificationLabelId + "NotificationsLabel in Mainscreen client");
        //System.out.println("notifaction label is initalizedddddd");
        NotificationMsgHandler n2 = NotificationMsgHandler.getInstance();
        System.out.println("calling the get instance again in the client");
    }
   void  initPopup(){
        popupMenu = new JFXPopup();
       VBox vBox= new VBox();
       JFXButton btnExit = new JFXButton("Exit");
       JFXButton btnLogout = new JFXButton("Logout");
       Popup status =new Popup();

       boolean b = vBox.getChildren().addAll(btnExit, btnLogout);
       popupMenu . setPopupContent(vBox);
        popupMenu.setAutoHide(true);
    }

//    public void showContextMenu(MouseEvent mouseEvent) {
//        contextMenu.show(btnContextMenu.getParent(),mouseEvent.getX(),mouseEvent.getY());
//        popupMenu.show(btnContextMenu.getParent(), JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT ,btnContextMenu.getLayoutX(),btnContextMenu.getLayoutY());
//    }

    public void showContextMenu(MouseEvent event) {
        contextMenu.show(btnContextMenu,Side.BOTTOM, -75,0);
//        popupMenu.show(btnContextMenu.getParent(), JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT ,btnContextMenu.getLayoutX(),btnContextMenu.getLayoutY());

    }

    public void performExit(ActionEvent actionEvent) {
    }

    public void performLogout(ActionEvent actionEvent) {
    }
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
        chatListView.getItems().clear();
        ObservableList<BorderPane> selectedContact;
        selectedContact= contactsListViewId.getSelectionModel().getSelectedItems();
        for(BorderPane borderPane:selectedContact) {
            //get the name and image of the selected contact
            VBox vBox = (VBox) borderPane.getCenter();
            HBox hbox =(HBox) vBox.getChildren().get(0);
            Label name = (Label)hbox.getChildren().get(0);
            receiverNumber = (Label) vBox.getChildren().get(1);
            // ImageView imageView =(ImageView) borderPane.getLeft();
            System.out.println("label text is " +name.getText());
            receivernameID.setText(name.getText());
            chatAreaBorderPaneID.setVisible(true);
        }
       /* ModelsFactory modelsFactory = ModelsFactory.getInstance();
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
           // chatListView.getItems().clear();*/

        }


    @FXML
    public void onClickSendButton(ActionEvent actionEvent) throws RemoteException {
        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();


        MessageDBInter messageServices = MessageDBService.getMessageService();

        String messsage = msgTxtFieldId.getText();
        Date date = Date.valueOf(LocalDate.now());
        MessageDto messageDto =new MessageDto("messagename" , currentUserModel.getPhoneNumber() ,receiverNumber.getText() ,"unseen",messsage ,date);
        try {
            ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
            clientConnectionInter.sendMsg(messageDto);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

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