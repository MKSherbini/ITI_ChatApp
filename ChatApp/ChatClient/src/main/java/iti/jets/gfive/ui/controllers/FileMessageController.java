package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.services.MessageDBService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        FileChooser fileChooser;
        File selectedFile;
        String[] fileSplit = fileNameLabelId.getText().split("\\.");
        String extension = fileSplit[1];
        //System.out.println(extension + " .extension");
        fileChooser = new FileChooser();
        fileChooser.setInitialFileName(fileNameLabelId.getText());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));
        selectedFile = fileChooser.showSaveDialog(null);
        if (fileChooser != null) {
            if (selectedFile != null) {
                String filePath = selectedFile.getAbsolutePath();
                MessageDBInter messageServices = MessageDBService.getMessageService();
                try {
                    byte[] retrievedFile = messageServices.getFile(Integer.parseInt(recordID.getText()));
                    if (!filePath.endsWith(extension)) {
                        filePath = filePath + "." + extension;
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                    if (retrievedFile == null) {
                        return;
                    }
                    fileOutputStream.write(retrievedFile);
                    fileOutputStream.close();
                } catch (RemoteException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
