package com.targus.ui.controllers;

import com.targus.ui.Mediator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.stage.*;

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

        inputsController.setCommunicationRangeVisibility(communicationRangeVisibility);
        inputsController.setSensingRangeVisibility(sensingRangeVisibility);

//        BorderPane.setMargin(footer, new Insets(10, 10, 10, 10));
//        BorderPane.setMargin(inputs, new Insets(50, 50, 50, 50));
//        BorderPane.setMargin(map, new Insets(50, 50, 50, 50));



        myToolBar.setPadding(new Insets(10, 20, 10, 20));
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
        dialogStage.showAndWait();
    }
    public void handleLoadFromFile() {
        mediator.loadFromFile();
    }

    public void handleExportToFile() {
        mediator.exportToFile();
    }

    public void handleSolve() {
        mediator.solve();
    }

    public void handleCleanSolution() {
        mediator.clean();
    }

    public void handleObjectiveValueDisplay(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        Scene scene = button.getScene();
        Stage stage = (Stage) scene.getWindow();

        if (objectiveValueDisplayController.getStage().isShowing()) {
            // close the pop-up window
            objectiveValueDisplayController.hide();
        } else {
            // open the pop-up window
            objectiveValueDisplayController.show(stage);
        }
    }

    public void handleResetRegion() {
        mediator.resetRegion();
    }

}
