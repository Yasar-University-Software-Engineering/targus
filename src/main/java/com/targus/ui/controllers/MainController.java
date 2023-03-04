package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import javafx.fxml.FXML;

public class MainController {
    @FXML
    private InputsController inputsController;
    @FXML
    private ObjectiveValueDisplayController objectiveValueDisplayController;
    @FXML
    private MapController mapController;
    @FXML
    private ProgressBarController progressBarController;

    private Mediator mediator;

    public void initialize() {
        mediator = new Mediator();

        mediator.setInputsController(inputsController);
        mediator.setInformativeController(objectiveValueDisplayController);
        mediator.setMapController(mapController);
        mediator.setEmptyController(progressBarController);

        inputsController.setMediator(mediator);
        objectiveValueDisplayController.setMediator(mediator);
        mapController.setMediator(mediator);
        progressBarController.setMediator(mediator);
    }
}
