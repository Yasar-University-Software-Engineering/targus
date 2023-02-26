package com.targus.ui;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.ui.controllers.EmptyController;
import com.targus.ui.controllers.InformativeController;
import com.targus.ui.controllers.InputsController;
import com.targus.ui.controllers.MapController;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import javafx.beans.property.ReadOnlyDoubleProperty;

public class Mediator {
    private InputsController inputsController;
    private InformativeController informativeController;
    private MapController mapController;
    private EmptyController emptyController;

    public void setInputsController(InputsController inputsController) {
        this.inputsController = inputsController;
    }

    public void setInformativeController(InformativeController informativeController) {
        this.informativeController = informativeController;
    }

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    public void setEmptyController(EmptyController emptyController) {
        this.emptyController = emptyController;
    }

    public void resizeMapPane(double width, double height) {
        mapController.resizePane(width, height);
    }

    public void display() {
        informativeController.display();
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
        emptyController.setProgressLabelText(text);
    }

    public void bindProgressBar(ReadOnlyDoubleProperty property) {
        emptyController.bindProgressBar(property);
    }

    public Solution getSolution() {
        return inputsController.getSolution();
    }

    public OptimizationProblem getOptimizationProblem() {
        return inputsController.getOptimizationProblem();
    }
}
