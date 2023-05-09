package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapController {
    @FXML
    public Pane mainPane;
    MenuItem createTarget = new MenuItem("Create Target");
    MenuItem createPotentialPosition = new MenuItem("Create Potential Position");
    MenuItem createSensor = new MenuItem("Create Sensor");
    MenuItem removeSensor = new MenuItem("Remove Sensor");
    private Mediator mediator;

    public void initialize() {
        Rectangle clip = new Rectangle();

        clip.widthProperty().bind(mainPane.widthProperty());
        clip.heightProperty().bind(mainPane.heightProperty());
        mainPane.setClip(clip);

        BorderStroke borderStroke = new BorderStroke(
                Color.web("#d3d3d3"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
        Border border = new Border(borderStroke);

        // Set the border on the pane
        mainPane.setBorder(border);
    }

    @FXML
    void paneClicked(javafx.scene.input.MouseEvent event) {
//        ContextMenu context = new ContextMenu(createTarget, createPotentialPosition, createSensor, removeSensor);
//
//        if (event.getButton() == MouseButton.SECONDARY) {
//            context.show(mainPane, event.getScreenX(), event.getScreenY());
//            createTarget.setOnAction(actionEvent -> {
//                addTargetToPane(new Target(event.getX(), event.getY()));
//                mediator.addTarget(new Target(event.getX(), event.getY()));
//            });
//
//            createPotentialPosition.setOnAction(actionEvent -> {
//                addPotentialPositionToPane(new PotentialPosition(event.getX(), event.getY()));
//                mediator.addPotentialPosition(new PotentialPosition(event.getX(), event.getY()));
//            });
//
//            createSensor.setOnAction(actionEvent -> {
//                PotentialPosition potentialPosition = (PotentialPosition) event.getTarget();
//                Sensor sensor = new Sensor(potentialPosition.getCenterX(), potentialPosition.getCenterY());
//                addSensorToPane(sensor);
//                mediator.addSensor(sensor);
//            });
//
//            removeSensor.setOnAction(actionEvent -> {
//                Circle circle = (Circle) event.getTarget();
//
//                Sensor sensor = null;
//
//                for (Node node : mainPane.getChildren()) {
//                    if (node instanceof Sensor) {
//                        if (circle.getCenterX() == ((Sensor) node).getCenterX()
//                                && circle.getCenterY() == ((Sensor) node).getCenterY()) {
//                            sensor = (Sensor) node;
//                        }
//                    }
//                }
//
//                removeChild(sensor);
//                mediator.removeSensor(sensor);
//            });
//
//        }
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void removeChildren() {
//        mainPane.getChildren().removeAll(mainPane.getChildren());
//        mediator.clearTargets();
//        mediator.clearPotentialPositions();
//        mediator.clearSensors();
    }

    public void removeChild(Object child) {
        mainPane.getChildren().remove(child);
    }

    public void removeSensors() {
        for (Sensor sensor : Sensor.getAllSensors()) {
            sensor.removeRangesFromPane(mainPane);
            mainPane.getChildren().remove(sensor);
        }
    }

    public void resetRegion() {
        removeChildren();
        mainPane.setMaxSize(0, 0);
        mediator.displayNonApplicable();
    }

    public void resizePane(double width, double height) {
        mainPane.setPrefSize(width, height);
        mainPane.setStyle("-fx-background-color: #e0e0e0;");
        mainPane.setMaxWidth(1000);
        mainPane.setMaxHeight(1000);
    }

    public void addTargetToPane(Target target) {
        mainPane.getChildren().add(target);
    }

    public void addPotentialPositionToPane(PotentialPosition potentialPosition) {
        mainPane.getChildren().add(potentialPosition);
    }

    public void addOrRemoveSensorFromPane(Sensor sensor) {
        Sensor sensorInPane = Sensor.retrieveSensorByIndex(sensor.getRespectivePotentialPositionIndex());
        if (mainPane.getChildren().contains(sensorInPane)) {
            sensorInPane.removeRangesFromPane(mainPane);
            mainPane.getChildren().remove(sensorInPane);
            Sensor.removeSensorFromAllSensorsList(sensor);
            Sensor.removeSensorFromSensorHashSet(sensor);
        } else {
            mainPane.getChildren().add(sensorInPane);
        }
    }
}


