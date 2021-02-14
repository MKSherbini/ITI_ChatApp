package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.ui.controllers.ChatMessageController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NotificationMsgHandler extends UnicastRemoteObject implements NotificationReceiveInter {
    private static NotificationMsgHandler notificationsLabel = null;
    private Label notificationLabelId;
    private ListView<AnchorPane> listView;
    private Button newButton;
    private ListView<BorderPane> contactsList;
    private BorderPane borderPane;
    private Label name;

    private NotificationMsgHandler() throws RemoteException {
        super();
    }

    public synchronized static NotificationMsgHandler getInstance() {
        if (notificationsLabel == null) {
            try {
                System.out.println("creating new obj ");
                return notificationsLabel = new NotificationMsgHandler();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        System.out.println("getting the obj");
        return notificationsLabel;
    }

    public void setNotificationLabel(Label notificationLabelId) {
        this.notificationLabelId = notificationLabelId;
        //System.out.println(notificationLabelId + "setting notification label");

    }

    public void setListView(ListView<AnchorPane> list) {
        listView = list;
    }
    public void setContactList(ListView<BorderPane> list) {
        contactsList = list;
    }
    public void setChatarea(BorderPane borderPane)
    {
        this.borderPane =borderPane;
    }
    public void setButton(Button b)
    {

        newButton=b;
    }
    public void setname(Label name)
    {
        this.name=name;
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

    @Override
    public void receive(NotificationDto notification) throws RemoteException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int notificationsNumber = Integer.parseInt(notificationLabelId.getText());
                notificationsNumber += 1;
                notificationLabelId.setText(notificationsNumber + "");
            }
        });
    }

    @Override
    public void receiveMsg(MessageDto messageDto) throws RemoteException {
        //el mafrod de kolha tb2a fel action bta3 el button

        Platform.runLater(new Runnable() {
                              @Override
                              public void run() {

                                  ObservableList<BorderPane> list = contactsList.getItems();
                                  for (BorderPane item : list) {
                                      VBox vBox = (VBox) item.getCenter();
                                      HBox hbox =(HBox) vBox.getChildren().get(0);
                                      Label senderName = (Label)hbox.getChildren().get(0);

                                      Label receiverNumber = (Label) vBox.getChildren().get(1);
                                      if (receiverNumber.getText().equals(messageDto.getSenderNumber())) {
                                          System.out.println("inside the match buttton");
                                          Button b = new Button("new");
                                          item.setRight(b);
                                          b.setOnAction(actionEvent -> {
                                              try {
                                                  b.setVisible(false);
                                                  name.setText(senderName.getText());
                                                  borderPane.setVisible(true);

                                                  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
                                                  AnchorPane anchorPane = fxmlLoader.load();
                                                  ChatMessageController controller = fxmlLoader.getController();
                                                  System.out.println("content of the message " + messageDto.getContent());
                                                  controller.msgLabelId.setText(messageDto.getContent());
                                                  // msgTxtFieldId.setText("");
                                                  listView.getItems().add(anchorPane);
                                              } catch (IOException e) {
                                                  e.printStackTrace();
                                              }


                                          });

                                          //newButton.setVisible(true);
                                          break;
                                      }
                                  }
                              }
                          });
                    //load contactview controller.phonenumber ==messgaedto.phonenumber
                    //get the items in the list and check if it's phone = phone

                    //todo still won't work with the method only by making the attribute public!
                    //controller.setLabelValue(contact.getUsername());
//                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatMessageView.fxml"));
//                    AnchorPane anchorPane = fxmlLoader.load();
//                    ChatMessageController controller = fxmlLoader.getController();
//                    System.out.println("content of the message " + messageDto.getContent());
//                    controller.msgLabelId.setText(messageDto.getContent());
//                    // msgTxtFieldId.setText("");
//                    listView.getItems().add(anchorPane);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }



}
    }
