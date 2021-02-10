package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
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
}