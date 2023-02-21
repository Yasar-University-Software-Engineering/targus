package com.targus.ui;

import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.problem.wsn.WSNOptimizationProblem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class MapController {
    @FXML
    public Pane mainPane;
    @FXML
    public Label lbl;

    MenuItem item1 = new MenuItem("Create Target");
    MenuItem item2 = new MenuItem("Create Potential Position");

    private final ArrayList<Sensor> sensors = new ArrayList<>();
    private final ArrayList<Circle> radii = new ArrayList<>();

    public void initialize() {

    }
    
    @FXML
    void paneClicked(javafx.scene.input.MouseEvent event) {

//        //add menu items to context menu
//        ContextMenu context = new ContextMenu(item1,item2);
//
//        //if right clicked on pane
//        if (event.getButton() == MouseButton.SECONDARY){
//            //display context menu
//            context.show(mainPane, event.getScreenX(),event.getScreenY());
//
//            //if "Create Target" option is selected
//            item1.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//                    //create target on mouse click location
//                    mainPane.getChildren().add(new Target(event.getX(),event.getY()));
//                    targets.add(new Point2D(event.getX(),event.getY()));
//                }
//            });
//
//            //if "Create Potential Position" option is selected
//            item2.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//                    //create potential position on mouse click location
//                    mainPane.getChildren().add(new PotentialPosition(event.getX(),event.getY()));
//
//                    potentialPositions.add(new Point2D(event.getX(),event.getY()));
//                }
//            });
//
//
//        }
    }

//    @FXML
//    void resetRegionButtonClicked() {
////        mainPane.getChildren().removeAll(mainPane.getChildren());
////        mainPane.setMaxSize(0,0);
////        potentialPositions.clear();
////        targets.clear();
//    }
//
//    @FXML
//    void resetComponentsClicked() {
//        mainPane.getChildren().removeAll(mainPane.getChildren());
//    }
//
//    @FXML
//    public void setAreaButtonClicked() {
////        paneWidth = Integer.parseInt(txtAreaWidth.getText());
////        paneHeight = Integer.parseInt(txtAreaHeight.getText());
////        mainPane.setMaxWidth(paneWidth);
////        mainPane.setMaxHeight(paneHeight);
////        mainPane.setStyle("-fx-background-color: lightGray;");
////        mainPane.setLayoutX(25);
////        mainPane.setLayoutY(25);
//    }

    public void setAreaButtonClicked(int paneWidth, int paneHeight) {
        mainPane.setMaxWidth(500);
        mainPane.setMaxHeight(100);
        mainPane.setStyle("-fx-background-color: lightGray;");
        mainPane.setLayoutX(25);
        mainPane.setLayoutY(25);
    }

    public void setMediator(Mediator mediator) {
    }

    public void resizePane(double width, double height) {
        mainPane.setPrefSize(width, height);
    }
}
