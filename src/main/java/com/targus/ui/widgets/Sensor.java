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

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Sensor extends Circle {
    private final int respectivePotentialPositionIndex;
    private static double COMMUNICATION_RADIUS;
    private static double SENSING_RADIUS;
    private boolean sensorVisible = false;
    private boolean communicationRangeVisible = false;
    private boolean sensingRangeVisible = false;
    private static final ConcurrentHashMap<Integer, Sensor> sensorHashMap = new ConcurrentHashMap<>();
    private Circle communicationRange;
    private Circle sensingRange;

    public Sensor(double centerX, double centerY, int respectivePotentialPositionIndex) {
        if (!staticVariablesInitialized()) {
            throw new IllegalStateException("Radii are not initialized.");
        }
        this.respectivePotentialPositionIndex = respectivePotentialPositionIndex;

        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(4.0);
        setFill(Color.GREEN);

        setVisible(sensorVisible);
        communicationRange = initializeCommunicationRange(centerX, centerY);
        sensingRange = initializeSensingRange(centerX, centerY);
        communicationRange.setVisible(false);
        sensingRange.setVisible(false);

        parentProperty().addListener(this::changed);
    }

    public static Collection<Sensor> fillPotentialPositions(Point2D[] potentialPositionArray) {
        for (int i = 0; i < potentialPositionArray.length; i++) {
            if (!sensorHashMap.containsKey(i)) {
                Point2D point2D = potentialPositionArray[i];
                sensorHashMap.put(i, new Sensor(point2D.getX(), point2D.getY(), i));
            }
        }
        return sensorHashMap.values();
    }

    public static void initializeRadii(double communicationRadius, double sensingRadius) {
        setCommunicationRadius(communicationRadius);
        setSensingRadius(sensingRadius);
    }

    private static boolean staticVariablesInitialized() {
        return COMMUNICATION_RADIUS != 0.0
                && SENSING_RADIUS != 0.0;
    }

    public void setCommunicationRangeVisibility(boolean visible) {
        communicationRangeVisible = visible;
        Collection<Sensor> sensors = new ConcurrentLinkedQueue<>(sensorHashMap.values());
        for (Sensor sensor : sensors) {
            sensor.getCommunicationRange().setVisible(visible);
        }
    }

    public void setSensingRangeVisibility(boolean visible) {
        sensingRangeVisible = visible;
        Collection<Sensor> sensors = new ConcurrentLinkedQueue<>(sensorHashMap.values());
        for (Sensor sensor : sensors) {
            sensor.getSensingRange().setVisible(visible);
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

    public Circle getCommunicationRange() {
        return communicationRange;
    }

    public Circle getSensingRange() {
        return sensingRange;
    }

    public static ConcurrentHashMap<Integer, Sensor> getSensorHashMap() {
        return sensorHashMap;
    }

    private void changed(ObservableValue<? extends Parent> observable, Parent oldParent, Parent newParent) {
        if (newParent != null) {
            Pane pane = (Pane) newParent;
            pane.getChildren().add(communicationRange);
            pane.getChildren().add(sensingRange);
        }
    }

    public static Sensor retrieveSensorFromHashMapByIndex(int respectivePotentialPositionIndex) {
        return sensorHashMap.get(respectivePotentialPositionIndex);
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
}
