package com.targus.ui;

import com.targus.problem.wsn.WSNMinimumSensorObjective;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Informative {
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

    public void display(double sensorPenValueScaled,
                        double mConnPenValueScaled,
                        double kCoverPenValueScaled) {
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