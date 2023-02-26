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

        txtWeightSensorObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightSensor));
        txtWeightConnectivityObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightMComm));
        txtWeightCoverageObjective.setText(String.valueOf(WSNMinimumSensorObjective.weightKCov));

        double weightedSensorValue = sensorPenValueScaled * WSNMinimumSensorObjective.weightSensor;
        double weightedMConnValue = mConnPenValueScaled * WSNMinimumSensorObjective.weightMComm;
        double weightedKCovValue = kCoverPenValueScaled * WSNMinimumSensorObjective.weightKCov;

        txtWeightSensorObjectiveResult.setText(String.valueOf(weightedSensorValue));
        txtWeightConnectivityObjectiveResult.setText(String.valueOf(weightedMConnValue));
        txtWeightCoverageObjectiveResult.setText(String.valueOf(weightedKCovValue));

        txtTotalResult.setText(String.valueOf(weightedSensorValue + weightedMConnValue + weightedKCovValue));
    }
}