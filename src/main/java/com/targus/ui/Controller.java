package com.targus.ui;

import com.targus.algorithm.ga.GA;
import com.targus.algorithm.ga.GABuilder;
import com.targus.base.Solution;
import com.targus.problem.BitStringSolution;
import com.targus.problem.wsn.*;
import com.targus.represent.BitString;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
        mainPane.setLayoutX(25);
        mainPane.setLayoutY((25));
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
        Sensor.setRadii(rangeComm,rangeSens);

        for (Integer index: indexes ) {
            Point2D potentialPosition = potentialPositions.get(index);
            Sensor sensor = new Sensor(potentialPosition.getX(), potentialPosition.getY());
            mainPane.getChildren().add(sensor);
        }

    }

    @FXML
    void loadFromFileButtonClicked() {

        final int scale = 4;

        WSNOptimizationProblem wsnOptimizationProblem;

        Point2D[] dimensions;
        Point2D[] targetArray;
        Point2D[] potentialPositionArray;

        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader("input.txt"));

            dimensions = extractCoordinates(read(bufferedReader));
            targetArray = extractCoordinates(read(bufferedReader));
            potentialPositionArray = extractCoordinates(read(bufferedReader));
            int m = Integer.parseInt(read(bufferedReader)[0]);
            int k = Integer.parseInt(read(bufferedReader)[0]);
            int commRange = Integer.parseInt(read(bufferedReader)[0]);
            int sensRange = Integer.parseInt(read(bufferedReader)[0]);
            int generationCount = Integer.parseInt(read(bufferedReader)[0]);
            double mutationRate = Double.parseDouble(read(bufferedReader)[0]);

            mainPane.setMaxWidth(scale * dimensions[0].getX());
            mainPane.setMaxHeight(scale * dimensions[0].getY());
            mainPane.setStyle("-fx-background-color: lightGray;");

            for (Point2D point2D: targetArray) {
                mainPane.getChildren().add(new Target(scale * point2D.getX(), scale * point2D.getY()));
            }

            for (Point2D point2D: potentialPositionArray) {
                mainPane.getChildren().add(new PotentialPosition(scale * point2D.getX(), scale * point2D.getY()));
            }

            WSN wsn = new WSN(targetArray,
                    potentialPositionArray,
                    m,
                    k,
                    scale * commRange,
                    scale * sensRange,
                    generationCount,
                    mutationRate);

             wsnOptimizationProblem = new WSNOptimizationProblem(wsn, new WSNMinimumSensorObjective());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GABuilder gaBuilder = new GABuilder(new GA(wsnOptimizationProblem));
        GA ga = gaBuilder.build();

        Solution bitStringSolution = ga.perform();

        BitString bitString = (BitString) bitStringSolution.getRepresentation();
        List<Integer> indexes = bitString.ones();

        for (Integer index: indexes ) {
            Point2D potentialPosition = potentialPositions.get(index);
            Sensor sensor = new Sensor(potentialPosition.getX(), potentialPosition.getY());
            mainPane.getChildren().add(sensor);
        }
    }

    private Point2D[] extractCoordinates(String[] snippet) {
        Point2D[] coordinates = new Point2D[snippet.length];

        for (int i = 0; i < snippet.length; i++) {
            String text = snippet[i].trim();

            double x = Double.parseDouble(text.split("\\s")[0]);
            double y = Double.parseDouble(text.split("\\s")[1]);

            coordinates[i] = new Point2D(x, y);
        }

        return coordinates;
    }

    private String[] read(BufferedReader bufferedReader) throws IOException {
        return bufferedReader.readLine().split(",");
    }
}