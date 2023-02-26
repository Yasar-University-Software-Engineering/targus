package com.targus.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class MapController {
    @FXML
    public Pane mainPane;
    private Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    MenuItem item1 = new MenuItem("Create Target");
    MenuItem item2 = new MenuItem("Create Potential Position");
    
    @FXML
    void paneClicked(javafx.scene.input.MouseEvent event) {
        ContextMenu context = new ContextMenu(item1,item2);

        if (event.getButton() == MouseButton.SECONDARY) {
            context.show(mainPane, event.getScreenX(), event.getScreenY());
            item1.setOnAction(actionEvent -> {
                addChild(new Target(event.getX(), event.getY()));
                mediator.addTarget(new Target(event.getX(), event.getY()));
            });

            item2.setOnAction(actionEvent -> {
                addChild(new PotentialPosition(event.getX(), event.getY()));
                mediator.addPotentialPosition(new PotentialPosition(event.getX(), event.getY()));
            });
        }
    }

    void removeChildren() {
        mainPane.getChildren().removeAll(mainPane.getChildren());
        mediator.clearTargets();
        mediator.clearPotentialPositions();
        mediator.clearSensors();
    }

    void resetRegion() {
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

    public void addChild(Node child) {
        mainPane.getChildren().add(child);
    }
}


