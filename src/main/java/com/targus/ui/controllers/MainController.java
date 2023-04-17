package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ToolBarController toolBarController;
    @FXML
    private InputsController inputsController;
    @FXML
    private SimplifiedObjectiveValueDisplayController simplifiedObjectiveValueDisplayController;
    @FXML
    private MapController mapController;
    @FXML
    private AlgorithmSelectionController algorithmSelectionController;
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
        mediator.setAlgorithmSelectionController(algorithmSelectionController);

        toolBarController.setMediator(mediator);
        inputsController.setMediator(mediator);
        simplifiedObjectiveValueDisplayController.setMediator(mediator);
        mapController.setMediator(mediator);
        progressBarController.setMediator(mediator);
        toolBarController.setMediator(mediator);
    }

}
