package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StatsManager;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServerStatsController implements Initializable {
    public PieChart chart_genderStats;
    public Label lbl_caption;
    public JFXButton btn_test;
    public JFXButton btn_test2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitGenderStats();
        btn_test.setOnAction(event -> {
            var statsModel = ModelsFactory.getInstance().getStatsModel();
            statsModel.setMalesCount(statsModel.getMalesCount() + 1);
        });
        btn_test2.setOnAction(event -> {
            var statsModel = ModelsFactory.getInstance().getStatsModel();
            statsModel.setFemalesCount(statsModel.getFemalesCount() + 1);
        });

        StatsManager.getInstance().initStats();
    }

    private void InitGenderStats() {
//        ObservableList<PieChart.Data> pieChartData =
//                FXCollections.observableArrayList(value -> new Observable[]{value.pieValueProperty()});

        var maleData = new PieChart.Data("Males", 10);
        var femaleData = new PieChart.Data("Females", 22);
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//                new PieChart.Data("Males", 10),
//                new PieChart.Data("Females", 22));
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.addAll(maleData, femaleData);
        var statsModel = ModelsFactory.getInstance().getStatsModel();
        maleData.pieValueProperty().bind(statsModel.malesCountProperty());
        femaleData.pieValueProperty().bind(statsModel.femalesCountProperty());

        chart_genderStats.setData(pieChartData);
        chart_genderStats.setTitle("Gender Stats");
        chart_genderStats.setClockwise(true);
        chart_genderStats.setLabelLineLength(20);
        chart_genderStats.setLabelsVisible(true);
        chart_genderStats.setStartAngle(180);

        lbl_caption.setStyle("-fx-font: 16 arial;");

        DoubleBinding total = Bindings.createDoubleBinding(() ->
                        pieChartData.stream().collect(Collectors.summingDouble(PieChart.Data::getPieValue)),
                maleData.pieValueProperty(), femaleData.pieValueProperty());


        SimpleLongProperty waitTime = new SimpleLongProperty(System.currentTimeMillis());

        for (final PieChart.Data data : chart_genderStats.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        lbl_caption.setTranslateX(e.getSceneX());
                        lbl_caption.setTranslateY(e.getSceneY());
                        String text = String.format("Count: %s, Ratio %.1f%%", data.getPieValue(), 100.0 * data.getPieValue() / total.get());
                        lbl_caption.setText(text);
                        lbl_caption.setVisible(true);
                        waitTime.set(System.currentTimeMillis() + 1000);
                        new Thread(() -> {
                            try {
                                Thread.sleep(waitTime.get() - System.currentTimeMillis());
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
//                            System.out.println(waitTime.get());
//                            System.out.println(System.currentTimeMillis());
                            if (waitTime.get() <= System.currentTimeMillis() + 50) {
//                                System.out.println("disable");
                                Platform.runLater(() -> lbl_caption.setVisible(false));
                            } else {
//                                System.out.println("no disable yet");
                            }
                        }).start();
//                        System.out.println(statsModel.getFemalesCount());
//                        System.out.println(statsModel.getMalesCount());
//                        System.out.println(data.getPieValue());
//                        System.out.println(total.get());
                    }
            );
        }

    }
}
