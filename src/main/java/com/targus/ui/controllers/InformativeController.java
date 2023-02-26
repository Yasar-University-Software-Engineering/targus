package com.targus.ui.controllers;

import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.represent.BitString;
import com.targus.ui.Mediator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.HashSet;

public class InformativeController {
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

        double sensorPenValueScaled = wsnMinimumSensorObjective.getSensorPenValueScaled(wsn, bitString.getBitSet());
        double mConnPenValueScaled = wsnMinimumSensorObjective.getMConnPenValueScaled(wsn, indexes);
        double kCoverPenValueScaled = wsnMinimumSensorObjective.getKCoverPenValueScaled(wsn, indexes);

        txtSensorObjective.setText(String.valueOf(sensorPenValueScaled));
        txtConnectivityObjective.setText(String.valueOf(mConnPenValueScaled));
        txtCoverageObjective.setText(String.valueOf(kCoverPenValueScaled));

        txtWeightSensorObjective.setText(String.valueOf(WSNMinimumSensorObjective.WEIGHT_SENSOR));
        txtWeightConnectivityObjective.setText(String.valueOf(WSNMinimumSensorObjective.WEIGHT_M_COMM));
        txtWeightCoverageObjective.setText(String.valueOf(WSNMinimumSensorObjective.WEIGHT_K_COV));

        double weightedSensorValue = sensorPenValueScaled * WSNMinimumSensorObjective.WEIGHT_SENSOR;
        double weightedMConnValue = mConnPenValueScaled * WSNMinimumSensorObjective.WEIGHT_M_COMM;
        double weightedKCovValue = kCoverPenValueScaled * WSNMinimumSensorObjective.WEIGHT_K_COV;

        txtWeightSensorObjectiveResult.setText(String.valueOf(weightedSensorValue));
        txtWeightConnectivityObjectiveResult.setText(String.valueOf(weightedMConnValue));
        txtWeightCoverageObjectiveResult.setText(String.valueOf(weightedKCovValue));

        txtTotalResult.setText(String.valueOf(weightedSensorValue + weightedMConnValue + weightedKCovValue));
    }
}