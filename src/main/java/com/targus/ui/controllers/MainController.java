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
    public ComboBox algorithmComboBox;
    public VBox GAInputs;
    public StackPane algorithmInputs;
    public ComboBox mutationComboBox;
    public TextField mutationRateTextField;
    public ComboBox terminationComboBox;
    public TextField terminationTextField;
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
