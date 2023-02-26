package com.targus.ui.widgets;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sensor extends Group {
    private static double COMMUNICATION_RADIUS;
    private static double SENSING_RADIUS;

    public Sensor(double centerX, double centerY) {
        Circle circle = new Circle(centerX, centerY, 4);
        circle.setFill(Color.GREEN);
        Circle communicationRange = new Circle(centerX, centerY, COMMUNICATION_RADIUS);
        communicationRange.setStroke(Color.ORANGE);
        communicationRange.setFill(Color.TRANSPARENT);
        communicationRange.setMouseTransparent(true);
        Circle sensingRange = new Circle(centerX, centerY, SENSING_RADIUS);
        sensingRange.setStroke(Color.BLUE);
        sensingRange.setFill(Color.TRANSPARENT);
        communicationRange.setMouseTransparent(true);
        getChildren().addAll(circle, sensingRange, communicationRange);
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
