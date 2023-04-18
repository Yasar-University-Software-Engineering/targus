package com.targus.ui.widgets;

import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Sensor extends Group {
    private static double COMMUNICATION_RADIUS;
    private static double SENSING_RADIUS;
    private static boolean STATIC_VARIABLES_INITIALIZED = false;
    private static boolean communicationRangeVisible = true;
    private static boolean sensingRangeVisible = true;
    private static final ArrayList<Sensor> allSensors = new ArrayList<>();
    private final Circle sensorDevice;
    private final Circle communicationRange;
    private final Circle sensingRange;

    public Sensor(double centerX, double centerY) {
        if (!staticVariablesInitialized()) {
            throw new IllegalStateException("Radii are not initialized.");
        }
        allSensors.add(this);

        sensorDevice = new Circle(centerX, centerY, 4);
        sensorDevice.setFill(Color.GREEN);

        communicationRange = initializeCommunicationRange(centerX, centerY);
        sensingRange = initializeSensingRange(centerX, centerY);

        getChildren().addAll(sensorDevice, sensingRange, communicationRange);
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
        return communicationRange;
    }

    private Circle initializeSensingRange(double centerX, double centerY) {
        Circle sensingRange = new Circle(centerX, centerY, SENSING_RADIUS);
        sensingRange.setStroke(Paint.valueOf("#6597B8"));
        sensingRange.setFill(Paint.valueOf("#D1EAF2"));
        sensingRange.setBlendMode(BlendMode.SRC_OVER);
        sensingRange.setMouseTransparent(true);
        sensingRange.setVisible(sensingRangeVisible);
        return sensingRange;
    }

    public double getCenterX() {
        return sensorDevice.getCenterX();
    }

    public double getCenterY() {
        return sensorDevice.getCenterY();
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

    public Circle getSensorDevice() {
        return sensorDevice;
    }
}
