package iti.jets.gfive;

import iti.jets.gfive.common.CustomLogger;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Scanner;
//import org.alicebot.ab.configuration.BotConfiguration;

public class Main extends Application {

    public static void main(String[] args) {
        System.out.println("Welcome to our error free app");
        //System.setErr(CustomLogger.sout);
        //System.setOut(CustomLogger.sout);
        System.out.println("dead");
        String path = System.getProperty("user.dir") + "/server.lock";
        File file = new File(path);
        if (file.exists()) {
            return;
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.initStage(primaryStage);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/iti/jets/gfive/images/icon.png")));
        stageCoordinator.switchToServerMain();
//        stageCoordinator.switchToServerStats();
        primaryStage.show();
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
