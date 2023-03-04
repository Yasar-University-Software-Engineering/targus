package com.targus.ui.widgets;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sensor extends Group {
    private static double COMMUNICATION_RADIUS;
    private static double SENSING_RADIUS;
    private static boolean STATIC_VARIABLES_INITIALIZED = false;
    private Circle sensor;

    public static void initializeRadii(double communicationRadius, double sensingRadius) {
        setCommunicationRadius(communicationRadius);
        setSensingRadius(sensingRadius);
        STATIC_VARIABLES_INITIALIZED = true;
    }

    private static boolean staticVariablesInitialized() {
        return COMMUNICATION_RADIUS != 0.0
                && SENSING_RADIUS != 0.0;
    }

    public Sensor(double centerX, double centerY) {
        if (!staticVariablesInitialized()) {
            throw new IllegalStateException("Radii are not initialized.");
        }

        sensor = new Circle(centerX, centerY, 4);
        sensor.setFill(Color.GREEN);

        Circle communicationRange = initializeCommunicationRange(centerX, centerY);
        Circle sensingRange = initializeSensingRange(centerX, centerY);

        getChildren().addAll(sensor, sensingRange, communicationRange);
    }

    private Circle initializeCommunicationRange(double centerX, double centerY) {
        Circle communicationRange = new Circle(centerX, centerY, COMMUNICATION_RADIUS);
        communicationRange.setStroke(Color.ORANGE);
        communicationRange.setFill(Color.TRANSPARENT);
        communicationRange.setMouseTransparent(true);
        return communicationRange;
    }

    private Circle initializeSensingRange(double centerX, double centerY) {
        Circle sensingRange = new Circle(centerX ,centerY, SENSING_RADIUS);
        sensingRange.setStroke(Color.BLUE);
        sensingRange.setFill(Color.TRANSPARENT);
        sensingRange.setMouseTransparent(true);
        return sensingRange;
    }

    public double getCenterX() {
        return sensor.getCenterX();
    }

    public double getCenterY() {
        return sensor.getCenterY();
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

    public void addToPane(Pane pane) {
        pane.getChildren().add(this);
    }

    public void removeFromPane(Pane pane) {
        pane.getChildren().remove(this);
    }
}
