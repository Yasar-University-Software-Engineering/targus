package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProgressBarController {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label gaProgressLabel;

    private Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void setProgressLabelText(String text) {
        gaProgressLabel.setText(text);
    }

    public void bindProgressBar(ReadOnlyDoubleProperty property) {
        progressBar.progressProperty().bind(property);
    }
}
