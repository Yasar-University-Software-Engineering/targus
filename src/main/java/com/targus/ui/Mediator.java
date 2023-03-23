package com.targus.ui;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.ui.controllers.*;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.CheckBox;

public class Mediator {
    private InputsController inputsController;
    private ObjectiveValueDisplayController objectiveValueDisplayController;
    private SimplifiedObjectiveValueDisplayController simplifiedObjectiveValueDisplayController;
    private MapController mapController;
    private ProgressBarController progressBarController;
    private CreateProblemInstanceController createProblemInstanceController;

    public void setInputsController(InputsController inputsController) {
        this.inputsController = inputsController;
    }

    public void setObjectiveValueDisplayController(ObjectiveValueDisplayController objectiveValueDisplayController) {
        this.objectiveValueDisplayController = objectiveValueDisplayController;
    }

    public void setSimplifiedObjectiveValueDisplayController(
            SimplifiedObjectiveValueDisplayController simplifiedObjectiveValueDisplayController) {
        this.simplifiedObjectiveValueDisplayController = simplifiedObjectiveValueDisplayController;
    }

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    public void setProgressBarController(ProgressBarController progressBarController) {
        this.progressBarController = progressBarController;
    }

    public void setCreateProblemInstanceController(CreateProblemInstanceController createProblemInstanceController) {
        this.createProblemInstanceController = createProblemInstanceController;
    }

    public void resizeMapPane(double width, double height) {
        mapController.resizePane(width, height);
    }

    public void display() {
        objectiveValueDisplayController.display();
    }

    public void addChild(Object child) {
        mapController.addChild(child);
        display();
    }

    public void removeChild(Object child) {
        mapController.removeChild(child);
        display();
    }

    public void addTarget(Target target) {
        inputsController.addTarget(target);
        display();
    }

    public void addSensor(Sensor sensor) {
        inputsController.addSensor(sensor);
        display();
    }

    public void removeSensor(Sensor sensor) {
        inputsController.removeSensor(sensor);
        display();
    }

    public void removeChildren() {
        mapController.removeChildren();
        display();
    }

    public void addPotentialPosition(PotentialPosition potentialPosition) {
        inputsController.addPotentialPosition(potentialPosition);
        display();
    }

    public void clearTargets() {
        inputsController.clearTargets();
        display();
    }

    public void clearPotentialPositions() {
        inputsController.clearPotentialPositions();
        display();
    }

    public void clearSensors() {
        inputsController.clearSensors();
        display();
    }

    public void resetRegion() {
        mapController.resetRegion();
    }

    public void setProgressLabelText(String text) {
        progressBarController.setProgressLabelText(text);
    }

    public void bindProgressBar(ReadOnlyDoubleProperty property) {
        progressBarController.bindProgressBar(property);
    }

    public Solution getSolution() {
        return inputsController.getSolution();
    }

    public OptimizationProblem getOptimizationProblem() {
        return inputsController.getWsnOptimizationProblem();
    }

    public CheckBox getCommunicationRangeVisibility() {
        return inputsController.getCommunicationRangeVisibility();
    }

    public CheckBox getSensingRangeVisibility() {
        return inputsController.getSensingRangeVisibility();
    }

    public void loadFromFile() {
        inputsController.handleLoadFromFile();
    }

    public void exportToFile() {
        inputsController.handleExportToFile();
    }

    public void solve() {
        inputsController.handleSolve();
    }

    public void clean() {
        inputsController.handleCleanSolution();
    }

    public void simplifiedDisplay(double weightedSensorValue, double weightedMConnValue, double weightedKCovValue) {
        simplifiedObjectiveValueDisplayController.simplifiedDisplay(weightedSensorValue, weightedMConnValue, weightedKCovValue);
    }

    public void createProblemInstance(WSNOptimizationProblem wsnOptimizationProblem, int width, int height) {
        inputsController.createProblemInstance(wsnOptimizationProblem, width, height);
    }
}
