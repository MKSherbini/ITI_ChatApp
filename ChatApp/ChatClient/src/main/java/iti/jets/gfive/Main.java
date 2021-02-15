package iti.jets.gfive;

import iti.jets.gfive.AIML.BotsManager;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.services.ClientConnectionService;
import iti.jets.gfive.ui.helpers.NotificationMsgHandler;
import iti.jets.gfive.ui.helpers.LoginManager;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.serialization.Marshaltor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.Scanner;
//import org.alicebot.ab.configuration.BotConfiguration;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        var m = Marshaltor.getInstance();
        m.marshalChat();
        Platform.exit();
//        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
//        stageCoordinator.initStage(primaryStage);
//        LoginManager loginManager = LoginManager.getInstance();
//        // check if the can login returned true this meaning that user just exit
//        // and his password and phone number saved and can enter
//        if (loginManager.canLogin()){
//            //implicitly login the user
//            loginManager.initCurrentUser();
//            //redirect user to main screen
//            stageCoordinator.switchToMainPage();
//
//        }else{
//            // redirect user to login screen
//            stageCoordinator.switchToLoginPage();
//        }
//
//        primaryStage.show();
//        //todo unregister and unexport but which obj??
//        primaryStage.setOnCloseRequest(ae ->{
//           StageCoordinator.getInstance().unregisterCurrentUser();
//        });
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
