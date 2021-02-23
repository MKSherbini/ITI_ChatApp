package iti.jets.gfive.ui.controllers;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AnnouncementDialog  implements Initializable {

    @FXML
    private WebView webView;

    @FXML
    private Button btnCancel;


    @FXML
    void btnCancelClicked(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED){
                webView.getEngine().getTitle();
            }
        });


        webView.getEngine().loadContent(ModelsFactory.getInstance().getCurrentUserModel().getAnnounce());
        ModelsFactory.getInstance().getCurrentUserModel().announceProperty().addListener((opservable, old ,newValue)->webView.getEngine().loadContent(newValue));
    }
}
