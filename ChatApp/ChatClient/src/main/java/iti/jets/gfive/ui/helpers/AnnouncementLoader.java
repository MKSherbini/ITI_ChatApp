package iti.jets.gfive.ui.helpers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AnnouncementLoader {
    Scene scene = null ;
    Stage stage = null;
    StringProperty content= new SimpleStringProperty();
    private static AnnouncementLoader instance = null;
    public synchronized static AnnouncementLoader getInstance(){
        if(null == instance){
            instance = new AnnouncementLoader();
        }
        return instance ;
    }
    private void openDialog(String viewName) {
        if(stage == null ) {
            stage = new Stage();
            if (scene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(String.format("/iti/jets/gfive/views/%s.fxml", viewName)));
                Parent parent = null;
                try {
                    parent = fxmlLoader.load();
                    scene = new Scene(parent, 600, 400);
                    stage.setMaximized(false);
                    stage.setResizable(false);
                    stage.initStyle(StageStyle.UTILITY);
                    stage.initModality(Modality.APPLICATION_MODAL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            if(stage.isShowing()){
                return ;
            }
        }
        stage.setScene(scene);
        stage.setTitle("Server Announcement ..");
        stage.showAndWait();
    }

    public void showDialog(String content){
        ModelsFactory.getInstance().getCurrentUserModel().announceProperty().setValue(content);
        openDialog("AnnouncementDialog");
    }
}
