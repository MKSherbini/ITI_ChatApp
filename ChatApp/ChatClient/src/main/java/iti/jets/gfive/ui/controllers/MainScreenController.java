package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.LoginManager;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
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
        miExit.setOnAction((actionEvent)-> {
            LoginManager.getInstance().Exit();
            Platform.exit();
        });
        miLogout = new MenuItem("Logout");
        miLogout.setOnAction((acrionEvent)-> {
            LoginManager.getInstance().Logout();
            StageCoordinator.getInstance().switchToLoginPage();
        });
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

//        btnContextMenu.setContextMenu(contextMenu);
//        initPopup();

        ContactsListView c = ContactsListView.getInstance();
        c.setContactsListViewId(this.contactsListViewId);
    }


    public void showContextMenu(MouseEvent event) {
        contextMenu.show(btnContextMenu,Side.BOTTOM, 0,0);
    }

    public void performExit(ActionEvent actionEvent) {
    }

    public void performLogout(ActionEvent actionEvent) {
    }
    @FXML
    public void openAddNewContactDialog(MouseEvent mouseEvent) {
        openDialog("NewContactDialog");
    }
    @FXML
    public void openFriendRequestDialog(ActionEvent actionEvent) {
        openDialog("FriendRequestDialog");
    }
    private void openDialog(String viewName){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(String.format("/iti/jets/gfive/views/%s.fxml",viewName)));
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
}