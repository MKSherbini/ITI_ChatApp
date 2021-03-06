package iti.jets.gfive;

import iti.jets.gfive.AIML.BotsManager;
import iti.jets.gfive.common.CustomLogger;
import iti.jets.gfive.common.IPConn;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.ClientConnectionService;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import iti.jets.gfive.ui.helpers.LoginManager;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.serialization.Marshaltor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.Scanner;
//import org.alicebot.ab.configuration.BotConfiguration;

public class Main extends Application {

    public static void main(String[] args) {
        System.out.println("Welcome to our error free app");
        if (args.length > 0) {
            System.out.println("Client on IP: " + args[0]);
            IPConn.IP = args[0];
        }

        if (args.length > 1) {
            CustomLogger.isDebugMode = args[1].startsWith("T");
        }

        if (!CustomLogger.isDebugMode) {
            System.setErr(CustomLogger.sout);
            System.setOut(CustomLogger.sout);
        }
        System.out.println("dead");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.initStage(primaryStage);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/iti/jets/gfive/images/icon.png")));
        LoginManager loginManager = LoginManager.getInstance();
        if (loginManager == null) return;


        UserDBCrudInter userServices = UserDBCrudService.getUserService();
        if (userServices == null) {
            primaryStage.show();
            StageCoordinator.getInstance().reset();
            return;
        }


        // check if the can login returned true this meaning that user just exit
        // and his password and phone number saved and can enter
        if (loginManager.canLogin()) {
            //implicitly login the user
            loginManager.initCurrentUser();
            //redirect user to main screen
            stageCoordinator.switchToMainPage();

        } else {
            // redirect user to login screen
            stageCoordinator.switchToLoginPage();
        }


        primaryStage.show();
        //todo unregister and unexport but which obj??
        primaryStage.setOnCloseRequest(ae -> {
            if (ModelsFactory.getInstance().getCurrentUserModel().getUsername() != null) {
                StageCoordinator.getInstance().close();
            }

            Platform.exit();
            //StageCoordinator.getInstance().die();
//            StageCoordinator.getInstance().unregisterCurrentUser(true);
//            Platform.exit();
        });
//        var m = Marshaltor.getInstance();
//        m.marshalChat();
//        Platform.exit();
    }

    private void botsDemo() {
        BotsManager botsManager = BotsManager.getInstance();
        botsManager.initBotChats();
        botsManager.printBots();
        Scanner s = new Scanner(System.in);
        while (true) {
            var userMsg = s.nextLine();
            System.out.println("userMsg = " + userMsg);
            var botMsg = botsManager.askBots(userMsg);
            System.out.println("botMsg = " + botMsg);
        }
    }

//    public static boolean isValidEmailAddress(String email) {
//        boolean result = true;
//        try {
//            InternetAddress emailAddr = new InternetAddress(email);
//            emailAddr.validate();
//        } catch (AddressException ex) {
//            result = false;
//        }
//        return result;
//    }


    @Override
    public void init() {
        // Initialize Database & Network Connections
    }

    @Override
    public void stop() {
        // Terminate Database & Network Connections
    }

}
