package iti.jets.gfive;

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

import java.sql.SQLException;
import java.util.Scanner;
//import org.alicebot.ab.configuration.BotConfiguration;

public class Main extends Application {

    public static void main(String[] args) {
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
