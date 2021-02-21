package iti.jets.gfive;

import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
//        stageCoordinator.switchToServerMain();
        stageCoordinator.switchToServerStats();
        primaryStage.show();

        //Preparing ObservbleList object
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//                new PieChart.Data("Iphone 5S", 13),
//                new PieChart.Data("Samsung Grand", 25),
//                new PieChart.Data("MOTO G", 10),
//                new PieChart.Data("Nokia Lumia", 22));
//
//        var stage = primaryStage;
//        //Creating a Pie chart
//        PieChart pieChart = new PieChart(pieChartData);
//
//        //Setting the title of the Pie chart
//        pieChart.setTitle("Mobile Sales");
//
//        //setting the direction to arrange the data
//        pieChart.setClockwise(true);
//
//        //Setting the length of the label line
//        pieChart.setLabelLineLength(50);
//
//        //Setting the labels of the pie chart visible
//        pieChart.setLabelsVisible(true);
//
//        //Setting the start angle of the pie chart
//        pieChart.setStartAngle(180);
//
//        //Creating a Group object
//        VBox root = new VBox(pieChart);
//
//        //Creating a scene object
//        Scene scene = new Scene(root, 600, 400);
//
//        //Setting title to the Stage
//        stage.setTitle("Pie chart");
//
//        //Adding scene to the stage
//        stage.setScene(scene);
//
//        //Displaying the contents of the stage
//        stage.show();
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
