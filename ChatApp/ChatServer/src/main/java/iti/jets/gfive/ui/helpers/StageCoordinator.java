package iti.jets.gfive.ui.helpers;

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

    private StageCoordinator() { }

    public void initStage(Stage stage) {
        if (primaryStage != null) {
            throw new IllegalArgumentException("The Stage Already been initialized");
        }
        primaryStage = stage;
    }

    public static StageCoordinator getInstance() {
        return stageCoordinator;
    }

    public void switchToLoginScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }

        if (!scenes.containsKey("login")) {
            try {
                System.out.println("Created New Scene");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/LoginView.fxml"));
                Parent loginView = fxmlLoader.load();
                Scene loginScene = new Scene(loginView);
                SceneData loginSceneData = new SceneData(fxmlLoader, loginView, loginScene);
                scenes.put("login", loginSceneData);
                primaryStage.setScene(loginScene);
            } catch (IOException e) {
                System.out.println("IO Exception: Couldn't load 'Login View' FXML file");
            }
        } else {
            System.out.println("Loaded Existing Scene");
            SceneData loginSceneData = scenes.get("login");
            Scene loginScene = loginSceneData.getScene();
            primaryStage.setScene(loginScene);
        }

    }

    public void switchToSignupScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }

        if (!scenes.containsKey("signup")) {
            try {
                System.out.println("Created New Scene");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/SignupView.fxml"));
                Parent signupView = fxmlLoader.load();
                Scene signupScene = new Scene(signupView);
                SceneData signupSceneData = new SceneData(fxmlLoader, signupView, signupScene);
                scenes.put("signup", signupSceneData);
                primaryStage.setScene(signupScene);
            } catch (IOException e) {
                System.out.println("IO Exception: Couldn't load 'Signup View' FXML file");
            }
        } else {
            System.out.println("Loaded Existing Scene");
            SceneData signupSceneData = scenes.get("signup");
            Scene signupScene = signupSceneData.getScene();
            primaryStage.setScene(signupScene);
        }
    }

    public void switchToChatScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }

        if (!scenes.containsKey("chat")) {
            try {
                System.out.println("Created New Scene");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iti/jets/gfive/views/ChatView.fxml"));
                Parent chatView = fxmlLoader.load();
                Scene chatScene = new Scene(chatView);
                SceneData chatSceneData = new SceneData(fxmlLoader, chatView, chatScene);
                scenes.put("chat", chatSceneData);
                primaryStage.setScene(chatScene);
            } catch (IOException e) {
                System.out.println("IO Exception: Couldn't load 'Chat View' FXML file");
            }
        } else {
            System.out.println("Loaded Existing Scene");
            SceneData chatSceneData = scenes.get("chat");
            Scene chatScene = chatSceneData.getScene();
            primaryStage.setScene(chatScene);
        }
    }

}
