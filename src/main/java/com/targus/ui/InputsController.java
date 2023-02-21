package com.targus.ui;

import com.targus.base.OptimizationProblem;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InputsController implements Initializable {

    private Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @FXML
    private TextField txtAreaWidth;
    @FXML
    private TextField txtAreaHeight;
    @FXML
    private TextField txtM;
    @FXML
    private TextField txtK;
    @FXML
    private TextField txtCommunicationRange;
    @FXML
    private TextField txtSensingRange;
    @FXML
    private TextField txtMutationRate;
    @FXML
    private TextField txtGenerationCount;

    private int paneWidth;
    private int paneHeight;
    private final ArrayList<Point2D> potentialPositions = new ArrayList<>();
    private final ArrayList<Point2D> targets = new ArrayList<>();
    private final IntegerProperty mProperty = new SimpleIntegerProperty(1);
    private final IntegerProperty kProperty = new SimpleIntegerProperty(1);
    private final DoubleProperty communicationRangeProperty = new SimpleDoubleProperty(100);
    private final DoubleProperty sensingRangeProperty = new SimpleDoubleProperty(50);
    private final IntegerProperty generationCountProperty = new SimpleIntegerProperty(1000);
    private final DoubleProperty mutationRateProperty = new SimpleDoubleProperty(0.3);

    @FXML
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private OptimizationProblem optimizationProblem;

    @FXML private Button resizeButton;


    public void initialize() {
        resizeButton.setOnAction(event -> {
            mediator.resizeMapPane(400, 400);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtM.textProperty().bindBidirectional(mProperty, new NumberStringConverter());
        txtK.textProperty().bindBidirectional(kProperty, new NumberStringConverter());
        txtCommunicationRange.textProperty().bindBidirectional(communicationRangeProperty, new NumberStringConverter());
        txtSensingRange.textProperty().bindBidirectional(sensingRangeProperty, new NumberStringConverter());
        txtGenerationCount.textProperty().bindBidirectional(generationCountProperty, new NumberStringConverter());
        txtMutationRate.textProperty().bindBidirectional(mutationRateProperty, new NumberStringConverter());

        choiceBox.getItems().add("Standard GA");
        choiceBox.getItems().add("Improved GA");
        choiceBox.getItems().add("Greedy Algorithm");

        choiceBox.setValue("Standard GA");
    }

    public void handleLoadFromFile() {
    }

    public void handleExportToFile() {
    }

    @FXML
    void handleGenerateGrid() {
//        for (int i = 5; i < paneHeight; i += 25) {
//            for (int j = 10; j < paneWidth; j += 25) {
//                potentialPositions.add(new Point2D(j, i));
//            }
//        }
//
//        initProblemInstance();
    }

    @FXML
    void handleCleanSolution() {
//        for (Sensor sensor : sensors) {
//            mainPane.getChildren().remove(sensor);
//        }
//        sensors.clear();
//
//        for (Circle radius : radii) {
//            mainPane.getChildren().remove(radius);
//        }
//        radii.clear();
    }


    @FXML
    void handleSolve()  {
//        disableTextField(true);
//
//        cleanSolution();
//        initProblemInstance();
//
//        WSN wsn = (WSN) optimizationProblem.model();
//
//        GA singleObjectiveOA;
//
//        if (choiceBox.getValue().equals("Standard GA")) {
//            GABuilder gaBuilder = new GABuilder(new StandardGA(optimizationProblem));
//            singleObjectiveOA = gaBuilder.build();
//        } else if (choiceBox.getValue().equals("Improved GA")) {
//            GABuilder gaBuilder = new GABuilder(new ImprovedGA(optimizationProblem));
//            singleObjectiveOA = gaBuilder.build();
//        } else if (choiceBox.getValue().equals("Greedy Algorithm")) {
//            singleObjectiveOA = null;
//        } else {
//            try {
//                throw new Exception("No such algorithm available");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        ProgressTask progressTask = new ProgressTask(singleObjectiveOA.getTerminalState());
//        progressTask.valueProperty().addListener((observable, oldValue, newValue) -> gaProgressLabel.setText(String.valueOf(newValue)));
//        progressBar.progressProperty().bind(progressTask.progressProperty());
//
//        Thread thread = new Thread(progressTask);
//        thread.setDaemon(true);
//        thread.start();
//
//        Task<Solution> gaTask = new Task<>() {
//            @Override
//            protected Solution call() {
//                return singleObjectiveOA.perform();
//            }
//        };
//
//        gaTask.setOnSucceeded(e -> {
//            gaProgressLabel.setText("GA is completed!");
//            Solution bitStringSolution = gaTask.getValue();
//            BitString bitString = (BitString) bitStringSolution.getRepresentation();
//            HashSet<Integer> indexes = bitString.ones();
//
//            WSNMinimumSensorObjective wsnMinimumSensorObjective = new WSNMinimumSensorObjective();
//
//            double sensorPenValueScaled = wsnMinimumSensorObjective.getSensorPenValueScaled(wsn, bitString.getBitSet());
//            double mConnPenValueScaled = wsnMinimumSensorObjective.getMConnPenValueScaled(wsn, indexes);
//            double kCoverPenValueScaled = wsnMinimumSensorObjective.getKCoverPenValueScaled(wsn, indexes);
//
//            //informativeController.display(sensorPenValueScaled, mConnPenValueScaled, kCoverPenValueScaled);
//
//            Point2D[] potentialPositionArray = wsn.getPotentialPositions();
//
//            for (Integer index: indexes ) {
//                Point2D potentialPosition = potentialPositionArray[index];
//                Sensor sensor = new Sensor(potentialPosition.getX(), potentialPosition.getY());
//                Circle sensingRadius = new Circle(sensor.getLayoutX(), sensor.getLayoutY(), sensor.getSensingRangeRadius());
//                sensingRadius.setFill(Color.TRANSPARENT);
//                sensingRadius.setStroke(Color.BLUE);
//                Circle commRadius = new Circle(sensor.getLayoutX(), sensor.getLayoutY(), sensor.getCommunicationRangeRadius());
//                commRadius.setFill(Color.TRANSPARENT);
//                commRadius.setStroke(Color.ORANGE);
//                mainPane.getChildren().add(commRadius);
//                mainPane.getChildren().add(sensingRadius);
//                mainPane.getChildren().add(sensor);
//
//                radii.add(commRadius);
//                radii.add(sensingRadius);
//                sensors.add(sensor);
//            }
//            disableTextField(false);
//        });
//        new Thread(gaTask).start();
//        // Below line is duplicated on purpose. It will be removed in the refactoring phase
//        gaProgressLabel.setText("GA is completed!");

    }

    private void disableTextField(boolean bool) {
        txtM.setDisable(bool);
        txtK.setDisable(bool);
        txtCommunicationRange.setDisable(bool);
        txtSensingRange.setDisable(bool);
        txtGenerationCount.setDisable(bool);
        txtMutationRate.setDisable(bool);
    }

    private void initProblemInstance() {
//        Sensor.setRadii(communicationRangeProperty.get(), sensingRangeProperty.get());
//        mainPane.setMaxWidth(paneWidth);
//        mainPane.setMaxHeight(paneHeight);
//        mainPane.setStyle("-fx-background-color: lightGray;");
//
//        mainPane.setLayoutX(25);
//        mainPane.setLayoutY(25);
//
//        for (Point2D point2D: targets) {
//            mainPane.getChildren().add(new Target(point2D.getX(), point2D.getY()));
//        }
//
//        for (Point2D point2D: potentialPositions) {
//            mainPane.getChildren().add(new PotentialPosition(point2D.getX(), point2D.getY()));
//        }
//
//        Point2D[] targetArray = new Point2D[targets.size()];
//        Point2D[] potentialPositionArray = new Point2D[potentialPositions.size()];
//
//        for (int i = 0; i < targetArray.length; i++) {
//            targetArray[i] = targets.get(i);
//        }
//
//        for (int i = 0; i < potentialPositionArray.length; i++) {
//            potentialPositionArray[i] = potentialPositions.get(i);
//        }
//
//        WSN wsn = new WSN(targetArray,
//                potentialPositionArray,
//                mProperty.get(),
//                kProperty.get(),
//                communicationRangeProperty.get(),
//                sensingRangeProperty.get(),
//                generationCountProperty.get(),
//                mutationRateProperty.get());
//
//        optimizationProblem = new WSNOptimizationProblem(wsn, new WSNMinimumSensorObjective());
    }

    @FXML
    void resetRegionButtonClicked() {
//        mainPane.getChildren().removeAll(mainPane.getChildren());
//        mainPane.setMaxSize(0,0);
//        potentialPositions.clear();
//        targets.clear();
    }

    @FXML
    void resetComponentsClicked() {
//        mainPane.getChildren().removeAll(mainPane.getChildren());
    }

    @FXML
    public void setAreaButtonClicked() {

//        paneWidth = Integer.parseInt(txtAreaWidth.getText());
//        paneHeight = Integer.parseInt(txtAreaHeight.getText());
//
//        mapController.setAreaButtonClicked(paneWidth, paneHeight);
    }

}
