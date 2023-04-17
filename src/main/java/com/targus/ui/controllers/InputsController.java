package com.targus.ui.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targus.algorithm.ga.*;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.*;
import com.targus.represent.BitString;
import com.targus.ui.Mediator;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import com.targus.utils.Constants;
import com.targus.utils.ProgressTask;
import javafx.concurrent.Task;
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
import java.text.DecimalFormat;
import java.util.*;

public class InputsController implements Initializable {
    private WSNPrototype wsnPrototype = new WSNPrototype();
    private final ArrayList<Sensor> sensors = new ArrayList<>();
    @FXML
    private TextField txtM;
    @FXML
    private TextField txtK;
    @FXML
    private TextField txtCommunicationRange;
    @FXML
    private TextField txtSensingRange;
    private OptimizationProblem wsnOptimizationProblem;
    private Solution solution;
    private Mediator mediator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtM.textProperty().bindBidirectional(wsnPrototype.getMProperty(), new NumberStringConverter());
        txtK.textProperty().bindBidirectional(wsnPrototype.getKProperty(), new NumberStringConverter());
        txtCommunicationRange.textProperty().bindBidirectional(wsnPrototype.getCommunicationRangeProperty(), new NumberStringConverter());
        txtSensingRange.textProperty().bindBidirectional(wsnPrototype.getSensingRangeProperty(), new NumberStringConverter());
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public OptimizationProblem getWsnOptimizationProblem() {
        return wsnOptimizationProblem;
    }

    public Solution getSolution() {
        return solution;
    }

    public void handleLoadFromFile(ActionEvent event) {

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
            addTarget(new Target(point2D.getX(), point2D.getY()));
        }

        for (Point2D point2D : potentialPositionArray) {
            addPotentialPosition(new PotentialPosition(point2D.getX(), point2D.getY()));
        }
        initProblemInstance();
    }

    public void handleExportToFile(ActionEvent event) {
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
                addPotentialPosition(new PotentialPosition(j, i));
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
            addTarget(new Target(x, y));
        }
    }

    @FXML
    public void handleCleanSolution() {
        for (Sensor sensor : sensors) {
            mediator.removeChild(sensor);
        }
        sensors.clear();

        if (solution == null) {
            return;
        }
        BitString bitString = (BitString) solution.getRepresentation();
        bitString.getBitSet().clear();
        mediator.display();
    }

    public TerminalState buildTerminalState() {
        TerminalState terminalState;
        int terminationValue = mediator.getTerminationValue();

        if (mediator.getTermination().equals("Time Based")) {
            terminalState = new TimeBasedTerminal(terminationValue);
        } else if (mediator.getTermination().equals("Iteration Based")) {
            terminalState = new IterationBasedTerminal(terminationValue);
        } else {
            terminalState = null;
        }

        return terminalState;
    }

    public MutationOperator buildMutationOperator() {
        MutationOperator mutationOperator;
        Double mutationRate = mediator.getMutationRate();

        if (mediator.getMutation().equals("OneBitMutation")) {
            mutationOperator = new OneBitMutation(mutationRate);
        } else if (mediator.getMutation().equals("KBitMutation")) {
            mutationOperator = new KBitMutation(mutationRate);
        } else {
            mutationOperator = null;
        }

        return mutationOperator;
    }

    public GA buildStandardGA() {
        MutationOperator mutationOperator = buildMutationOperator();
        TerminalState terminalState = buildTerminalState();

        return StandardGA
                .builder(wsnOptimizationProblem)
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(mutationOperator)
                .setTerminalState(terminalState)
                .build();
    }

    // TODO: replace wsn.getGenerationCount() with time
    public GA buildImprovedGA(WSN wsn) {
        MutationOperator mutationOperator = buildMutationOperator();
        TerminalState terminalState = buildTerminalState();

        return ImprovedGA
                .builder(wsnOptimizationProblem)
                .setSolutionImprover(new WSNSolutionImprover(wsn, Constants.DEFAULT_IMPROVE_PROBABILITY))
                .setTerminalState(terminalState)
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(mutationOperator)
                .build();
    }

    @FXML
    public void handleSolve() {
        disableTextField(true);

        handleCleanSolution();
        initProblemInstance();

        WSN wsn = (WSN) wsnOptimizationProblem.model();

        String algorithmType = mediator.getAlgorithm();

        // TODO: we should replace this line with -> SingleObjectiveOA algorithm
        // I was going to do that but the progress bar fails. Since it takes time
        // to refactor this, I will leave it for later
        GA ga;

        switch (algorithmType) {
            case Constants.STANDARD_GA -> ga = buildStandardGA();
            case Constants.IMPROVED_GA -> ga = buildImprovedGA(wsn);
            case Constants.SIMULATED_ANNEALING -> ga = buildSimulatedAnnealing(wsn);
            case Constants.GREEDY_ALGORITHM -> ga = buildGreedyAlgorithm(wsn);
            default -> {
                try {
                    throw new Exception("No such algorithm available");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        ProgressTask progressTask = new ProgressTask(ga.getTerminalState());
        // progressTask.valueProperty().addListener((observable, oldValue, newValue) -> mediator.setProgressLabelText(String.valueOf(newValue)));
        mediator.bindProgressBar(progressTask.progressProperty());

        Thread thread = new Thread(progressTask);
        thread.setDaemon(true);
        thread.start();

        GA finalGa = ga;
        Task<Solution> gaTask = new Task<>() {
            @Override
            protected Solution call() {
                mediator.setProgressBarVisible(true);
                return finalGa.perform();
            }
        };

        gaTask.setOnSucceeded(e -> {
            mediator.setProgressBarVisible(false);
            solution = gaTask.getValue();
            BitString bitString = (BitString) solution.getRepresentation();
            HashSet<Integer> indexes = bitString.ones();

            Point2D[] potentialPositionArray = wsn.getPotentialPositions();

            Sensor.initializeRadii(
                    wsnPrototype.getCommunicationRange(),
                    wsnPrototype.getSensingRange());

            for (Integer index : indexes) {
                Point2D potentialPosition = potentialPositionArray[index];
                Sensor sensor = null;

                try {
                    sensor = new Sensor(potentialPosition.getX(), potentialPosition.getY());
                } catch (IllegalStateException illegalStateException) {
                    throw new IllegalStateException(illegalStateException.getMessage());
                }

                mediator.addSensorToPane(sensor);
                addSensor(sensor);
            }

            mediator.bringTargetsToFront();
            mediator.bringPotentialPositionsToFront();
            mediator.bringSensorDevicesToFront();

            mediator.display();
            disableTextField(false);
        });
        new Thread(gaTask).start();
    }

    private GA buildGreedyAlgorithm(WSN wsn) {
        return null;
    }

    private GA buildSimulatedAnnealing(WSN wsn) {
        return null;
    }

    private void disableTextField(boolean bool) {
        txtM.setDisable(bool);
        txtK.setDisable(bool);
        txtCommunicationRange.setDisable(bool);
        txtSensingRange.setDisable(bool);
    }

    private void initProblemInstance() {
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

    public void addTarget(Target target) {
        wsnPrototype.addTarget(target);
    }

    public void addPotentialPosition(PotentialPosition potentialPosition) {
        wsnPrototype.addPotentialPosition(potentialPosition);
    }

    public void addSensor(Sensor sensor) {
        double x = sensor.getCenterX();
        double y = sensor.getCenterY();

        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i < wsnPrototype.getPotentialPositions().size(); i++) {
            if (df.format(wsnPrototype.getPotentialPositionByIndex(i).getX()).equals(df.format(x))
                    && df.format(wsnPrototype.getPotentialPositionByIndex(i).getY()).equals(df.format(y))) {
                BitString bitString = (BitString) solution.getRepresentation();
                bitString.set(i, true);
            }
        }

        sensors.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        double x = sensor.getCenterX();
        double y = sensor.getCenterY();

        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i < wsnPrototype.getPotentialPositions().size(); i++) {
            if (df.format(wsnPrototype.getPotentialPositionByIndex(i).getX()).equals(df.format(x))
                    && df.format(wsnPrototype.getPotentialPositionByIndex(i).getY()).equals(df.format(y))) {
                BitString bitString = (BitString) solution.getRepresentation();
                bitString.set(i, false);
            }
        }

        sensors.remove(sensor);
    }

    public void clearTargets() {
        wsnPrototype.clearTargets();
    }

    public void clearPotentialPositions() {
        wsnPrototype.clearPotentialPositions();
    }

    public void clearSensors() {
        handleCleanSolution();
    }

    public void createProblemInstance(WSNPrototype wsnPrototype, int distance, int numberNodes) {
        this.wsnPrototype = wsnPrototype;

        handleGenerateGrid(distance);
        handleGenerateRandomTarget(numberNodes);

        initProblemInstance();
    }
}
