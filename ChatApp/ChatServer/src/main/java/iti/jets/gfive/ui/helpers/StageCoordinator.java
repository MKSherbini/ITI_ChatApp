package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.Server;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StageCoordinator {

    private static Stage primaryStage;
    private static final StageCoordinator stageCoordinator = new StageCoordinator();
    private final Map<String, SceneData> scenes = new HashMap<>();

    private StageCoordinator() {
    }

    public void initStage(Stage stage) {
        if (primaryStage != null) {
            throw new IllegalArgumentException("The Stage Already been initialized");
        }
        primaryStage = stage;
        primaryStage.setOnCloseRequest(event -> {
            Server.getInstance().stopServer();
            Platform.exit();
        });
    }

    public static StageCoordinator getInstance() {
        return stageCoordinator;
    }


    public void switchToServerMain() {
        var viewName = "ServerMain";

        loadView(viewName);
    }

    private void loadView(String viewName) {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }
        if (!scenes.containsKey(viewName)) {
            try {
                System.out.println("Created New Scene");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(String.format("/iti/jets/gfive/views/%s.fxml", viewName)));
                Parent loginView = fxmlLoader.load();
                Scene loginScene = new Scene(loginView);
                SceneData loginSceneData = new SceneData(fxmlLoader, loginView, loginScene);
                scenes.put(viewName, loginSceneData);
                primaryStage.setScene(loginScene);
            } catch (IOException e) {
//                System.out.println(String.format("IO Exception: Couldn't load %s FXML file", viewName));
                e.printStackTrace();
            }
        } else {
            System.out.println("Loaded Existing Scene");
            SceneData loginSceneData = scenes.get(viewName);
            Scene loginScene = loginSceneData.getScene();
            primaryStage.setScene(loginScene);
        }
    }


}
