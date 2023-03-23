package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import javafx.scene.control.Label;

public class SimplifiedObjectiveValueDisplayController {
    public Label sensorObjective;
    public Label connectivityObjective;
    public Label coverageObjective;
    public Label fitnessValue;
    private Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void simplifiedDisplay(double weightedSensorValue, double weightedMConnValue, double weightedKCovValue) {
        sensorObjective.setText("Sensor objective: " + weightedSensorValue);
        connectivityObjective.setText("Connectivity objective: " + weightedMConnValue);
        coverageObjective.setText("Coverage objective: " + weightedKCovValue);
        double fitnessValue = weightedSensorValue + weightedMConnValue + weightedKCovValue;
        coverageObjective.setText("Fitness value: " + fitnessValue);
    }
}
