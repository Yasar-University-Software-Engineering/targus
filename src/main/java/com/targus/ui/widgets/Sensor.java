package com.targus.ui;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sensor extends Group {
    private double communicationRadius;
    private double sensingRadius;

    public Sensor(double centerX, double centerY) {
        Circle circle = new Circle(centerX, centerY, 4);
        circle.setFill(Color.GREEN);
        Circle communicationRange = new Circle(centerX, centerY, communicationRadius);
        communicationRange.setFill(Color.YELLOW); // green with 20% opacity
        Circle sensingRange = new Circle(centerX, centerY, sensingRadius);
        sensingRange.setFill(Color.rgb(255, 0, 0, 0.2)); // red with 20% opacity
        getChildren().addAll(circle, sensingRange, communicationRange);
    }

    public double getCommunicationRadius() {
        return communicationRadius;
    }

    public void setCommunicationRadius(double communicationRadius) {
        this.communicationRadius = communicationRadius;
    }

    public double getSensingRadius() {
        return sensingRadius;
    }

    public void setSensingRadius(double sensingRadius) {
        this.sensingRadius = sensingRadius;
    }

    public void addToPane(Pane pane) {
        pane.getChildren().add(this);
    }

    public void removeFromPane(Pane pane) {
        pane.getChildren().remove(this);
    }
}
