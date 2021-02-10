package iti.jets.gfive.ui.controllers;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.controlsfx.control.ToggleSwitch;

public class ChatAreaController {
    @FXML
    private HBox hboxChatBarId;

    @FXML
    private ToggleSwitch botSwitchBtnId;

    @FXML
    private Button saveBtnId;

    @FXML
    private TextField msgTxtFieldId;

    @FXML
    private JFXButton attachBtnId;

    @FXML
    private JFXButton sendBtnId;

    @FXML
    private ListView<?> chatListView;
}
