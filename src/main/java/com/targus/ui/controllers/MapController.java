package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class MapController {
    @FXML
    public Pane mainPane;
    private Mediator mediator;
    MenuItem createTarget = new MenuItem("Create Target");
    MenuItem createPotentialPosition = new MenuItem("Create Potential Position");
    MenuItem createSensor = new MenuItem("Create Sensor");
    MenuItem removeSensor = new MenuItem("Remove Sensor");

    @FXML
    void paneClicked(javafx.scene.input.MouseEvent event) {
        ContextMenu context = new ContextMenu(createTarget, createPotentialPosition, createSensor, removeSensor);

        if (event.getButton() == MouseButton.SECONDARY) {
            context.show(mainPane, event.getScreenX(), event.getScreenY());
            createTarget.setOnAction(actionEvent -> {
                addChild(new Target(event.getX(), event.getY()));
                mediator.addTarget(new Target(event.getX(), event.getY()));
            });

            createPotentialPosition.setOnAction(actionEvent -> {
                addChild(new PotentialPosition(event.getX(), event.getY()));
                mediator.addPotentialPosition(new PotentialPosition(event.getX(), event.getY()));
            });

            createSensor.setOnAction(actionEvent -> {
                Sensor sensor = new Sensor(
                        ((PotentialPosition) event.getTarget()).getLayoutX(),
                        ((PotentialPosition) event.getTarget()).getLayoutY());
                addChild(sensor);
                mediator.addSensor(sensor);
            });

            removeSensor.setOnAction(actionEvent -> {
                Sensor sensor = (Sensor) event.getTarget();
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
        mainPane.setMaxWidth(500);
        mainPane.setMaxHeight(500);
        mainPane.setStyle("-fx-background-color: lightGray;");
        mainPane.setLayoutX(25);
        mainPane.setLayoutY(25);
    }

    public void addChild(Object child) {
        if (child instanceof Circle) {
            mainPane.getChildren().add((Circle) child);
        }
        if (child instanceof Group) {
            mainPane.getChildren().add((Group) child);
        }
    }
}


