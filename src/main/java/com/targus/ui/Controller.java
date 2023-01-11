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
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Controller implements Initializable {
    private final WSNPrototype WSNPrototype = new WSNPrototype();
    @FXML
    private TextField txtSensorObjective;
    @FXML
    private TextField txtWeightSensorObjective;
    @FXML
    private TextField txtWeightSensorObjectiveResult;
    @FXML
    private TextField txtConnectivityObjective;
    @FXML
    private TextField txtWeightConnectivityObjective;
    @FXML
    private TextField txtWeightConnectivityObjectiveResult;
    @FXML
    private TextField txtCoverageObjective;
    @FXML
    private TextField txtWeightCoverageObjective;
    @FXML
    private TextField txtWeightCoverageObjectiveResult;
    @FXML
    private TextField txtTotalResult;

    @FXML
    private TextField setAreaHeightTextField;

    @FXML
    private TextField txtCommunicationRange;
    @FXML
    private TextField txtSensingRange;
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
    private TextField setAreaWidthTextField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label gaProgressLabel;

    MenuItem item1 = new MenuItem("Create Target");
    MenuItem item2 = new MenuItem("Create Potential Position");

    private OptimizationProblem optimizationProblem;

    private final ArrayList<Sensor> sensors = new ArrayList<>();
    private final ArrayList<Circle> radii = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtM.textProperty().bindBidirectional(WSNPrototype.getMProperty(), new NumberStringConverter());
        txtK.textProperty().bindBidirectional(WSNPrototype.getKProperty(), new NumberStringConverter());
        txtCommunicationRange.textProperty().bindBidirectional(WSNPrototype.getCommunicationRangeProperty(), new NumberStringConverter());
        txtSensingRange.textProperty().bindBidirectional(WSNPrototype.getSensingRangeProperty(), new NumberStringConverter());
        txtGenerationCount.textProperty().bindBidirectional(WSNPrototype.getGenerationCountProperty(), new NumberStringConverter());
        txtMutationRate.textProperty().bindBidirectional(WSNPrototype.getMutationRateProperty(), new NumberStringConverter());
    }

    private void changeDisable(boolean bool) {
        txtM.setDisable(bool);
        txtK.setDisable(bool);
        txtCommunicationRange.setDisable(bool);
        txtSensingRange.setDisable(bool);
        txtGenerationCount.setDisable(bool);
        txtMutationRate.setDisable(bool);
    }

    @FXML
    public void setAreaButtonClicked() {
        WSNPrototype.setPaneWidth(Integer.parseInt(setAreaWidthTextField.getText()));
        WSNPrototype.setPaneHeight(Integer.parseInt(setAreaHeightTextField.getText()));
        mainPane.setMaxWidth(WSNPrototype.getPaneWidth());
        mainPane.setMaxHeight(WSNPrototype.getPaneHeight());
        mainPane.setStyle("-fx-background-color: lightGray;");
        mainPane.setLayoutX(25);
        mainPane.setLayoutY(25);
    }

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
                    WSNPrototype.getTargets().add(new Point2D(event.getX(),event.getY()));
                }
            });

            //if "Create Potential Position" option is selected
            item2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //create potential position on mouse click location
                    mainPane.getChildren().add(new PotentialPosition(event.getX(),event.getY()));
                    WSNPrototype.getPotentialPositions().add(new Point2D(event.getX(),event.getY()));
                }
            });


        }
    }

    @FXML
    void resetRegionButtonClicked() {
        mainPane.getChildren().removeAll(mainPane.getChildren());
        mainPane.setMaxSize(0,0);
        WSNPrototype.getPotentialPositions().clear();
        WSNPrototype.getTargets().clear();
    }

    @FXML
    void resetComponentsClicked() {
        mainPane.getChildren().removeAll(mainPane.getChildren());
    }

    @FXML
    void generateGrid() {
        for (int i = 5; i < WSNPrototype.getPaneHeight(); i += 25) {
            for (int j = 10; j < WSNPrototype.getPaneWidth(); j += 25) {
                WSNPrototype.getPotentialPositions().add(new Point2D(j, i));
            }
        }

        initProblemInstance();
    }

    @FXML
    void solveButtonClicked()  {
        changeDisable(true);

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

        Task<Solution> gaTask = new Task<>() {
            @Override
            protected Solution call() {
                return ga.perform();
            }
        };

        gaTask.setOnSucceeded(e -> {
            gaProgressLabel.setText("GA is completed!");
            Solution bitStringSolution = gaTask.getValue();
            BitString bitString = (BitString) bitStringSolution.getRepresentation();
            HashSet<Integer> indexes = bitString.ones();

            WSNMinimumSensorObjective wsnMinimumSensorObjective = new WSNMinimumSensorObjective();
            double sensorPenValueScaled = wsn.getSolutionSize() != 0 ?
                    1 - ((double) bitString.getBitSet().cardinality() / wsn.getSolutionSize()) : 0;

            double mConnPenValueScaled = indexes.size() == 0 || wsn.getM() == 0 ?
                    1 : (double) wsnMinimumSensorObjective.mConnPenSum(wsn, indexes) / (indexes.size() * wsn.getM());

            double kCoverPenValueScaled = wsn.targetsSize() * wsn.getK() != 0 ?
                    (double) wsnMinimumSensorObjective.kCovPenSum(wsn, indexes) / (wsn.targetsSize() * wsn.getK()) : 1;

            txtSensorObjective.setText(String.valueOf(sensorPenValueScaled));
            txtConnectivityObjective.setText(String.valueOf(mConnPenValueScaled));
            txtCoverageObjective.setText(String.valueOf(kCoverPenValueScaled));

            txtWeightSensorObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightSensor));
            txtWeightConnectivityObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightMComm));
            txtWeightCoverageObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightKCov));

            txtWeightSensorObjectiveResult.setText(String.valueOf(sensorPenValueScaled * WSNMinimumSensorObjective.weightSensor));
            txtWeightConnectivityObjectiveResult.setText(String.valueOf(mConnPenValueScaled * WSNMinimumSensorObjective.weightMComm));
            txtWeightCoverageObjectiveResult.setText(String.valueOf(kCoverPenValueScaled * WSNMinimumSensorObjective.weightKCov));

/*            txtTotalResult.setText(String.valueOf(sensorPenValueScaled * WSNMinimumSensorObjective.weightSensor +
                    mConnPenValueScaled * WSNMinimumSensorObjective.weightMComm +
                    kCoverPenValueScaled * WSNMinimumSensorObjective.weightKCov));*/

            txtTotalResult.setText(String.valueOf(wsnMinimumSensorObjective.value(wsn, bitString)));

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
            changeDisable(false);
        });
        new Thread(gaTask).start();
        // Below line is duplicated on purpose. It will be removed in the refactoring phase
        gaProgressLabel.setText("GA is completed!");

    }

    @FXML
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

    void initProblemInstance() {
        Sensor.setRadii(WSNPrototype.getCommunicationRangeProperty().get(), WSNPrototype.getSensingRangeProperty().get());
        mainPane.setMaxWidth(WSNPrototype.getPaneWidth());
        mainPane.setMaxHeight(WSNPrototype.getPaneHeight());
        mainPane.setStyle("-fx-background-color: lightGray;");

        mainPane.setLayoutX(25);
        mainPane.setLayoutY(25);

        for (Point2D point2D: WSNPrototype.getTargets()) {
            mainPane.getChildren().add(new Target(point2D.getX(), point2D.getY()));
        }

        for (Point2D point2D: WSNPrototype.getPotentialPositions()) {
            mainPane.getChildren().add(new PotentialPosition(point2D.getX(), point2D.getY()));
        }

        Point2D[] targetArray = new Point2D[WSNPrototype.getTargets().size()];
        Point2D[] potentialPositionArray = new Point2D[WSNPrototype.getPotentialPositions().size()];

        for (int i = 0; i < targetArray.length; i++) {
            targetArray[i] = WSNPrototype.getTargets().get(i);
        }

        for (int i = 0; i < potentialPositionArray.length; i++) {
            potentialPositionArray[i] = WSNPrototype.getPotentialPositions().get(i);
        }

        WSN wsn = new WSN(targetArray,
                potentialPositionArray,
                WSNPrototype.getMProperty().get(),
                WSNPrototype.getKProperty().get(),
                WSNPrototype.getCommunicationRangeProperty().get(),
                WSNPrototype.getSensingRangeProperty().get(),
                WSNPrototype.getGenerationCountProperty().get(),
                WSNPrototype.getMutationRateProperty().get());

        optimizationProblem = new WSNOptimizationProblem(wsn, new WSNMinimumSensorObjective());
    }

    @FXML
    void exportToFileButtonClicked() {
        WSNPrototype.exportToFile();
    }

    @FXML
    void loadFromFileButtonClicked() {
        resetRegionButtonClicked();
        WSNPrototype.loadFromFile();
        initProblemInstance();
    }
}
