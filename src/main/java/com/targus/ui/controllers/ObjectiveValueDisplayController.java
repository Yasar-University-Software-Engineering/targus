package com.targus.ui.controllers;

import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.represent.BitString;
import com.targus.ui.Main;
import com.targus.ui.Mediator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;

public class ObjectiveValueDisplayController {
    @FXML
    TextField txtSensorObjective;
    @FXML
    TextField txtWeightSensorObjective;
    @FXML
    TextField txtWeightSensorObjectiveResult;
    @FXML
    TextField txtConnectivityObjective;
    @FXML
    TextField txtWeightConnectivityObjective;
    @FXML
    TextField txtWeightConnectivityObjectiveResult;
    @FXML
    TextField txtCoverageObjective;
    @FXML
    TextField txtWeightCoverageObjective;
    @FXML
    TextField txtWeightCoverageObjectiveResult;
    @FXML
    TextField txtTotalResult;

    private Mediator mediator;
    private Parent root;
    private Stage stage;

    public ObjectiveValueDisplayController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/targus/objectiveValueDisplay.fxml"));
        loader.setController(this);

        try {
            root = loader.load();
            stage = new Stage();
            stage.initModality(Modality.NONE);
            stage.initOwner(Main.getPrimaryStage());
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void display() {
        WSNOptimizationProblem wsnOptimizationProblem = (WSNOptimizationProblem) mediator.getOptimizationProblem();
        Solution solution = mediator.getSolution();

        if (wsnOptimizationProblem == null || solution == null) {
            return;
        }

        WSN wsn = (WSN) wsnOptimizationProblem.model();
        WSNMinimumSensorObjective wsnMinimumSensorObjective = (WSNMinimumSensorObjective) wsnOptimizationProblem.getObjectiveFunction();
        BitString bitString = (BitString) solution.getRepresentation();
        HashSet<Integer> indexes = bitString.ones();

        double weightedSensorValue = sensorValue(wsnMinimumSensorObjective, wsn, bitString);
        double weightedMConnValue = mConnectivityValue(wsnMinimumSensorObjective, wsn, indexes);
        double weightedKCovValue = kCoverageValue(wsnMinimumSensorObjective, wsn, indexes);

        double totalResult = weightedSensorValue + weightedMConnValue + weightedKCovValue;
        setTextField(txtTotalResult, totalResult);

        mediator.simplifiedDisplay(weightedSensorValue, weightedMConnValue, weightedKCovValue);
    }

    private double sensorValue(WSNMinimumSensorObjective wsnMinimumSensorObjective, WSN wsn, BitString bitString) {
        double sensorValueScaled = wsnMinimumSensorObjective.getSensorPenValueScaled(wsn, bitString.getBitSet());
        double weightedSensorValue = sensorValueScaled * WSNMinimumSensorObjective.WEIGHT_SENSOR;
        setTextField(txtSensorObjective, sensorValueScaled);
        setTextField(txtWeightSensorObjective, WSNMinimumSensorObjective.WEIGHT_SENSOR);
        setTextField(txtWeightSensorObjectiveResult, weightedSensorValue);
        return  weightedSensorValue;
    }

    private double mConnectivityValue(WSNMinimumSensorObjective wsnMinimumSensorObjective, WSN wsn, HashSet<Integer> indexes) {
        double mConnectivityValueScaled = wsnMinimumSensorObjective.getMConnPenValueScaled(wsn, indexes);
        double weightedMConnectivityValue = mConnectivityValueScaled * WSNMinimumSensorObjective.WEIGHT_M_COMM;
        setTextField(txtConnectivityObjective, mConnectivityValueScaled);
        setTextField(txtWeightConnectivityObjective, WSNMinimumSensorObjective.WEIGHT_M_COMM);
        setTextField(txtWeightConnectivityObjectiveResult, weightedMConnectivityValue);
        return weightedMConnectivityValue;
    }

    private double kCoverageValue(WSNMinimumSensorObjective wsnMinimumSensorObjective, WSN wsn, HashSet<Integer> indexes) {
        double kCoverageValueScaled = wsnMinimumSensorObjective.getKCoverPenValueScaled(wsn, indexes);
        double weightedKCoverageValue = kCoverageValueScaled * WSNMinimumSensorObjective.WEIGHT_K_COV;
        setTextField(txtCoverageObjective, kCoverageValueScaled);
        setTextField(txtWeightCoverageObjective, WSNMinimumSensorObjective.WEIGHT_K_COV);
        setTextField(txtWeightCoverageObjectiveResult, weightedKCoverageValue);
        return weightedKCoverageValue;
    }

    private void setTextField(TextField textField, double value) {
        textField.setText(String.valueOf(value));
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

        stage.show(); // show the window after owner is set
    }

    public void hide() {
        stage.hide();
    }

    public Stage getStage() {
        return stage;
    }
}