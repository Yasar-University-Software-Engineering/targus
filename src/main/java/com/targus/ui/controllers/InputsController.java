package com.targus.ui.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.problem.wsn.WSNPrototype;
import com.targus.ui.Mediator;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import com.targus.utils.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class InputsController implements Initializable {
    private WSNPrototype wsnPrototype = new WSNPrototype();
    private WSNOptimizationProblem wsnOptimizationProblem;
    private final SolutionController solutionController = new SolutionController();

    @FXML
    private TextField txtM;
    @FXML
    private TextField txtK;
    @FXML
    private TextField txtCommunicationRange;
    @FXML
    private TextField txtSensingRange;

    private Mediator mediator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtM.textProperty().bindBidirectional(wsnPrototype.getMProperty(), new NumberStringConverter());
        txtK.textProperty().bindBidirectional(wsnPrototype.getKProperty(), new NumberStringConverter());
        txtCommunicationRange.textProperty().bindBidirectional(wsnPrototype.getCommunicationRangeProperty(), new NumberStringConverter());
        txtSensingRange.textProperty().bindBidirectional(wsnPrototype.getSensingRangeProperty(), new NumberStringConverter());

        Platform.runLater(() -> solutionController.setMediator(mediator));
    }

    public WSNOptimizationProblem getWsnOptimizationProblem() {
        return wsnOptimizationProblem;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void solve(String algorithmType,
                      String mutationType,
                      double mutationRate,
                      String terminationType,
                      int terminationValue) {

        cleanSolution();
        initProblemInstance();
        solutionController.solve(
                wsnOptimizationProblem,
                algorithmType,
                mutationType,
                mutationRate,
                terminationType,
                terminationValue);
    }

    public void displaySolution(Solution solution) {
        solutionController.displaySolution(solution);
    }

    public void loadFromFile(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        File f = fc.showOpenDialog(stage);

        if (f == null) {
            return;
        }
        mediator.resetRegion();

        String src = f.getAbsolutePath();

        Point2D dimensions;
        Point2D[] targetArray;
        Point2D[] potentialPositionArray;

        try {
            Reader reader = Files.newBufferedReader(Paths.get(src));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parser = objectMapper.readTree(reader);

            JsonNode dimensionsNode = parser.path(Constants.DIMENSIONS);
            dimensions = new Point2D(dimensionsNode.get(0).asDouble(), dimensionsNode.get(1).asDouble());

            JsonNode targetsNode = parser.path(Constants.TARGETS);
            targetArray = new Point2D[targetsNode.size()];
            for (int i = 0; i < targetsNode.size(); i++) {
                JsonNode node = targetsNode.get(i);
                Point2D target = new Point2D(node.get(0).asDouble(), node.get(1).asDouble());
                targetArray[i] = target;
            }


            JsonNode potentialPositionsNode = parser.path(Constants.POTENTIAL_POSITIONS);
            potentialPositionArray = new Point2D[potentialPositionsNode.size()];
            for (int i = 0; i < potentialPositionsNode.size(); i++) {
                JsonNode node = potentialPositionsNode.get(i);
                Point2D potentialPos = new Point2D(node.get(0).asDouble(), node.get(1).asDouble());
                potentialPositionArray[i] = potentialPos;
            }

            wsnPrototype.setM(parser.path(Constants.M).asInt());
            wsnPrototype.setK(parser.path(Constants.K).asInt());
            wsnPrototype.setCommunicationRange(Double.parseDouble(parser.path(Constants.COMMUNICATION_RADIUS).asText()));
            wsnPrototype.setSensingRange(Double.parseDouble(parser.path(Constants.SENSING_RADIUS).asText()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        wsnPrototype.setPaneWidth((int) dimensions.getX());
        wsnPrototype.setPaneHeight((int) dimensions.getY());

        for (Point2D point2D : targetArray) {
            wsnPrototype.addTarget(new Target(point2D.getX(), point2D.getY()));
        }

        for (Point2D point2D : potentialPositionArray) {
            wsnPrototype.addPotentialPosition(new PotentialPosition(point2D.getX(), point2D.getY()));
        }
        initProblemInstance();
    }

    public void exportToFile(ActionEvent event) {
        try {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

            Button button = (Button) event.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            File f = fc.showSaveDialog(stage);

            if (f == null) {
                return;
            }

            String src = f.getAbsolutePath();

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(src));
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> problemInfo = new HashMap<>();
            problemInfo.put(Constants.DIMENSIONS, Arrays.asList(
                    wsnPrototype.getPaneWidth(),
                    wsnPrototype.getPaneHeight()));

            List<double[]> targetList = new ArrayList<>();
            for (Point2D target : wsnPrototype.getTargets()) {
                double[] coords = new double[2];
                coords[0] = target.getX();
                coords[1] = target.getY();
                targetList.add(coords);
            }
            problemInfo.put(Constants.TARGETS, targetList);

            List<double[]> potentialPositionList = new ArrayList<>();
            for (Point2D potentialPosition : wsnPrototype.getPotentialPositions()) {
                double[] coords = new double[2];
                coords[0] = potentialPosition.getX();
                coords[1] = potentialPosition.getY();
                potentialPositionList.add(coords);
            }
            problemInfo.put(Constants.POTENTIAL_POSITIONS, potentialPositionList);

            problemInfo.put(Constants.COMMUNICATION_RADIUS, wsnPrototype.getCommunicationRange());
            problemInfo.put(Constants.SENSING_RADIUS, wsnPrototype.getSensingRange());
            problemInfo.put(Constants.M, wsnPrototype.getM());
            problemInfo.put(Constants.K, wsnPrototype.getK());

            writer.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(problemInfo));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGenerateGrid(int distance) {
        if (distance == 0) {
            return;
        }

        for (double i = distance; i < wsnPrototype.getPaneHeight(); i += distance) {
            for (double j = distance; j < wsnPrototype.getPaneWidth(); j += distance) {
                wsnPrototype.addPotentialPosition(new PotentialPosition(j, i));
            }
        }
    }

    public void handleGenerateRandomTarget(int numberNodes) {
        if (numberNodes == 0) {
            return;
        }

        for (int i = 0; i < numberNodes; i++) {
            double x = Math.random() * wsnPrototype.getPaneWidth();
            double y = Math.random() * wsnPrototype.getPaneHeight();
            wsnPrototype.addTarget(new Target(x, y));
        }
    }

    public void cleanSolution() {
        mediator.removeSensorsFromPane();
        Sensor.clearHashMap();
    }

    private void initProblemInstance() {
        mediator.resetRegion();
        mediator.resizeMapPane(wsnPrototype.getPaneWidthProperty().get(), wsnPrototype.getPaneHeightProperty().get());

        // TODO: In MapController, add options to add targets
        //  or potential positions by Point2D
        //  or with their respective objects.

        for (Point2D target : wsnPrototype.getTargets()) {
            mediator.addTargetToPane(new Target(target.getX(), target.getY()));
        }

        for (Point2D potentialPosition : wsnPrototype.getPotentialPositions()) {
            mediator.addPotentialPositionToPane(new PotentialPosition(potentialPosition.getX(), potentialPosition.getY()));
        }

        Point2D[] targetArray = new Point2D[wsnPrototype.getTargetsSize()];
        Point2D[] potentialPositionArray = new Point2D[wsnPrototype.getPotentialPositionsSize()];

        for (int i = 0; i < targetArray.length; i++) {
            targetArray[i] = wsnPrototype.getTargetByIndex(i);
        }

        for (int i = 0; i < potentialPositionArray.length; i++) {
            potentialPositionArray[i] = wsnPrototype.getPotentialPositionByIndex(i);
        }

        WSN wsn = new WSN(targetArray,
                potentialPositionArray,
                wsnPrototype.getM(),
                wsnPrototype.getK(),
                wsnPrototype.getCommunicationRange(),
                wsnPrototype.getSensingRange(),
                0, // Dummy values
                0.0);

        wsnOptimizationProblem = new WSNOptimizationProblem(wsn, new WSNMinimumSensorObjective());
    }

    public void createProblemInstance(WSNPrototype wsnPrototype, int distance, int numberNodes) {
        this.wsnPrototype = wsnPrototype;

        handleGenerateGrid(distance);
        handleGenerateRandomTarget(numberNodes);

        initProblemInstance();
    }

    public void disableTextField(boolean bool) {
        txtM.setDisable(bool);
        txtK.setDisable(bool);
        txtCommunicationRange.setDisable(bool);
        txtSensingRange.setDisable(bool);
    }
}
