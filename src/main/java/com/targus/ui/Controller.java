package com.targus.ui;

import com.targus.problem.wsn.WSNSensorOptimizationSolver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private static final int potentialPositionRadius = 15;
    private static final int targetRadius = 10;

    @FXML
    private TextField setAreaHeightTextField;

    @FXML
    private Button resetButton;

    @FXML
    private TextField commRange;
    @FXML
    private TextField sensRange;
    @FXML
    private TextField mValue;
    @FXML
    private TextField kValue;

    @FXML
    private TextField mutationRate;
    @FXML private TextField generationCount;

    @FXML
    private Pane mainPane;

    @FXML
    private Button setAreaButton;

    @FXML
    private TextField setAreaWidthTextField;

    @FXML
    private Button setPotentialPosButton;

    @FXML
    private Button setTargetPosButton;

    @FXML
    private TextField userPPXLocation;

    @FXML
    private TextField userPPYLocation;

    @FXML
    private TextField userTargetXLocation;

    @FXML
    private TextField userTargetYLocation;

    private int paneWidth;
    private int paneHeight;

    private ArrayList<Point2D> potentialPositions = new ArrayList<>();
    private ArrayList<Point2D> targets = new ArrayList<>();

    //create region according to user's size preference
    @FXML
    public void setAreaButtonClicked(ActionEvent event) {

        paneWidth = Integer.parseInt(setAreaWidthTextField.getText());
        paneHeight = Integer.parseInt(setAreaHeightTextField.getText());
        mainPane.setMaxWidth(paneWidth);
        mainPane.setMaxHeight(paneHeight);
        mainPane.setStyle("-fx-background-color: lightGray;");

    }
    //create menu items
    MenuItem item1 = new MenuItem("Create Target");
    MenuItem item2 = new MenuItem("Create Potential Position");

    @FXML
    void paneClicked(javafx.scene.input.MouseEvent event) {

        //add menu items to context menu
        ContextMenu context = new ContextMenu(item1,item2);

        //if right clicked on pane
        if (event.getButton() == MouseButton.SECONDARY){
            //display context menu
            context.show(mainPane, event.getScreenX(),event.getScreenY());

            //if "Create Target" option is selected
            item1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //create target on mouse click location
                    mainPane.getChildren().add(new Target(event.getX(),event.getY()));
                    targets.add(new Point2D(event.getX(),event.getY()));
                }
            });

            //if "Create Potential Position" option is selected
            item2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //create potential position on mouse click location
                    mainPane.getChildren().add(new PotentialPosition(event.getX(),event.getY()));
                    potentialPositions.add(new Point2D(event.getX(),event.getY()));
                }
            });


        }
    }
    //resets region and removes child nodes
    @FXML
    void resetRegionButtonClicked(ActionEvent event) throws Exception {
        //check if there exists a node in mainPane's observable list) -> then remove all and reset the size
        if(mainPane.getChildren().size()>0){
            mainPane.getChildren().removeAll(mainPane.getChildren());
            mainPane.setMaxSize(0,0);
            //targets ve pps' removelanmalı

        }

    }
    //resets child nodes
    @FXML
    void resetComponentsClicked(ActionEvent event) throws Exception {
        //check if there exists a node in mainPane's observable list) -> then remove all and reset the size
        if(mainPane.getChildren().size()>0){
            mainPane.getChildren().removeAll(mainPane.getChildren());
        }

    }
    //user manually sets pp location if texts are not empty and if pane is created
    @FXML
    void setPotentialPosButtonClicked(ActionEvent event) {
        if (mainPane.getMaxHeight() > 0 && mainPane.getMaxWidth() > 0) {

            if (!(userPPXLocation.getText().trim().isEmpty()) && !(userPPYLocation.getText().trim().isEmpty())) {
                int PotentialPosX = Integer.parseInt(userPPXLocation.getText());
                int PotentialPosY = Integer.parseInt(userPPYLocation.getText());
                if (PotentialPosX < paneWidth - potentialPositionRadius && PotentialPosX > potentialPositionRadius && PotentialPosY < paneHeight - potentialPositionRadius && PotentialPosY > 0)
                    mainPane.getChildren().add(new PotentialPosition(PotentialPosX, PotentialPosY));
                    potentialPositions.add(new Point2D(PotentialPosX,PotentialPosY));
            }
        }
    }

    //user manually sets target location if texts are not empty and if pane is created
    @FXML
    void setTargetPosButtonClicked(ActionEvent event) {
        if(mainPane.getMaxHeight() > 0 && mainPane.getMaxWidth() > 0){
            if(!(userTargetXLocation.getText().trim().isEmpty()) && !(userTargetYLocation.getText().trim().isEmpty())) {
                int TargetPosX = Integer.parseInt(userTargetXLocation.getText());
                int TargetPosY = Integer.parseInt(userTargetYLocation.getText());
                if(TargetPosX < paneWidth-targetRadius && TargetPosX > targetRadius && TargetPosY < paneHeight-targetRadius && TargetPosY > 0)
                    mainPane.getChildren().add(new Target(TargetPosX,TargetPosY));
                    targets.add(new Point2D(TargetPosX,TargetPosY));

            }
        }

    }
    //gets 3 values from user to be solved
    @FXML
    void solveButtonClicked(ActionEvent event){
        int valueM = Integer.parseInt(mValue.getText());
        int valueK = Integer.parseInt(kValue.getText());
        int rangeComm = Integer.parseInt(commRange.getText());
        int rangeSens = Integer.parseInt(sensRange.getText());
        int countGeneration = Integer.parseInt(generationCount.getText());
        double rateMutation = Double.parseDouble(mutationRate.getText());

        Point2D[] targetArray = new Point2D[targets.size()];
        for (int i = 0; i < targets.size(); i++) {
            targetArray[i] = targets.get(i);
        }

        Point2D[] potentialPositionArray = new Point2D[potentialPositions.size()];
        for (int i = 0; i < potentialPositions.size(); i++) {
            potentialPositionArray[i] = potentialPositions.get(i);
        }
       WSNSensorOptimizationSolver wsnSensorOptimizationSolver = new WSNSensorOptimizationSolver(
               targetArray,
               potentialPositionArray,
               valueM,
               valueK,
               rangeComm,
               rangeSens,
               countGeneration,
               rateMutation);

        List<Integer> indexes = wsnSensorOptimizationSolver.solve();
       /* indexes.add(0);
        indexes.add(2);*/
        Sensor.setRadii(rangeComm,rangeSens);

        for (Integer index: indexes ) {
            Point2D potentialPosition = potentialPositions.get(index);
            Sensor sensor = new Sensor(potentialPosition.getX(), potentialPosition.getY());
            mainPane.getChildren().add(sensor);
        }

      // draw(indexes);
//m = 3 dersek her sensörümün en az 3 farklı sensör ile aynı sensörde commRange içinde bulunmasını istiyorum
        //k= 2 girersek her target en az 2 sensör tarafından cover edilmesi gerektiği anlamına gelir (sensRange)

    }
}