package iti.jets.gfive.ui.controllers;

import com.jfoenix.controls.JFXButton;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.helpers.StatsManager;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServerStatsController implements Initializable {
    public PieChart chart_genderStats;
    public PieChart chart_countryStats;
    public Label lbl_caption;
    public JFXButton btn_test;
    public JFXButton btn_test2;
    public JFXButton btn_back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_caption.setStyle("-fx-font: 16 arial;");

        StatsManager.getInstance().updateStats();
        InitGenderStats();
        InitCountryStats();

        btn_test.setOnAction(event -> {
            var statsModel = ModelsFactory.getInstance().getStatsModel();
//            statsModel.setMalesCount(statsModel.getMalesCount() + 1);
            statsModel.getCountryPropertiesMap().get("Unspecified").set(
                    statsModel.getCountryPropertiesMap().get("Unspecified").get() + 1
            );
        });
        btn_test2.setOnAction(event -> {
            var statsModel = ModelsFactory.getInstance().getStatsModel();
//            statsModel.setFemalesCount(statsModel.getFemalesCount() + 1);
            statsModel.getCountryPropertiesMap().get("Albania").set(
                    statsModel.getCountryPropertiesMap().get("Albania").get() + 1
            );
        });
        btn_back.setOnAction(event -> {
            StageCoordinator.getInstance().switchToServerMain();
        });

    }

    private void InitCountryStats() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        var statsModel = ModelsFactory.getInstance().getStatsModel();
        var dataList = statsModel.getCountryPropertiesMap().entrySet().stream()
                .map(entry -> {
                    var data = new PieChart.Data(entry.getKey(), entry.getValue().get());
                    data.pieValueProperty().bind(entry.getValue());
                    return data;
                })
                .collect(Collectors.toList());
        pieChartData.addAll(dataList);

        setupPieChart(pieChartData, chart_countryStats, "Country Stats");

        DoubleBinding totalBinding = new DoubleBinding() {

            {
                pieChartData.forEach(data -> {
                    super.bind(data.pieValueProperty());
                });
            }

            @Override
            protected double computeValue() {
                return pieChartData.stream().mapToDouble(PieChart.Data::getPieValue).sum();
            }
        };

        addCaption(chart_countryStats, totalBinding);
    }

    private void InitGenderStats() {
        var maleData = new PieChart.Data("Males", 0);
        var femaleData = new PieChart.Data("Females", 0);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.addAll(maleData, femaleData);

        var statsModel = ModelsFactory.getInstance().getStatsModel();
        maleData.pieValueProperty().bind(statsModel.malesCountProperty());
        femaleData.pieValueProperty().bind(statsModel.femalesCountProperty());

        setupPieChart(pieChartData, chart_genderStats, "Gender Stats");

        DoubleBinding total = Bindings.createDoubleBinding(() ->
                        pieChartData.stream().collect(Collectors.summingDouble(PieChart.Data::getPieValue)),
                maleData.pieValueProperty(), femaleData.pieValueProperty());

        addCaption(chart_genderStats, total);

    }

    private void setupPieChart(ObservableList<PieChart.Data> pieChartData, PieChart chart_countryStats, String s) {
        chart_countryStats.setData(pieChartData);
        chart_countryStats.setTitle(s);
        chart_countryStats.setClockwise(true);
        chart_countryStats.setLabelLineLength(20);
        chart_countryStats.setLabelsVisible(true);
        chart_countryStats.setStartAngle(180);
    }

    private void addCaption(PieChart chart, DoubleBinding total) {
        SimpleLongProperty waitTime = new SimpleLongProperty(System.currentTimeMillis());

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        lbl_caption.setTranslateX(e.getSceneX());
                        lbl_caption.setTranslateY(e.getSceneY());
                        String text = String.format("Count: %s, Ratio %.1f%%", data.getPieValue(), 100.0 * data.getPieValue() / total.get());
                        System.out.println("text = " + text);
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
