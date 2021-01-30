package iti.jets.gfive.ui.helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneData {
    private final FXMLLoader fxmlLoader;
    private final Parent view;
    private final Scene scene;

    public SceneData(FXMLLoader fxmlLoader, Parent view, Scene scene) {
        this.fxmlLoader = fxmlLoader;
        this.view = view;
        this.scene = scene;
    }

    public FXMLLoader getLoader() {
        return this.fxmlLoader;
    }

    public Parent getView() {
        return this.view;
    }

    public Scene getScene() {
        return this.scene;
    }
}
