package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import javafx.fxml.FXML;

public class MainController {
    @FXML
    private InputsController inputsController;
    @FXML
    private InformativeController informativeController;
    @FXML
    private MapController mapController;
    @FXML
    private EmptyController emptyController;

    private Mediator mediator;

    public void initialize() {
        mediator = new Mediator();

        mediator.setInputsController(inputsController);
        mediator.setInformativeController(informativeController);
        mediator.setMapController(mapController);
        mediator.setEmptyController(emptyController);

        inputsController.setMediator(mediator);
        informativeController.setMediator(mediator);
        mapController.setMediator(mediator);
        emptyController.setMediator(mediator);
    }
}
