package com.targus.ui.controllers;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.ui.Mediator;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateProblemInstanceController {

    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField connectivityField;

    @FXML
    private TextField coverageField;

    @FXML
    private TextField commRadiusField;

    @FXML
    private TextField sensRadiusField;

    @FXML
    private TextField genCountField;

    @FXML
    private TextField mutRateField;

    @FXML
    private Button okButton;

    private Stage dialogStage;

    private Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void submit() {
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        int connectivity = Integer.parseInt(connectivityField.getText());
        int coverage = Integer.parseInt(coverageField.getText());
        double commRadius = Double.parseDouble(commRadiusField.getText());
        double sensRadius = Double.parseDouble(sensRadiusField.getText());
        int genCount = Integer.parseInt(genCountField.getText());
        double mutRate = Double.parseDouble(mutRateField.getText());

        WSNOptimizationProblem wsnOptimizationProblem = new WSNOptimizationProblem(
                new WSN(new Point2D[0],
                        new Point2D[0],
                        connectivity,
                        coverage,
                        commRadius,
                        sensRadius,
                        genCount,
                        mutRate),
        new WSNMinimumSensorObjective()
        );

        createProblemInstance(wsnOptimizationProblem, width, height);
        dialogStage.close();
    }

    public void createProblemInstance(WSNOptimizationProblem wsnOptimizationProblem, int width, int height) {
        mediator.createProblemInstance(wsnOptimizationProblem, width, height);
    }
}
