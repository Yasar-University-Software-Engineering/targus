package com.targus.ui.controllers;

import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.represent.BitString;
import com.targus.ui.Main;
import com.targus.ui.Mediator;
import com.targus.utils.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

public class ObjectiveValueDisplayController {
    @FXML
    Label lblSensorObjective;
    @FXML
    Label lblWeightSensorObjective;
    @FXML
    Label lblWeightSensorObjectiveResult;
    @FXML
    Label lblConnectivityObjective;
    @FXML
    Label lblWeightConnectivityObjective;
    @FXML
    Label lblWeightConnectivityObjectiveResult;
    @FXML
    Label lblCoverageObjective;
    @FXML
    Label lblWeightCoverageObjective;
    @FXML
    Label lblWeightCoverageObjectiveResult;
    @FXML
    Label lblTotalResult;

    private Mediator mediator;
    private Parent root;
    private Stage stage;

    private long currentState;
    private Solution solution;

    public ObjectiveValueDisplayController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/targus/objectiveValueDisplay.fxml"));
        loader.setController(this);
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResource("/icons/logo.png")).toExternalForm());

        try {
            root = loader.load();
            stage = new Stage();
            stage.initModality(Modality.NONE);
            stage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/fitness-window.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("targus");
            stage.getIcons().add(logoImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void display() {
        WSNOptimizationProblem wsnOptimizationProblem = (WSNOptimizationProblem) mediator.getOptimizationProblem();

        WSN wsn = (WSN) wsnOptimizationProblem.model();
        WSNMinimumSensorObjective wsnMinimumSensorObjective = (WSNMinimumSensorObjective) wsnOptimizationProblem.getObjectiveFunction();
        BitString bitString = (BitString) solution.getRepresentation();
        HashSet<Integer> indexes = bitString.ones();

        double weightedSensorValue = sensorValue(wsnMinimumSensorObjective, wsn, bitString);
        double weightedMConnValue = mConnectivityValue(wsnMinimumSensorObjective, wsn, indexes);
        double weightedKCovValue = kCoverageValue(wsnMinimumSensorObjective, wsn, indexes);

        double totalResult = weightedSensorValue + weightedMConnValue + weightedKCovValue;

        Platform.runLater(() -> setText(lblTotalResult, totalResult));

        mediator.displaySolution(solution);
        mediator.simplifiedDisplay(weightedSensorValue, weightedMConnValue, weightedKCovValue);
        if (currentState != -1) {
            mediator.updateGraph(currentState, totalResult);
        }
    }

    private double sensorValue(WSNMinimumSensorObjective wsnMinimumSensorObjective, WSN wsn, BitString bitString) {
        double sensorValueScaled = wsnMinimumSensorObjective.getSensorPenValueScaled(wsn, bitString.getBitSet());
        double weightedSensorValue = sensorValueScaled * WSNMinimumSensorObjective.WEIGHT_SENSOR;
        Platform.runLater(() -> {
            setText(lblSensorObjective, sensorValueScaled);
            setText(lblWeightSensorObjective, WSNMinimumSensorObjective.WEIGHT_SENSOR);
            setText(lblWeightSensorObjectiveResult, weightedSensorValue);
        });
        return weightedSensorValue;
    }

    private double mConnectivityValue(WSNMinimumSensorObjective wsnMinimumSensorObjective, WSN wsn, HashSet<Integer> indexes) {
        double mConnectivityValueScaled = wsnMinimumSensorObjective.getMConnPenValueScaled(wsn, indexes);
        double weightedMConnectivityValue = mConnectivityValueScaled * WSNMinimumSensorObjective.WEIGHT_M_COMM;
        Platform.runLater(() -> {
            setText(lblConnectivityObjective, mConnectivityValueScaled);
            setText(lblWeightConnectivityObjective, WSNMinimumSensorObjective.WEIGHT_M_COMM);
            setText(lblWeightConnectivityObjectiveResult, weightedMConnectivityValue);
        });
        return weightedMConnectivityValue;
    }

    private double kCoverageValue(WSNMinimumSensorObjective wsnMinimumSensorObjective, WSN wsn, HashSet<Integer> indexes) {
        double kCoverageValueScaled = wsnMinimumSensorObjective.getKCoverPenValueScaled(wsn, indexes);
        double weightedKCoverageValue = kCoverageValueScaled * WSNMinimumSensorObjective.WEIGHT_K_COV;
        Platform.runLater(() -> {
            setText(lblCoverageObjective, kCoverageValueScaled);
            setText(lblWeightCoverageObjective, WSNMinimumSensorObjective.WEIGHT_K_COV);
            setText(lblWeightCoverageObjectiveResult, weightedKCoverageValue);
        });
        return weightedKCoverageValue;
    }

    private void setText(Label label, String value) {
        label.setText(value);
    }

    private void setText(Label label, Double value) {
        label.setText(String.format("%.3f", value));
    }

    public void show(Stage owner) {
        if (stage == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/targus/createProblemInstance.fxml"));
            loader.setController(this);

            try {
                root = loader.load();
                stage = new Stage();

                stage.initModality(Modality.NONE);
                stage.initOwner(owner);
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public Stage getStage() {
        return stage;
    }

    public void displayNonApplicable() {
        setText(lblSensorObjective, Constants.NON_APPLICABLE);
        setText(lblWeightSensorObjective, Constants.NON_APPLICABLE);
        setText(lblWeightSensorObjectiveResult, Constants.NON_APPLICABLE);

        setText(lblConnectivityObjective, Constants.NON_APPLICABLE);
        setText(lblWeightConnectivityObjective, Constants.NON_APPLICABLE);
        setText(lblWeightConnectivityObjectiveResult, Constants.NON_APPLICABLE);

        setText(lblCoverageObjective, Constants.NON_APPLICABLE);
        setText(lblWeightCoverageObjective, Constants.NON_APPLICABLE);
        setText(lblWeightCoverageObjectiveResult, Constants.NON_APPLICABLE);

        setText(lblTotalResult, Constants.NON_APPLICABLE);

        mediator.simplifiedDisplayNonApplicable();
    }

    public void updateCurrentSolution(long currentState, Solution solution) {
        this.currentState = currentState;
        this.solution = solution;
        display();
    }
}