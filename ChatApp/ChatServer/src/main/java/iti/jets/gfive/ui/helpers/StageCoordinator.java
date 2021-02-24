package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.Server;
import iti.jets.gfive.server.ClientConnectionImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
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
            die();
        });
    }

    public void die() {
        String path = System.getProperty("user.dir") + "/server.lock";
        File file = new File(path);
        if (file.exists())
            file.delete();
        Server.getInstance().stopServer();
        ClientConnectionImpl.shouldDie = true;
        if (ClientConnectionImpl.pingerThread != null)
            ClientConnectionImpl.pingerThread.interrupt();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (ClientConnectionImpl.pingerThread != null)
                System.out.println("alive" +
                        ClientConnectionImpl.pingerThread.isAlive()
                );
        }).start();
        Platform.exit();
        System.exit(0); // todo care later
    }

    public static StageCoordinator getInstance() {
        return stageCoordinator;
    }


    public void switchToServerMain() {
        var viewName = "ServerMain";

        loadView(viewName);
    }

    public void switchToAnnouncement() {
        var viewName = "AnnouncementView";
        loadView(viewName);
    }

    public void switchToServerStats() {
        var viewName = "ServerStats";

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
