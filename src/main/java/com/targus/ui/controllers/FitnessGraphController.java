package com.targus.ui.controllers;

import com.targus.algorithm.ga.TerminalState;
import com.targus.ui.Mediator;
import com.targus.utils.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class FitnessGraphController {
    @FXML
    private LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> series;
    private TerminalState terminalState;

    private Mediator mediator;

    public void updateFitness(long currentState, double fitnessValue) {
        Platform.runLater(() -> series.getData().add(new XYChart.Data<>(currentState, fitnessValue)));
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void configureChart(TerminalState terminalState, String terminationType) {
        this.terminalState = terminalState;

        chart.getData().clear();
        series = new XYChart.Series<>();
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        chart.getData().add(series);
        xAxis.setUpperBound(terminalState.getFinishState());

        if (terminationType.equals(Constants.ITERATION_BASED)) {
            xAxis.setLabel("Iteration");
            xAxis.setTickUnit((double) terminalState.getFinishState() / 10);
        } else if (terminationType.equals(Constants.TIME_BASED)) {
            xAxis.setLabel("Time (ms)");
            xAxis.setTickUnit(10000);
        }
    }
}
