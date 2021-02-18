package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.services.MessageDBService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

public class FileMessageController {
    @FXML
    public Label recordID;

    @FXML
    public Label fileNameLabelId;

    @FXML
    public Button downloadBtnId;

    public void onClickDownloadBtn(ActionEvent actionEvent) {
        System.out.println("inside the download button");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter musicFileExtensions =new FileChooser.ExtensionFilter("Music fFiles","*.mp3", "*.mp4", "*.wav");
        FileChooser.ExtensionFilter imageFileExtensions =new FileChooser.ExtensionFilter("Image Files","*.jpg", "*.jpeg", "*.png");
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files","*.wav"));
        fileChooser.getExtensionFilters().addAll(musicFileExtensions, imageFileExtensions);
        File selectedFile = fileChooser.showSaveDialog(null);
        if(fileChooser != null){
            if(selectedFile != null){
                String filePath = selectedFile.getAbsolutePath();
                MessageDBInter messageServices = MessageDBService.getMessageService();
                try {
                    byte [] retrievedFile = messageServices.getFile(Integer.parseInt(recordID.getText()));
                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                    if(retrievedFile == null){
                        System.out.println("Fuckin null bytes");
                        return;
                    }
                    fileOutputStream.write(retrievedFile);
                } catch (RemoteException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
