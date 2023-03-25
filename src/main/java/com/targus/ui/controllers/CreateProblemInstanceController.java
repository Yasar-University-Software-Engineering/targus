package com.targus.ui.controllers;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.ui.Mediator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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

//    @FXML
//    private TextField genCountField;
//
//    @FXML
//    private TextField mutRateField;

    @FXML
    private CheckBox gridCheckBox;
    @FXML
    private Label distanceLabel;

    @FXML
    private TextField distanceField;

    @FXML
    private CheckBox randomPlacementCheckbox;
    @FXML
    private Label numNodesLabel;
    @FXML
    private TextField numNodesField;

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
        mediator.resetRegion();
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        int connectivity = Integer.parseInt(connectivityField.getText());
        int coverage = Integer.parseInt(coverageField.getText());
        double commRadius = Double.parseDouble(commRadiusField.getText());
        double sensRadius = Double.parseDouble(sensRadiusField.getText());
//        int genCount = Integer.parseInt(genCountField.getText());
//        double mutRate = Double.parseDouble(mutRateField.getText());
        int genCount = 1;
        double mutRate = 0.3;
        int distance = 0;
        int numberNodes = 0;

        if (gridCheckBox.isSelected()) {
            distance = Integer.parseInt(distanceField.getText());
        }

        if (randomPlacementCheckbox.isSelected()) {
            numberNodes = Integer.parseInt(numNodesField.getText());
        }

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

        createProblemInstance(wsnOptimizationProblem, width, height, distance, numberNodes);
        dialogStage.close();
    }

    public void createProblemInstance(WSNOptimizationProblem wsnOptimizationProblem, int width, int height, int distance , int numberNodes) {
        mediator.createProblemInstance(wsnOptimizationProblem, width, height, distance, numberNodes);
    }

    @FXML
    private void toggleGridFields() {
        boolean isChecked = gridCheckBox.isSelected();
        distanceLabel.setDisable(!isChecked);
        distanceField.setDisable(!isChecked);
    }

    @FXML
    private void toggleRandomPlacement() {
        boolean isChecked = randomPlacementCheckbox.isSelected();
        numNodesLabel.setDisable(!isChecked);
        numNodesField.setDisable(!isChecked);
    }

}
