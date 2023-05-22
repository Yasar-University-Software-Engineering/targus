package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import com.targus.utils.BestSolutionUpdater;
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
    @FXML
    private FitnessGraphController fitnessGraphController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Mediator mediator = new Mediator();
        ObjectiveValueDisplayController objectiveValueDisplayController = new ObjectiveValueDisplayController();
        BestSolutionUpdater.setObjectiveValueDisplayController(objectiveValueDisplayController);

        mediator.setObjectiveValueDisplayController(objectiveValueDisplayController);
        mediator.setInputsController(inputsController);
        mediator.setSimplifiedObjectiveValueDisplayController(simplifiedObjectiveValueDisplayController);
        mediator.setMapController(mapController);
        mediator.setProgressBarController(progressBarController);
        mediator.setAlgorithmSelectionController(algorithmSelectionController);
        mediator.setFitnessGraphController(fitnessGraphController);

        toolBarController.setMediator(mediator);
        inputsController.setMediator(mediator);
        simplifiedObjectiveValueDisplayController.setMediator(mediator);
        mapController.setMediator(mediator);
        progressBarController.setMediator(mediator);
        toolBarController.setMediator(mediator);
        fitnessGraphController.setMediator(mediator);
        objectiveValueDisplayController.setMediator(mediator);
    }

}
