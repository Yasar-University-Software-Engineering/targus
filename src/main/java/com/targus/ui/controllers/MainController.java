package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import com.targus.ui.widgets.Sensor;
import com.targus.utils.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public CheckBox sensingRangeVisibility;
    public CheckBox communicationRangeVisibility;
    public HBox simplifiedObjectiveValueDisplay;
    public VBox inputs;
    public AnchorPane map;
    public ToolBar myToolBar;
    public HBox footer;
    public BorderPane borderPane;
    @FXML
    private InputsController inputsController;
    @FXML
    private ObjectiveValueDisplayController objectiveValueDisplayController;
    @FXML
    private SimplifiedObjectiveValueDisplayController simplifiedObjectiveValueDisplayController;
    @FXML
    private MapController mapController;
    @FXML
    private ProgressBarController progressBarController;
    private CreateProblemInstanceController createProblemInstanceController;
    private Mediator mediator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mediator = new Mediator();
        createProblemInstanceController = new CreateProblemInstanceController();
        objectiveValueDisplayController = new ObjectiveValueDisplayController();

        mediator.setMainController(this);
        mediator.setInputsController(inputsController);
        mediator.setObjectiveValueDisplayController(objectiveValueDisplayController);
        mediator.setSimplifiedObjectiveValueDisplayController(simplifiedObjectiveValueDisplayController);
        mediator.setMapController(mapController);
        mediator.setProgressBarController(progressBarController);
        mediator.setCreateProblemInstanceController(createProblemInstanceController);

        inputsController.setMediator(mediator);
        objectiveValueDisplayController.setMediator(mediator);
        simplifiedObjectiveValueDisplayController.setMediator(mediator);
        mapController.setMediator(mediator);
        progressBarController.setMediator(mediator);
        createProblemInstanceController.setMediator(mediator);

        communicationRangeVisibility.setOnAction(event -> Sensor.setCommunicationRangeVisibility(communicationRangeVisibility.isSelected()));

        sensingRangeVisibility.setOnAction(event -> Sensor.setSensingRangeVisibility(sensingRangeVisibility.isSelected()));
    }



        myToolBar.setPadding(new Insets(10, 20, 10, 20));
    }

    public CheckBox getCommunicationRangeVisibility() {
        return communicationRangeVisibility;
    }

    public CheckBox getSensingRangeVisibility() {
        return sensingRangeVisibility;
    }

    public void handleCreateProblemInstance(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Scene scene = button.getScene();
        Window window = scene.getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/targus/createProblemInstance.fxml"));
        loader.setController(createProblemInstanceController);
        Parent root = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(window);
        dialogStage.setScene(new Scene(root));
        createProblemInstanceController.setDialogStage(dialogStage);

        dialogStage.setTitle("Create a problem instance");
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
    }

    public void handleLoadFromFile(ActionEvent event) {
        mediator.loadFromFile(event);
    }

    public void handleExportToFile(ActionEvent event) {
        mediator.exportToFile(event);
    }

    public void handleSolve() {
        mediator.solve();
    }

    public void handleCleanSolution() {
        mediator.clean();
    }

    public void handleObjectiveValueDisplay(ActionEvent event) {
        Button button = (Button) event.getSource();
        Scene scene = button.getScene();
        Stage stage = (Stage) scene.getWindow();

        if (objectiveValueDisplayController.getStage().isShowing()) {
            objectiveValueDisplayController.hide();
        } else {
            objectiveValueDisplayController.show(stage);
        }
    }

    public void handleResetRegion() {
        mediator.resetRegion();
    }
}
