package iti.jets.gfive.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ChatMessageController {
    @FXML
    private ImageView msgImgId;

    @FXML
    public Label msgLabelId;

    public void setMsgImgId(ImageView msgImgId) {
        this.msgImgId = msgImgId;
    }
    public void setMsgLabelId(Label label)
    {
        this.msgLabelId=label;
    }
}
