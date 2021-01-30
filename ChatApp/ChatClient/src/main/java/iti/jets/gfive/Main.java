package iti.jets.gfive;

import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileFilter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
//import org.alicebot.ab.configuration.BotConfiguration;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {

        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.initStage(primaryStage);
        stageCoordinator.switchToLoginScene();
        primaryStage.show();

//        String botname = "super";
//        String path = "src/main/resources/iti/jets/gfive";
////        String path = "iti/jets/gfive/bots";
//        Bot bot = new Bot(botname, path);
////        System.out.println(bot.getAimlPath());
////        bot.getProperties().forEach((s, s2) -> System.out.println(s + ": " + s2));
//
//        var bots = createBots();
//        System.out.printf("Found %s Bots: ", bots.size());
//        bots.forEach(bot -> System.out.print(bot.getName() + " "));
//        System.out.println();

//        var userMsg = "how are you";
//        System.out.println("userMsg: " + userMsg);

//        for (Bot bot : bots) {
//            var chatSession = new Chat(bot);
//            String answer = chatSession.multisentenceRespond(userMsg);
//
//            if (!answer.equals("I have no answer for that.")) {
//                System.out.println("botMsg: " + answer);
//                break;
//            }
//        }
//        var chatSession = new Chat(bots.get(1));
//        Scanner s = new Scanner(System.in);
//        while (true) {
////            var userMsg = "how are you";
//            var userMsg = s.nextLine();
//            System.out.println("userMsg: " + userMsg);
//            String answer = chatSession.multisentenceRespond(userMsg);
//            System.out.println("botMsg: " + answer);
//        }
//        Platform.exit();


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

    public List<Bot> createBots() {
        List<Bot> bots = new ArrayList<>();
        var botPath = "src/main/resources/iti/jets/gfive";
        File[] directories = new File(botPath + "/bots")
                .listFiles(File::isDirectory);

        if (directories != null) {
            bots = Arrays.stream(directories)
                    .map(File::getName)
                    .map(name -> new Bot(name, botPath))
                    .collect(Collectors.toList());
//                    .forEach(bot -> bot.getProperties().forEach((s, s2) -> System.out.println(s + ": " + s2)));
        }
        return bots;
    }

    @Override
    public void init() {
        // Initialize Database & Network Connections
    }

    @Override
    public void stop() {
        // Terminate Database & Network Connections
    }

}
