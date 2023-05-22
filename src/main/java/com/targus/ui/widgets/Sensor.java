package com.targus.ui.widgets;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Sensor extends Circle {
    private static double COMMUNICATION_RADIUS;
    private static double SENSING_RADIUS;
    private static final ArrayList<Sensor> sensorArrayList = new ArrayList<>();
    private final Circle communicationRange;
    private final Circle sensingRange;
    private static HashSet<Integer> turnedOnSensors = new HashSet<>();

    public Sensor(double centerX, double centerY) {
        if (!staticVariablesInitialized()) {
            throw new IllegalStateException("Radii are not initialized.");
        }

        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(4.0);
        setFill(Color.GREEN);

        communicationRange = initializeCommunicationRange(centerX, centerY);
        sensingRange = initializeSensingRange(centerX, centerY);

        setVisible(false);
        communicationRange.setVisible(false);
        sensingRange.setVisible(false);

        parentProperty().addListener(this::changed);
    }

    public static Collection<Sensor> fillPotentialPositions(Point2D[] potentialPositionArray) {
        clearSensorArrayList();
        for (Point2D point2D : potentialPositionArray) {
            sensorArrayList.add(new Sensor(point2D.getX(), point2D.getY()));
        }
        return sensorArrayList;
    }

    public static void initializeRadii(double communicationRadius, double sensingRadius) {
        setCommunicationRadius(communicationRadius);
        setSensingRadius(sensingRadius);
    }

    private static boolean staticVariablesInitialized() {
        return COMMUNICATION_RADIUS != 0.0
                && SENSING_RADIUS != 0.0;
    }

    public static void setCommunicationRangeVisibility(boolean visible) {
        for (Integer index : turnedOnSensors) {
            Platform.runLater(() -> sensorArrayList.get(index).communicationRange.setVisible(visible));
        }
    }

    public static void setSensingRangeVisibility(boolean visible) {
        for (Integer index : turnedOnSensors) {
            Platform.runLater(() -> sensorArrayList.get(index).sensingRange.setVisible(visible));
        }
    }

    private Circle initializeCommunicationRange(double centerX, double centerY) {
        Circle communicationRange = new Circle(centerX, centerY, COMMUNICATION_RADIUS);
        communicationRange.setStroke(Paint.valueOf("#FFA07A"));
        communicationRange.setFill(Paint.valueOf("#FFF5E6"));
        communicationRange.setBlendMode(BlendMode.SRC_OVER);
        communicationRange.setMouseTransparent(true);
        communicationRange.setViewOrder(3);
        return communicationRange;
    }

    private Circle initializeSensingRange(double centerX, double centerY) {
        Circle sensingRange = new Circle(centerX, centerY, SENSING_RADIUS);
        sensingRange.setStroke(Paint.valueOf("#6597B8"));
        sensingRange.setFill(Paint.valueOf("#D1EAF2"));
        sensingRange.setBlendMode(BlendMode.SRC_OVER);
        sensingRange.setMouseTransparent(true);
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

    public static void setTurnedOnSensors(HashSet<Integer> turnedOnSensors) {
        Sensor.turnedOnSensors = turnedOnSensors;
    }

    public static ArrayList<Sensor> getSensorArrayList() {
        return sensorArrayList;
    }

    private void changed(ObservableValue<? extends Parent> observable, Parent oldParent, Parent newParent) {
        if (newParent != null) {
            Pane pane = (Pane) newParent;
            pane.getChildren().add(communicationRange);
            pane.getChildren().add(sensingRange);
        }
    }

    public static Sensor retrieveSensorFromHashMapByIndex(int respectivePotentialPositionIndex) {
        return sensorArrayList.get(respectivePotentialPositionIndex);
    }

    public void turnOn() {
        Platform.runLater(() -> {
            setVisible(true);
            communicationRange.setVisible(true);
            sensingRange.setVisible(true);
        });
    }

    public void turnOff() {
        Platform.runLater(() -> {
            setVisible(false);
            communicationRange.setVisible(false);
            sensingRange.setVisible(false);
        });
    }

    public static void clearSensorArrayList() {
        sensorArrayList.clear();
        turnedOnSensors.clear();
    }

    public void removeRangesFromPane(Pane pane) {
        pane.getChildren().remove(communicationRange);
        pane.getChildren().remove(sensingRange);
    }
}
