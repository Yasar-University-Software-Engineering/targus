package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import com.targus.ui.widgets.Sensor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ToolBarController implements Initializable {
    @FXML
    private CheckBox sensingRangeVisibility;
    @FXML
    private CheckBox communicationRangeVisibility;
    private CreateProblemInstanceController createProblemInstanceController;
    private Mediator mediator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        communicationRangeVisibility.setOnAction(event -> Sensor.setCommunicationRangeVisibility(communicationRangeVisibility.isSelected()));
        sensingRangeVisibility.setOnAction(event -> Sensor.setSensingRangeVisibility(sensingRangeVisibility.isSelected()));

        createProblemInstanceController = new CreateProblemInstanceController();
        Platform.runLater(() -> createProblemInstanceController.setMediator(mediator));
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
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
        mediator.cleanSolution();
    }

    public void handleObjectiveValueDisplay(ActionEvent event) {
        Platform.runLater(() -> {
            Button button = (Button) event.getSource();
            Scene scene = button.getScene();
            Stage stage = (Stage) scene.getWindow();

            ObjectiveValueDisplayController objectiveValueDisplayController = mediator.getObjectiveValueDisplayController();

            if (objectiveValueDisplayController.getStage().isShowing()) {
                objectiveValueDisplayController.hide();
            } else {
                objectiveValueDisplayController.show(stage);
            }
                }

        );
    }

    public void handleResetRegion() {
        mediator.resetRegion();
    }
}
