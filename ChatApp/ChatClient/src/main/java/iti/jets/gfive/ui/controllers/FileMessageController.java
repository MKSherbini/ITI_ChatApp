package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.common.interfaces.GroupChatInter;
import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.services.GroupChatService;
import iti.jets.gfive.services.MessageDBService;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import iti.jets.gfive.ui.helpers.StageCoordinator;
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
        NotificationMsgHandler n = NotificationMsgHandler.getInstance();
        String recevierNum = n.getReceivernumberID();
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
                new Thread(()->{
                    String filePath = selectedFile.getAbsolutePath();
                    MessageDBInter messageServices = MessageDBService.getMessageService();
                    GroupChatInter groupChatInter = GroupChatService.getGroupChatInter();
                    byte[] retrievedFile = null;
                    try {
                        retrievedFile = recevierNum.charAt(0) == '0' ? messageServices.getFile(Integer.parseInt(recordID.getText())) : groupChatInter.getFileForGroup(Integer.parseInt(recordID.getText()));
                        if (!filePath.endsWith(extension)) {
                            filePath = filePath + "." + extension;
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        if (retrievedFile == null) {
                            return;
                        }
                        fileOutputStream.write(retrievedFile);
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        StageCoordinator.getInstance().reset();
                    }
                }).start();
            }
        }
    }
}
