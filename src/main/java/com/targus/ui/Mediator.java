package com.targus.ui;

import com.targus.algorithm.ga.TerminalState;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSNPrototype;
import com.targus.ui.controllers.*;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;

import java.util.ArrayList;

public class Mediator {
    private InputsController inputsController;
    private SimplifiedObjectiveValueDisplayController simplifiedObjectiveValueDisplayController;
    private MapController mapController;
    private ProgressBarController progressBarController;
    private AlgorithmSelectionController algorithmSelectionController;
    private ObjectiveValueDisplayController objectiveValueDisplayController;
    private FitnessGraphController fitnessGraphController;

    public void setObjectiveValueDisplayController(ObjectiveValueDisplayController objectiveValueDisplayController) {
        this.objectiveValueDisplayController = objectiveValueDisplayController;
    }

    public void setInputsController(InputsController inputsController) {
        this.inputsController = inputsController;
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

    public void setAlgorithmSelectionController(AlgorithmSelectionController algorithmSelectionController) {
        this.algorithmSelectionController = algorithmSelectionController;
    }

    public void resizeMapPane(double width, double height) {
        mapController.resizePane(width, height);
    }

    public void display() {
//        objectiveValueDisplayController.display();
    }

    public void removeChild(Object child) {
        mapController.removeChild(child);
        display();
    }

    public void removeChildren() {
        mapController.removeChildren();
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

    public OptimizationProblem getOptimizationProblem() {
        return inputsController.getWsnOptimizationProblem();
    }

    public void loadFromFile(ActionEvent event) {
        inputsController.loadFromFile(event);
    }

    public void exportToFile(ActionEvent event) {
        inputsController.exportToFile(event);
    }

    public void solve() {
        inputsController.solve(
                algorithmSelectionController.getAlgorithm(),
                algorithmSelectionController.getMutation(),
                algorithmSelectionController.getMutationRate(),
                algorithmSelectionController.getTermination(),
                algorithmSelectionController.getTerminationValue());
    }

    public void clean() {
        inputsController.cleanSolution();
    }

    public void simplifiedDisplay(double weightedSensorValue, double weightedMConnValue, double weightedKCovValue) {
        simplifiedObjectiveValueDisplayController.simplifiedDisplay(weightedSensorValue, weightedMConnValue, weightedKCovValue);
    }

    public void createProblemInstance(WSNPrototype wsnPrototype, int distance, int numberNodes) {
        inputsController.createProblemInstance(wsnPrototype, distance, numberNodes);
    }

    public void setProgressBarVisible(boolean visible) {
        progressBarController.setProgressBarVisible(visible);
    }

    public void addTargetToPane(Target target) {
        mapController.addTargetToPane(target);
    }

    public void addPotentialPositionToPane(PotentialPosition potentialPosition) {
        mapController.addPotentialPositionToPane(potentialPosition);
    }

    public void displayNonApplicable() {
//        objectiveValueDisplayController.displayNonApplicable();
    }

    public void simplifiedDisplayNonApplicable() {
        simplifiedObjectiveValueDisplayController.simplifiedDisplayNonApplicable();
    }

    public void displaySensors(ArrayList<Sensor> sensors) {
        mapController.addSensorsToPane(sensors);
    }

    public ObjectiveValueDisplayController getObjectiveValueDisplayController() {
        return objectiveValueDisplayController;
    }

    public void updateGraph(double fitness) {
        fitnessGraphController.updateFitness(fitness);
    }

    public void setFitnessGraphController(FitnessGraphController fitnessGraphController) {
        this.fitnessGraphController = fitnessGraphController;
    }

    public void configureChart(TerminalState terminalState, String terminationType) {
        fitnessGraphController.configureChart(terminalState, terminationType);
    }

    public void removeSensorsFromPane() {
        mapController.removeSensors();
    }

    public void displaySolution(Solution oldSolution, Solution newSolution) {
        inputsController.displaySolution(oldSolution, newSolution);
    }

    public void addOrRemoveSensor(Sensor sensor) {
        mapController.addOrRemoveSensor(sensor);
    }
}
