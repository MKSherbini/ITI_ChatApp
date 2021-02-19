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
//        FileChooser.ExtensionFilter musicFileExtensions =new FileChooser.ExtensionFilter("Music Files","*.mp3", "*.mp4", "*.wav");
//        FileChooser.ExtensionFilter imageFileExtensions =new FileChooser.ExtensionFilter("Image Files","*.jpg", "*.jpeg", "*.png", "*.gif");
//        FileChooser.ExtensionFilter textFileExtensions =new FileChooser.ExtensionFilter("Text Files","*.txt");
//        fileChooser.getExtensionFilters().addAll(musicFileExtensions, imageFileExtensions, textFileExtensions);
        File selectedFile;
        String [] fileSplit = fileNameLabelId.getText().split("\\.");
        String extension = fileSplit[1];
        System.out.println(extension + " .extension");
        if (extension.equals("txt")) {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
            selectedFile = fileChooser.showSaveDialog(null);
            if (fileChooser != null) {
                if (selectedFile != null) {
                    String filePath = selectedFile.getAbsolutePath();
                    MessageDBInter messageServices = MessageDBService.getMessageService();
                    try {
                        byte[] retrievedFile = messageServices.getFile(Integer.parseInt(recordID.getText()));
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        if (retrievedFile == null) {
                            System.out.println("Fuckin null bytes");
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
        } else if(extension.equals("mp3") || extension.equals("mp4") || extension.equals("wav")) {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Music Files", "*.mp3", "*.mp4", "*.wav"));
            selectedFile = fileChooser.showSaveDialog(null);
            if (fileChooser != null) {
                if (selectedFile != null) {
                    String filePath = selectedFile.getAbsolutePath();
                    MessageDBInter messageServices = MessageDBService.getMessageService();
                    try {
                        byte[] retrievedFile = messageServices.getFile(Integer.parseInt(recordID.getText()));
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        if (retrievedFile == null) {
                            System.out.println("Fuckin null bytes");
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
        } else if(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")){
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg", "*.jpeg", "*.png", "*.gif"));
            selectedFile = fileChooser.showSaveDialog(null);
            if (fileChooser != null) {
                if (selectedFile != null) {
                    String filePath = selectedFile.getAbsolutePath();
                    MessageDBInter messageServices = MessageDBService.getMessageService();
                    try {
                        byte[] retrievedFile = messageServices.getFile(Integer.parseInt(recordID.getText()));
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        if (retrievedFile == null) {
                            System.out.println("Fuckin null bytes");
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
        } else if(extension.equals("gif")){
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GIF Files","*.gif"));
            selectedFile = fileChooser.showSaveDialog(null);
            if (fileChooser != null) {
                if (selectedFile != null) {
                    String filePath = selectedFile.getAbsolutePath();
                    MessageDBInter messageServices = MessageDBService.getMessageService();
                    try {
                        byte[] retrievedFile = messageServices.getFile(Integer.parseInt(recordID.getText()));
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        if (retrievedFile == null) {
                            System.out.println("Fuckin null bytes");
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
}
