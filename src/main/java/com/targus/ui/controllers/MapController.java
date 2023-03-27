package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import com.targus.ui.widgets.CoordinateSystemPane;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

public class MapController {
    @FXML
    public Pane mainPane;
    private Mediator mediator;
    MenuItem createTarget = new MenuItem("Create Target");
    MenuItem createPotentialPosition = new MenuItem("Create Potential Position");
    MenuItem createSensor = new MenuItem("Create Sensor");
    MenuItem removeSensor = new MenuItem("Remove Sensor");

    public void initialize() {
        Rectangle clip = new Rectangle();

        clip.widthProperty().bind(mainPane.widthProperty());
        clip.heightProperty().bind(mainPane.heightProperty());
        mainPane.setClip(clip);

        BorderStroke borderStroke = new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
        Border border = new Border(borderStroke);

        // Set the border on the pane
        mainPane.setBorder(border);
    }

    @FXML
    void paneClicked(javafx.scene.input.MouseEvent event) {
        ContextMenu context = new ContextMenu(createTarget, createPotentialPosition, createSensor, removeSensor);

        if (event.getButton() == MouseButton.SECONDARY) {
            context.show(mainPane, event.getScreenX(), event.getScreenY());
            createTarget.setOnAction(actionEvent -> {
                addChild(new Target(event.getX(), event.getY()));
                addTargetToPane(new Target(event.getX(), event.getY()));
                mediator.addTarget(new Target(event.getX(), event.getY()));
            });

            createPotentialPosition.setOnAction(actionEvent -> {
                addChild(new PotentialPosition(event.getX(), event.getY()));
                addPotentialPositionToPane(new PotentialPosition(event.getX(), event.getY()));
                mediator.addPotentialPosition(new PotentialPosition(event.getX(), event.getY()));
            });

            createSensor.setOnAction(actionEvent -> {
                PotentialPosition potentialPosition = (PotentialPosition) event.getTarget();
                Sensor sensor = new Sensor(potentialPosition.getCenterX(), potentialPosition.getCenterY());
                addChild(sensor);
                addSensorToPane(sensor);
                mediator.addSensor(sensor);
            });

            removeSensor.setOnAction(actionEvent -> {
                Circle circle = (Circle) event.getTarget();

                Sensor sensor = null;

                for (Node node : mainPane.getChildren()) {
                    if (node instanceof Sensor) {
                        if (circle.getCenterX() == ((Sensor) node).getCenterX()
                        && circle.getCenterY() == ((Sensor) node).getCenterY()) {
                            sensor = (Sensor) node;
                        }
                    }
                }
                
                removeChild(sensor);
                mediator.removeSensor(sensor);
            });

        }
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void removeChildren() {
        mainPane.getChildren().removeAll(mainPane.getChildren());
        mediator.clearTargets();
        mediator.clearPotentialPositions();
        mediator.clearSensors();
    }

    public void removeChild(Object child) {
        mainPane.getChildren().remove(child);
    }

    public void resetRegion() {
        removeChildren();
        mainPane.setMaxSize(0,0);
    }

    public void resizePane(double width, double height) {
        mainPane.setPrefSize(width, height);
        mainPane.setStyle("-fx-background-color: #e0e0e0;");
        mainPane.setMaxWidth(1000);
        mainPane.setMaxHeight(1000);
    }

    public void addChild(Object child) {
        if (child instanceof Circle) {
            mainPane.getChildren().add((Circle) child);
    public void addTargetToPane(Target target) {
        mainPane.getChildren().add(target);
    }

    public void addPotentialPositionToPane(PotentialPosition potentialPosition) {
        mainPane.getChildren().add(potentialPosition);
    }

    public void addSensorToPane(Sensor sensor) {
        mainPane.getChildren().add(sensor);
    }

    public void bringTargetsToFront() {
        List<Node> nodesToMoveToFront = new ArrayList<>();
        for (Node node : mainPane.getChildren()) {
            if (node instanceof Target) {
                nodesToMoveToFront.add(node);
            }
        }
        for (Node node : nodesToMoveToFront) {
            mainPane.getChildren().remove(node);
            mainPane.getChildren().add(node);
        }
    }

    public void bringPotentialPositionsToFront() {
        List<Node> nodesToMoveToFront = new ArrayList<>();
        for (Node node : mainPane.getChildren()) {
            if (node instanceof PotentialPosition) {
                nodesToMoveToFront.add(node);
            }
        }
        for (Node node : nodesToMoveToFront) {
            mainPane.getChildren().remove(node);
            mainPane.getChildren().add(node);
        }
    }

    public void bringSensorDevicesToFront() {
        List<Node> nodesToMoveToFront = new ArrayList<>();
        for (Node node : mainPane.getChildren()) {
            if (node instanceof Sensor) {
                nodesToMoveToFront.add(((Sensor) node).getSensorDevice());
            }
        }
        for (Node node : nodesToMoveToFront) {
            mainPane.getChildren().remove(node);
            mainPane.getChildren().add(node);
        }
    }
}


