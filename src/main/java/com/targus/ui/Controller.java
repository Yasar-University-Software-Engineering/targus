package com.targus.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targus.algorithm.ga.GA;
import com.targus.algorithm.ga.GABuilder;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.*;
import com.targus.represent.BitString;
import com.targus.utils.Constants;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

    private ArrayList<Sensor> sensors = new ArrayList<>();
    private ArrayList<Circle> radii = new ArrayList<>();


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
        mainPane.getChildren().removeAll(mainPane.getChildren());
        mainPane.setMaxSize(0,0);
        potentialPositions.clear();
        targets.clear();
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
    void generateGrid() throws Exception {
        for (int i = 5; i < paneHeight; i += 25) {
            for (int j = 10; j < paneWidth; j += 25) {
                potentialPositions.add(new Point2D(j, i));
            }
        }

        initProblemInstance();
    }

    @FXML
    void setParametersButtonClicked() {
        if (!txtM.getText().isEmpty()) {
            m = Integer.parseInt(txtM.getText());
        }

        if (!txtK.getText().isEmpty()) {
            k = Integer.parseInt(txtK.getText());
        }

        if (!txtCommRange.getText().isEmpty()) {
            commRange = Double.parseDouble(txtCommRange.getText());
        }

        if (!txtSensRange.getText().isEmpty()) {
            sensRange = Double.parseDouble(txtSensRange.getText());
        }

        if (!txtGenerationCount.getText().isEmpty()) {
            generationCount = Integer.parseInt(txtGenerationCount.getText());
        }

        if (!txtMutationRate.getText().isEmpty()) {
            mutationRate = Double.parseDouble(txtMutationRate.getText());
        }
    }

    @FXML
    void solveButtonClicked() throws Exception {

        cleanSolution();
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
            HashSet<Integer> indexes = bitString.ones();

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

                radii.add(commRadius);
                radii.add(sensingRadius);
                sensors.add(sensor);
            }
        });
        new Thread(gaTask).start();
        // Below line is duplicated on purpose. It will be removed in the refactoring phase
        gaProgressLabel.setText("GA is completed!");

    }

    void cleanSolution() {
        for (Sensor sensor : sensors) {
            mainPane.getChildren().remove(sensor);
        }
        sensors.clear();

        for (Circle radius : radii) {
            mainPane.getChildren().remove(radius);
        }
        radii.clear();
    }

    void initProblemInstance() throws Exception {

        final int scale = 1;

        Sensor.setRadii(scale * commRange, scale * sensRange);
        mainPane.setMaxWidth(scale * paneWidth);
        mainPane.setMaxHeight(scale * paneHeight);
        mainPane.setStyle("-fx-background-color: lightGray;");

        mainPane.setLayoutX(25);
        mainPane.setLayoutY(25);

        potentialPositions.replaceAll(point2D -> point2D.multiply(scale));

        targets.replaceAll(point2D -> point2D.multiply(scale));

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
    void exportToFileButtonClicked() {
        try {
            // write with json prettifier
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.json"));
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> problemInfo = new HashMap<>();
            problemInfo.put(Constants.DIMENSIONS, Arrays.asList(paneWidth, paneWidth));

            List<double[]> targetList = new ArrayList<>();
            for (Point2D target : targets) {
                double[] coords = new double[2];
                coords[0] = target.getX();
                coords[1] = target.getY();
                targetList.add(coords);
            }
            problemInfo.put(Constants.TARGETS, targetList);

            List<double[]> potentialPositionList = new ArrayList<>();
            for (Point2D potentialPosition : potentialPositions) {
                double[] coords = new double[2];
                coords[0] = potentialPosition.getX();
                coords[1] = potentialPosition.getY();
                potentialPositionList.add(coords);
            }
            problemInfo.put(Constants.POTENTIAL_POSITIONS, potentialPositionList);

            problemInfo.put(Constants.COMMUNICATION_RADIUS, commRange);
            problemInfo.put(Constants.SENSING_RADIUS, sensRange);
            problemInfo.put(Constants.M, m);
            problemInfo.put(Constants.K, k);
            problemInfo.put(Constants.GENERATION_COUNT, generationCount);
            problemInfo.put(Constants.MUTATION_RATE, mutationRate);

            writer.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(problemInfo));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadFromFileButtonClicked() throws Exception {
        resetRegionButtonClicked();

        Point2D dimensions;
        Point2D[] targetArray;
        Point2D[] potentialPositionArray;

        try {
            Reader reader = Files.newBufferedReader(Paths.get("output.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parser = objectMapper.readTree(reader);

            JsonNode dimensionsNode = parser.path(Constants.DIMENSIONS);
            dimensions = new Point2D(dimensionsNode.get(0).asDouble(), dimensionsNode.get(1).asDouble());

            JsonNode targetsNode = parser.path(Constants.TARGETS);
            targetArray = new Point2D[targetsNode.size()];
            for (int i = 0; i < targetsNode.size(); i++) {
                JsonNode node = targetsNode.get(i);
                Point2D target = new Point2D(node.get(0).asDouble(), node.get(1).asDouble());
                targetArray[i] = target;
            }

            JsonNode potentialPositionsNode = parser.path(Constants.POTENTIAL_POSITIONS);
            potentialPositionArray = new Point2D[potentialPositionsNode.size()];
            for (int i = 0; i < potentialPositionsNode.size(); i++) {
                JsonNode node = potentialPositionsNode.get(i);
                Point2D potentialPos = new Point2D(node.get(0).asDouble(), node.get(1).asDouble());
                potentialPositionArray[i] = potentialPos;
            }

            m = parser.path(Constants.M).asInt();
            k = parser.path(Constants.K).asInt();
            generationCount = parser.path(Constants.GENERATION_COUNT).asInt();
            mutationRate = Double.parseDouble(parser.path(Constants.MUTATION_RATE).asText());
            sensRange = Double.parseDouble(parser.path(Constants.SENSING_RADIUS).asText());
            commRange = Double.parseDouble(parser.path(Constants.COMMUNICATION_RADIUS).asText());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        paneWidth = (int) dimensions.getX();
        paneHeight = (int) dimensions.getY();

        Collections.addAll(targets, targetArray);
        Collections.addAll(potentialPositions, potentialPositionArray);

        initProblemInstance();
    }
}
