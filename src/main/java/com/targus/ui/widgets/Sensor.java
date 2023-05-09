package com.targus.ui.widgets;

import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;

public class Sensor extends Circle {
    private int respectivePotentialPositionIndex;
    private static double COMMUNICATION_RADIUS;
    private static double SENSING_RADIUS;
    private static boolean STATIC_VARIABLES_INITIALIZED = false;
    private static boolean communicationRangeVisible = true;
    private static boolean sensingRangeVisible = true;
    private static final ArrayList<Sensor> allSensors = new ArrayList<>();
    private static final HashMap<Integer, Sensor> sensorHashMap = new HashMap<>();
    private final Circle communicationRange;
    private final Circle sensingRange;

    public Sensor(double centerX, double centerY, int respectivePotentialPositionIndex) {
        if (!staticVariablesInitialized()) {
            throw new IllegalStateException("Radii are not initialized.");
        }
        this.respectivePotentialPositionIndex = respectivePotentialPositionIndex;

        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(4.0);
        setFill(Color.GREEN);

        communicationRange = initializeCommunicationRange(centerX, centerY);
        sensingRange = initializeSensingRange(centerX, centerY);

        parentProperty().addListener(this::changed);

        allSensors.add(this);
        if (!sensorHashMap.containsKey(respectivePotentialPositionIndex)) {
            sensorHashMap.put(respectivePotentialPositionIndex, this);
        }
    }

    public static void initializeRadii(double communicationRadius, double sensingRadius) {
        setCommunicationRadius(communicationRadius);
        setSensingRadius(sensingRadius);
        STATIC_VARIABLES_INITIALIZED = true;
    }

    private static boolean staticVariablesInitialized() {
        return COMMUNICATION_RADIUS != 0.0
                && SENSING_RADIUS != 0.0;
    }

    public static void setCommunicationRangeVisibility(boolean visible) {
        communicationRangeVisible = visible;
        for (Sensor sensor : allSensors) {
            sensor.getCommunicationRange().setVisible(visible);
        }
    }

    public static void setSensingRangeVisibility(boolean visible) {
        sensingRangeVisible = visible;
        for (Sensor sensor : allSensors) {
            sensor.getSensingRange().setVisible(visible);
        }
    }

    private Circle initializeCommunicationRange(double centerX, double centerY) {
        Circle communicationRange = new Circle(centerX, centerY, COMMUNICATION_RADIUS);
        communicationRange.setStroke(Paint.valueOf("#FFA07A"));
        communicationRange.setFill(Paint.valueOf("#FFF5E6"));
        communicationRange.setBlendMode(BlendMode.SRC_OVER);
        communicationRange.setMouseTransparent(true);
        communicationRange.setVisible(communicationRangeVisible);
        communicationRange.setViewOrder(3);
        return communicationRange;
    }

    private Circle initializeSensingRange(double centerX, double centerY) {
        Circle sensingRange = new Circle(centerX, centerY, SENSING_RADIUS);
        sensingRange.setStroke(Paint.valueOf("#6597B8"));
        sensingRange.setFill(Paint.valueOf("#D1EAF2"));
        sensingRange.setBlendMode(BlendMode.SRC_OVER);
        sensingRange.setMouseTransparent(true);
        sensingRange.setVisible(sensingRangeVisible);
        sensingRange.setViewOrder(3);
        return sensingRange;
    }

    public double getCommunicationRadius() {
        return COMMUNICATION_RADIUS;
    }

    public static void setCommunicationRadius(double communicationRadius) {
        COMMUNICATION_RADIUS = communicationRadius;
    }

    public double getSensingRadius() {
        return SENSING_RADIUS;
    }

    public static void setSensingRadius(double sensingRadius) {
        SENSING_RADIUS = sensingRadius;
    }

    public Circle getCommunicationRange() {
        return communicationRange;
    }

    public Circle getSensingRange() {
        return sensingRange;
    }

    public static ArrayList<Sensor> getAllSensors() {
        return allSensors;
    }

    private void changed(ObservableValue<? extends Parent> observable, Parent oldParent, Parent newParent) {
        if (newParent != null) {
            Pane pane = (Pane) newParent;
            pane.getChildren().add(communicationRange);
            pane.getChildren().add(sensingRange);
        }
    }

    public void removeRangesFromPane(Pane pane) {
        pane.getChildren().remove(communicationRange);
        pane.getChildren().remove(sensingRange);
    }

    public static void removeSensorFromAllSensorsList(Sensor sensor) {
        allSensors.remove(sensor);
    }

    public static Sensor retrieveSensorByIndex(int respectivePotentialPositionIndex) {
        return sensorHashMap.get(respectivePotentialPositionIndex);
    }

    public static void removeSensorFromSensorHashSet(Sensor sensor) {
        sensorHashMap.remove(sensor.respectivePotentialPositionIndex);
    }

    public int getRespectivePotentialPositionIndex() {
        return respectivePotentialPositionIndex;
    }
}
