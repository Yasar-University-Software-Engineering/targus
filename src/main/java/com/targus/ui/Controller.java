package com.targus.ui;

import com.targus.algorithm.ga.GA;
import com.targus.algorithm.ga.GABuilder;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.*;
import com.targus.represent.BitString;
import com.targus.utils.ProgressTask;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {

    private static final int potentialPositionRadius = 15;
    private static final int targetRadius = 10;
    public Label sensorObjective;
    public Label connectivityObjective;
    public Label coverageObjective;
    public Label weightSensorObjective;
    public Label weightConnectivityObjective;
    public Label weightCoverageObjective;
    public Label weightSensorObjectiveResult;
    public Label weightConnectivityObjectiveResult;
    public Label weightCoverageObjectiveResult;
    public Label total;

    @FXML
    private TextField setAreaHeightTextField;

    @FXML
    private Button resetButton;

    @FXML
    private TextField txtCommRange;
    @FXML
    private TextField txtSensRange;
    @FXML
    private TextField txtM;
    @FXML
    private TextField txtK;

    @FXML
    private TextField txtMutationRate;
    @FXML private TextField txtGenerationCount;

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

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label gaProgressLabel;

    private int paneWidth;
    private int paneHeight;

    private ArrayList<Point2D> potentialPositions = new ArrayList<>();
    private ArrayList<Point2D> targets = new ArrayList<>();

    private int m;
    private int k;

    private double commRange;
    private double sensRange;

    private int generationCount;
    private double mutationRate;
    private OptimizationProblem optimizationProblem;


    //create region according to user's size preference
    @FXML
    public void setAreaButtonClicked(ActionEvent event) {

        paneWidth = Integer.parseInt(setAreaWidthTextField.getText());
        paneHeight = Integer.parseInt(setAreaHeightTextField.getText());
        mainPane.setMaxWidth(paneWidth);
        mainPane.setMaxHeight(paneHeight);
        mainPane.setStyle("-fx-background-color: lightGray;");
        mainPane.setLayoutX(25);
        mainPane.setLayoutY(25);
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
    void resetRegionButtonClicked() throws Exception {
        //check if there exists a node in mainPane's observable list) -> then remove all and reset the size
        if(mainPane.getChildren().size()>0){
            mainPane.getChildren().removeAll(mainPane.getChildren());
            mainPane.setMaxSize(0,0);
            potentialPositions.clear();
            targets.clear();

        }

    }
    //resets child nodes
    @FXML
    void resetComponentsClicked() throws Exception {
        //check if there exists a node in mainPane's observable list) -> then remove all and reset the size
        if(mainPane.getChildren().size()>0){
            mainPane.getChildren().removeAll(mainPane.getChildren());
        }

    }
    //user manually sets pp location if texts are not empty and if pane is created
    @FXML
    void setPotentialPosButtonClicked() {
        if (mainPane.getMaxHeight() > 0 && mainPane.getMaxWidth() > 0) {

            if (!(userPPXLocation.getText().trim().isEmpty()) && !(userPPYLocation.getText().trim().isEmpty())) {
                int PotentialPosX = Integer.parseInt(userPPXLocation.getText());
                int PotentialPosY = Integer.parseInt(userPPYLocation.getText());
                if (PotentialPosX < paneWidth - potentialPositionRadius && PotentialPosX > potentialPositionRadius && PotentialPosY < paneHeight - potentialPositionRadius && PotentialPosY > 0)
                {
                    mainPane.getChildren().add(new PotentialPosition(PotentialPosX, PotentialPosY));
                    potentialPositions.add(new Point2D(PotentialPosX,PotentialPosY));
                }
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
                {
                    mainPane.getChildren().add(new Target(TargetPosX,TargetPosY));
                    targets.add(new Point2D(TargetPosX,TargetPosY));
                }
            }
        }

    }

    @FXML
    void setParametersButtonClicked() {
        m = Integer.parseInt(mValue.getText());
        k = Integer.parseInt(kValue.getText());
        commRange = Double.parseDouble(txtCommRange.getText());
        sensRange = Double.parseDouble(txtSensRange.getText());
        generationCount = Integer.parseInt(txtGenerationCount.getText());
        mutationRate = Double.parseDouble(txtMutationRate.getText());
    }

    @FXML
    void solveButtonClicked() throws Exception {

        initProblemInstance();

        WSN wsn = (WSN) optimizationProblem.model();

        GABuilder gaBuilder = new GABuilder(new GA(optimizationProblem));
        GA ga = gaBuilder.build();

        ProgressTask progressTask = new ProgressTask(ga.getTerminalState());
        progressTask.valueProperty().addListener((observable, oldValue, newValue) -> gaProgressLabel.setText(String.valueOf(newValue)));
        progressBar.progressProperty().bind(progressTask.progressProperty());

        Thread thread = new Thread(progressTask);
        thread.setDaemon(true);
        thread.start();

        Task<Solution> gaTask = new Task<Solution>() {
            @Override
            protected Solution call() throws Exception {
                return ga.perform();
            }
        };

        gaTask.setOnSucceeded(e -> {
            gaProgressLabel.setText("GA is completed!");
            Solution bitStringSolution = gaTask.getValue();
            BitString bitString = (BitString) bitStringSolution.getRepresentation();
            List<Integer> indexes = bitString.ones();

            WSNMinimumSensorObjective wsnMinimumSensorObjective = new WSNMinimumSensorObjective();
            double sensorPenValueScaled = wsn.getPopulationSize() != 0 ?
                    1 - ((double) bitString.getBitSet().cardinality() / wsn.getPopulationSize()) : 0;

            double mConnPenValueScaled = indexes.size() * wsn.getM() != 0 ?
                    (double) wsnMinimumSensorObjective.mConnPenSum(wsn, indexes) / (indexes.size() * wsn.getM()) : 0;

            double kCoverPenValueScaled = wsn.targetsSize() * wsn.getK() != 0 ?
                    (double) wsnMinimumSensorObjective.kCovPenSum(wsn, indexes) / (wsn.targetsSize() * wsn.getK()) : 0;

            sensorObjective.setText(String.valueOf(sensorPenValueScaled));
            connectivityObjective.setText(String.valueOf(mConnPenValueScaled));
            coverageObjective.setText(String.valueOf(kCoverPenValueScaled));

            weightSensorObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightSensor));
            weightConnectivityObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightMComm));
            weightCoverageObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightKCov));

            weightSensorObjectiveResult.setText(String.valueOf(sensorPenValueScaled * WSNMinimumSensorObjective.weightSensor));
            weightConnectivityObjectiveResult.setText(String.valueOf(mConnPenValueScaled * WSNMinimumSensorObjective.weightMComm));
            weightCoverageObjectiveResult.setText(String.valueOf(kCoverPenValueScaled * WSNMinimumSensorObjective.weightKCov));

            total.setText(String.valueOf(sensorPenValueScaled * WSNMinimumSensorObjective.weightSensor +
                    mConnPenValueScaled * WSNMinimumSensorObjective.weightMComm +
                    kCoverPenValueScaled * WSNMinimumSensorObjective.weightKCov));

            Point2D[] potentialPositionArray = wsn.getPotentialPositions();

            for (Integer index: indexes ) {
                Point2D potentialPosition = potentialPositionArray[index];
                Sensor sensor = new Sensor(potentialPosition.getX(), potentialPosition.getY());
                Circle sensingRadius = new Circle(sensor.getLayoutX(), sensor.getLayoutY(), sensor.getSensingRangeRadius());
                sensingRadius.setFill(Color.TRANSPARENT);
                sensingRadius.setStroke(Color.BLUE);
                Circle commRadius = new Circle(sensor.getLayoutX(), sensor.getLayoutY(), sensor.getCommunicationRangeRadius());
                commRadius.setFill(Color.TRANSPARENT);
                commRadius.setStroke(Color.ORANGE);
                mainPane.getChildren().add(commRadius);
                mainPane.getChildren().add(sensingRadius);
                mainPane.getChildren().add(sensor);
            }
        });
        new Thread(gaTask).start();
        // Below line is duplicated on purpose. It will be removed in the refactoring phase
        gaProgressLabel.setText("GA is completed!");

    }

    void initProblemInstance() throws Exception {

        resetRegionButtonClicked();

        final int scale = 1;

        Sensor.setRadii(scale * commRange, scale * sensRange);
        mainPane.setMaxWidth(scale * paneWidth);
        mainPane.setMaxHeight(scale * paneHeight);
        mainPane.setStyle("-fx-background-color: lightGray;");

        mainPane.setLayoutX(25);
        mainPane.setLayoutY(25);

        for (int i = 0; i < potentialPositions.size(); i++) {
            potentialPositions.set(i, potentialPositions.get(i).multiply(scale));
        }

        for (int i = 0; i < targets.size(); i++) {
            targets.set(i, targets.get(i).multiply(scale));
        }

        for (Point2D point2D: targets) {
            mainPane.getChildren().add(new Target(point2D.getX(), point2D.getY()));
        }

        for (Point2D point2D: potentialPositions) {
            mainPane.getChildren().add(new PotentialPosition(point2D.getX(), point2D.getY()));
        }

        Point2D[] targetArray = new Point2D[targets.size()];
        Point2D[] potentialPositionArray = new Point2D[potentialPositions.size()];

        for (int i = 0; i < targetArray.length; i++) {
            targetArray[i] = targets.get(i);
        }

        for (int i = 0; i < potentialPositionArray.length; i++) {
            potentialPositionArray[i] = potentialPositions.get(i);
        }

        WSN wsn = new WSN(targetArray,
                potentialPositionArray,
                m,
                k,
                scale * commRange,
                scale * sensRange,
                generationCount,
                mutationRate);

        optimizationProblem = new WSNOptimizationProblem(wsn, new WSNMinimumSensorObjective());
    }

    @FXML
    void exportToFileButtonClicked() throws Exception {
        try {
            new File("export.txt");
            FileWriter myWriter = new FileWriter("export.txt");


            myWriter.write("\ndimensions:\n");
            myWriter.write(paneWidth + " " + paneHeight);
            myWriter.write("\n");

            myWriter.write("\ntargets:\n");
            for (int i = 0; i < targets.size() - 1; i++) {
                myWriter.write(targets.get(i).getX() + " " + targets.get(i).getX() + ", ");
            }
            myWriter.write(targets.get(targets.size() - 1).getX() + " " + targets.get(targets.size() - 1).getX());
            myWriter.write("\n");

            myWriter.write("\npotential positions:\n");
            for (int i = 0; i < potentialPositions.size() - 1; i++) {
                myWriter.write(potentialPositions.get(i).getX() + " " + potentialPositions.get(i).getX() + ", ");
            }
            myWriter.write(potentialPositions.get(potentialPositions.size() - 1).getX() + " " + potentialPositions.get(potentialPositions.size() - 1).getX());
            myWriter.write("\n");

            myWriter.write("\nm:\n");
            myWriter.write(Integer.toString(m));
            myWriter.write("\n");

            myWriter.write("\nk:\n");
            myWriter.write(Integer.toString(k));
            myWriter.write("\n");

            myWriter.write("\ngeneration count:\n");
            myWriter.write(Integer.toString(generationCount));
            myWriter.write("\n");

            myWriter.write("\nmutation rate:\n");
            myWriter.write(Double.toString(mutationRate));
            myWriter.write("\n");


            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadFromFileButtonClicked() throws Exception {
        resetRegionButtonClicked();

        BufferedReader bufferedReader;

        Point2D[] dimensions;
        Point2D[] targetArray;
        Point2D[] potentialPositionArray;

        try {
            bufferedReader = new BufferedReader(new FileReader("input.txt"));

            moveLine(bufferedReader, 2);
            dimensions = extractCoordinates(read(bufferedReader));

            moveLine(bufferedReader, 2);
            targetArray = extractCoordinates(read(bufferedReader));

            moveLine(bufferedReader, 2);
            potentialPositionArray = extractCoordinates(read(bufferedReader));

            moveLine(bufferedReader, 2);
            m = Integer.parseInt(read(bufferedReader)[0]);

            moveLine(bufferedReader, 2);
            k = Integer.parseInt(read(bufferedReader)[0]);

            moveLine(bufferedReader, 2);
            commRange = Double.parseDouble(read(bufferedReader)[0]);

            moveLine(bufferedReader, 2);
            sensRange = Double.parseDouble(read(bufferedReader)[0]);

            moveLine(bufferedReader, 2);
            generationCount = Integer.parseInt(read(bufferedReader)[0]);

            moveLine(bufferedReader, 2);
            mutationRate = Double.parseDouble(read(bufferedReader)[0]);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        paneWidth = (int) dimensions[0].getX();
        paneHeight = (int) dimensions[0].getY();

        Collections.addAll(targets, targetArray);
        Collections.addAll(potentialPositions, potentialPositionArray);

        initProblemInstance();
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

    private void moveLine(BufferedReader bufferedReader, int n) throws IOException {
        for (int i = 0; i < n; i++) {
            bufferedReader.readLine();
        }
    }
}