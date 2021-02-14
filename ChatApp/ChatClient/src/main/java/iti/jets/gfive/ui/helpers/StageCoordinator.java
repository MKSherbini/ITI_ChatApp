package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.ui.controllers.LoginController;
import iti.jets.gfive.ui.controllers.RegisterController;
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
    }

    public static StageCoordinator getInstance() {
        return stageCoordinator;
    }

    public void switchToLoginPage() {
        var viewName = "LoginView";

        if (scenes.containsKey(viewName)) {
            LoginController controller = scenes.get(viewName).getLoader().getController();
            controller.validateFields();
        }
        loadView(viewName);
    }

    public void switchToRegisterPage() {
        var viewName = "RegisterView";

        if (scenes.containsKey(viewName)) {
            RegisterController controller = scenes.get(viewName).getLoader().getController();
            controller.resetFields();
        }
        loadView(viewName);
    }

    public void switchToUpdateProfilePage() {
        var viewName = "UpdateProfileView";

        loadView(viewName);
    }

    public void switchToProfilePage() {
        var viewName = "ProfileView";

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


    public void switchToMainPage() {
        var viewName = "MainScreenView";
        loadView(viewName);
    }
}
