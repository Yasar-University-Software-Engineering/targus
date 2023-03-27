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
        sensorObjective.setText(String.format("%.3f", weightedSensorValue));
        connectivityObjective.setText(String.format("%.3f", weightedMConnValue));
        coverageObjective.setText(String.format("%.3f", weightedKCovValue));
        double total = weightedSensorValue + weightedMConnValue + weightedKCovValue;
        fitnessValue.setText(String.format("%.3f", total));
    }

    public void simplifiedDisplayNonApplicable() {
        sensorObjective.setText(Constants.NON_APPLICABLE);
        connectivityObjective.setText(Constants.NON_APPLICABLE);
        coverageObjective.setText(Constants.NON_APPLICABLE);
        fitnessValue.setText(Constants.NON_APPLICABLE);
    }
}
