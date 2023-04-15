package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import com.targus.utils.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public ComboBox algorithmComboBox;
    public VBox GAInputs;
    public StackPane algorithmInputs;
    public ComboBox mutationComboBox;
    public TextField mutationRateTextField;
    public ComboBox terminationComboBox;
    public TextField terminationTextField;
    @FXML
    private ToolBarController toolBarController;
    @FXML
    private InputsController inputsController;
    @FXML
    private SimplifiedObjectiveValueDisplayController simplifiedObjectiveValueDisplayController;
    @FXML
    private MapController mapController;
    @FXML
    private ProgressBarController progressBarController;
    private Mediator mediator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mediator = new Mediator();

        mediator.setMainController(this);
        mediator.setToolBarController(toolBarController);
        mediator.setInputsController(inputsController);
        mediator.setSimplifiedObjectiveValueDisplayController(simplifiedObjectiveValueDisplayController);
        mediator.setMapController(mapController);
        mediator.setProgressBarController(progressBarController);
        mediator.setToolBarController(toolBarController);

        toolBarController.setMediator(mediator);
        inputsController.setMediator(mediator);
        simplifiedObjectiveValueDisplayController.setMediator(mediator);
        mapController.setMediator(mediator);
        progressBarController.setMediator(mediator);
        toolBarController.setMediator(mediator);
    }

    public String getAlgorithm() {
        return algorithmComboBox.getValue().toString();
    }

    public String getMutation() {
        return mutationComboBox.getValue().toString();
    }

    public double getMutationRate() {
        return Double.parseDouble(mutationRateTextField.getText());
    }

    public String getTermination() {
        return terminationComboBox.getValue().toString();
    }

    public int getTerminationValue() {
        return Integer.parseInt(terminationTextField.getText());
    }

    @FXML
    private void handleAlgorithmSelection() {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectionModel().getSelectedItem();
        if (selectedAlgorithm.equals(Constants.STANDARD_GA) || selectedAlgorithm.equals(Constants.IMPROVED_GA)) {
            // Set the visibility of the algorithmInputs StackPane to true
            algorithmInputs.setVisible(true);
            // Create a timeline animation to animate the height of the standardGAInputs VBox
            KeyValue keyValue = new KeyValue(GAInputs.prefHeightProperty(), GAInputs.getPrefHeight());
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.play();
        } else {
            algorithmInputs.setVisible(false);
        }
    }

}
