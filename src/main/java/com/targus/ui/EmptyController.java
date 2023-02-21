package com.targus.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class EmptyController {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label gaProgressLabel;
    @FXML private Label emptyLabel;

    private Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void initialize() {

    }
}
