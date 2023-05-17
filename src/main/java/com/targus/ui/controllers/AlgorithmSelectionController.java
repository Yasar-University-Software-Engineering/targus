package com.targus.ui.controllers;

import com.targus.utils.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Objects;

public class AlgorithmSelectionController {
    public ComboBox algorithmComboBox;
    public VBox GAInputs;
    public StackPane algorithmInputs;
    public ComboBox mutationComboBox;
    public TextField mutationRateTextField;
    public ComboBox terminationComboBox;
    public TextField terminationTextField;
    public Label mutationOperatorLabel;
    public Label mutationRateLabel;

    public void initialize() {
        algorithmInputs.setVisible(false);
    }

    public String getAlgorithm() {
        return algorithmComboBox.getValue().toString();
    }

    public String getMutation() {
        if (Objects.equals(algorithmComboBox.getValue().toString(), Constants.STANDARD_GA)
                || Objects.equals(algorithmComboBox.getValue().toString(), Constants.IMPROVED_GA)) {
            return mutationComboBox.getValue().toString();
        }
        return "";
    }

    public double getMutationRate() {
        if (Objects.equals(algorithmComboBox.getValue().toString(), Constants.STANDARD_GA)
                || Objects.equals(algorithmComboBox.getValue().toString(), Constants.IMPROVED_GA)) {
            if (Double.parseDouble(mutationRateTextField.getText()) > 0) {
                return Double.parseDouble(mutationRateTextField.getText());
            }
        }
        return 0.0;
    }

    public String getTermination() {
//        if (Objects.equals(algorithmComboBox.getValue().toString(), Constants.STANDARD_GA)
//                || Objects.equals(algorithmComboBox.getValue().toString(), Constants.IMPROVED_GA)) {
//            return terminationComboBox.getValue().toString();
//        }
//        return "";
        return terminationComboBox.getValue().toString();
    }

    public int getTerminationValue() {
//        if (Objects.equals(algorithmComboBox.getValue().toString(), Constants.STANDARD_GA)
//                || Objects.equals(algorithmComboBox.getValue().toString(), Constants.IMPROVED_GA)) {
//            if (Integer.parseInt(terminationTextField.getText()) > 0) {
//                return Integer.parseInt(terminationTextField.getText());
//            }
//        }
//        return 0;
        if (Integer.parseInt(terminationTextField.getText()) > 0) {
            return Integer.parseInt(terminationTextField.getText());
        }
        return 0;
    }

    @FXML
    private void handleAlgorithmSelection() {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectionModel().getSelectedItem();
        if (selectedAlgorithm.equals(Constants.STANDARD_GA) || selectedAlgorithm.equals(Constants.IMPROVED_GA)) {
            algorithmComboBox.setVisible(true);
            // Set the visibility of the algorithmInputs StackPane to true
            algorithmInputs.setVisible(true);
            mutationComboBox.setVisible(true);
            mutationOperatorLabel.setVisible(true);
            mutationRateTextField.setVisible(true);
            mutationRateLabel.setVisible(true);
            // Create a timeline animation to animate the height of the standardGAInputs VBox
            KeyValue keyValue = new KeyValue(GAInputs.prefHeightProperty(), GAInputs.getPrefHeight());
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.play();
        } else {
            algorithmInputs.setVisible(true);
            mutationComboBox.setVisible(false);
            mutationOperatorLabel.setVisible(false);
            mutationRateTextField.setVisible(false);
            mutationRateLabel.setVisible(false);
        }
    }
}
