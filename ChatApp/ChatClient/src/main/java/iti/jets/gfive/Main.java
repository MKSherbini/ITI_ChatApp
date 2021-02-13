package iti.jets.gfive;

import iti.jets.gfive.AIML.BotsManager;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Scanner;
//import org.alicebot.ab.configuration.BotConfiguration;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {

        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.initStage(primaryStage);
        stageCoordinator.switchToLoginPage();
        primaryStage.show();
        //todo unregister and unexport but which obj??
//        botsDemo();

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
