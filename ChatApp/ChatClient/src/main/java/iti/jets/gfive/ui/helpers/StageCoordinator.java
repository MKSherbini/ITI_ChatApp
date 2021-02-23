package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.ClientConnectionService;
import iti.jets.gfive.services.ContactDBCrudService;
import iti.jets.gfive.ui.controllers.LoginController;
import iti.jets.gfive.ui.controllers.MainScreenController;
import iti.jets.gfive.ui.controllers.RegisterController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StageCoordinator {
    //    private boolean closed = false;
    boolean registered = false;
    private static Stage primaryStage;
    private static final StageCoordinator stageCoordinator = new StageCoordinator();
    private final Map<String, SceneData> scenes = new HashMap<>();
    private boolean hasServerErrors;
    private boolean switchingToError;

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
        if (hasServerErrors)
            if (!viewName.equals("ErrorView"))
                return;


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
        System.out.println("loaded " + viewName);
    }


    public void switchToMainPage() {
        var viewName = "MainScreenView";
        loadView(viewName);
    }

    public void switchToErrorPage() {
        var viewName = "ErrorView";
        loadView(viewName);
    }

    public void reset() {
        System.out.println("Resetting");
        hasServerErrors = true;
        unregisterCurrentUser(true);
        switchToErrorPage();
//        Platform.exit();
//        scenes.clear();
//        switchToLoginPage();
    }

    // todo fix this shit
    // This method unregister the user from the server
    public void unregisterCurrentUser(boolean force) {
        if (force) {
            try {
                // make sure this dies
                UnicastRemoteObject.unexportObject(NotificationMsgHandler.getInstance(), true);
            } catch (NoSuchObjectException noSuchObjectException) {
                noSuchObjectException.printStackTrace();
                // idfc
            }
        }

        if (!registered) {
            return;
        }
        registered = false;
        ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
        try {
            UserDto user = new UserDto(ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber(), ModelsFactory.getInstance().getCurrentUserModel().getUsername(), ModelsFactory.getInstance().getCurrentUserModel().getStatus());
            user.setImage(ModelsFactory.getInstance().getCurrentUserModel().getImage());
            clientConnectionInter.puplishStatus(user);
            clientConnectionInter.unregister(NotificationMsgHandler.getInstance());
            // force will be true only on exit and close
            if (force) {
                UnicastRemoteObject.unexportObject(NotificationMsgHandler.getInstance(), true);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            // calmly squashing the no server error
            // todo until another method emerges I'm doing it this way...
            try {
                // make sure this dies
                UnicastRemoteObject.unexportObject(NotificationMsgHandler.getInstance(), true);
            } catch (NoSuchObjectException noSuchObjectException) {
                noSuchObjectException.printStackTrace();
            }

            reset();
        }
    }


    public void registerUser(UserDto userDto) {
        if (registered) return;
        registered = true;
        ClientConnectionInter clientConnectionInter = ClientConnectionService.getClientConnService();
        try {
            NotificationMsgHandler notify = NotificationMsgHandler.getInstance();
//            UnicastRemoteObject.exportObject(notify, 8000);
            clientConnectionInter.register(userDto, notify);

        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }
    }

    public void changeStatus(UserDto user) {
        System.out.println(user.getPhoneNumber() + "-----------> " + user.getStatus());
//        ( (MainScreenController)scenes.get("MainScreenView").getLoader().getController()).changeContactStatus(user);
        ArrayList<UserDto> contacts = null;
        try {
            contacts = ContactDBCrudService.getContactService().getContactsList(ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber());
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }

        ContactsListView c = ContactsListView.getInstance();
        c.fillContacts(contacts);
    }
}
