package com.targus.ui.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targus.algorithm.ga.*;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.problem.wsn.WSNSolutionImprover;
import com.targus.represent.BitString;
import com.targus.ui.Mediator;
import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Sensor;
import com.targus.ui.widgets.Target;
import com.targus.utils.Constants;
import com.targus.utils.ProgressTask;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
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
    @FXML
    private TextField txtAreaWidth;
    @FXML
    private TextField txtAreaHeight;
    @FXML
    private TextField txtM;
    @FXML
    private TextField txtK;
    @FXML
    private TextField txtCommunicationRange;
    @FXML
    private TextField txtSensingRange;
    @FXML
    private TextField txtMutationRate;
    @FXML
    private TextField txtGenerationCount;
    private int paneWidth;
    private int paneHeight;
    private final ArrayList<Point2D> targets = new ArrayList<>();
    private final ArrayList<Point2D> potentialPositions = new ArrayList<>();
    private final ArrayList<Sensor> sensors = new ArrayList<>();
    private final IntegerProperty mProperty = new SimpleIntegerProperty(1);
    private final IntegerProperty kProperty = new SimpleIntegerProperty(1);
    private final DoubleProperty communicationRangeProperty = new SimpleDoubleProperty(100);
    private final DoubleProperty sensingRangeProperty = new SimpleDoubleProperty(50);
    private final IntegerProperty generationCountProperty = new SimpleIntegerProperty(1000);
    private final DoubleProperty mutationRateProperty = new SimpleDoubleProperty(0.3);

    @FXML
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private OptimizationProblem optimizationProblem;
    private Solution solution;
    private Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public OptimizationProblem getOptimizationProblem() {
        return optimizationProblem;
    }

    public Solution getSolution() {
        return solution;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtM.textProperty().bindBidirectional(mProperty, new NumberStringConverter());
        txtK.textProperty().bindBidirectional(kProperty, new NumberStringConverter());
        txtCommunicationRange.textProperty().bindBidirectional(communicationRangeProperty, new NumberStringConverter());
        txtSensingRange.textProperty().bindBidirectional(sensingRangeProperty, new NumberStringConverter());
        txtGenerationCount.textProperty().bindBidirectional(generationCountProperty, new NumberStringConverter());
        txtMutationRate.textProperty().bindBidirectional(mutationRateProperty, new NumberStringConverter());

        choiceBox.getItems().add("Standard GA");
        choiceBox.getItems().add("Improved GA");
        choiceBox.getItems().add("Greedy Algorithm");
        choiceBox.setValue("Standard GA");
    }

    public void handleLoadFromFile() {
        mediator.resetRegion();

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("./src/main/resources/json/"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File f = fc.showOpenDialog(null);

        if (f == null) {
            return;
        }

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

            mProperty.set(parser.path(Constants.M).asInt());
            kProperty.set(parser.path(Constants.K).asInt());
            communicationRangeProperty.set(Double.parseDouble(parser.path(Constants.COMMUNICATION_RADIUS).asText()));
            sensingRangeProperty.set(Double.parseDouble(parser.path(Constants.SENSING_RADIUS).asText()));
            generationCountProperty.set(parser.path(Constants.GENERATION_COUNT).asInt());
            mutationRateProperty.set(Double.parseDouble(parser.path(Constants.MUTATION_RATE).asText()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        paneWidth = (int) dimensions.getX();
        paneHeight = (int) dimensions.getY();

        for (Point2D point2D : targetArray) {
            addTarget(new Target(point2D.getX(), point2D.getY()));
        }

        for (Point2D point2D : potentialPositionArray) {
            addPotentialPosition(new PotentialPosition(point2D.getX(), point2D.getY()));
        }

        initProblemInstance();
    }

    public void handleExportToFile() {
        try {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File("./src/main/resources/json/"));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File f = fc.showSaveDialog(null);

            if (f == null) {
                return;
            }

            String src = f.getAbsolutePath();

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(src));
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> problemInfo = new HashMap<>();
            problemInfo.put(Constants.DIMENSIONS, Arrays.asList(paneWidth, paneWidth));

            List<double[]> targetList = new ArrayList<>();
            for (Point2D target : targets) {
                double[] coords = new double[2];
                coords[0] = target.getX();
                coords[1] = target.getY();
                targetList.add(coords);
            }
            problemInfo.put(Constants.TARGETS, targetList);

            List<double[]> potentialPositionList = new ArrayList<>();
            for (Point2D potentialPosition : potentialPositions) {
                double[] coords = new double[2];
                coords[0] = potentialPosition.getX();
                coords[1] = potentialPosition.getY();
                potentialPositionList.add(coords);
            }
            problemInfo.put(Constants.POTENTIAL_POSITIONS, potentialPositionList);

            problemInfo.put(Constants.COMMUNICATION_RADIUS, communicationRangeProperty.get());
            problemInfo.put(Constants.SENSING_RADIUS, sensingRangeProperty.get());
            problemInfo.put(Constants.M, mProperty.get());
            problemInfo.put(Constants.K, kProperty.get());
            problemInfo.put(Constants.GENERATION_COUNT, generationCountProperty.get());
            problemInfo.put(Constants.MUTATION_RATE, mutationRateProperty.get());

            writer.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(problemInfo));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleGenerateGrid() {
        for (int i = 5; i < paneHeight; i += 25) {
            for (int j = 10; j < paneWidth; j += 25) {
                addPotentialPosition(new PotentialPosition(j, i));
            }
        }
        initProblemInstance();
    }

    @FXML
    void handleCleanSolution() {
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

    public GA buildStandardGA(WSN wsn) {
        return StandardGA
                .builder(optimizationProblem)
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(new OneBitMutation())
                .setTerminalState(new TimeBasedTerminal(wsn.getGenerationCount()))
                .build();
    }

    // TODO: replace wsn.getGenerationCount() with time
    public GA buildImprovedGA(WSN wsn) {
        return ImprovedGA
                .builder(optimizationProblem)
                .setSolutionImprover(new WSNSolutionImprover())
                .setTerminalState(new TimeBasedTerminal(wsn.getGenerationCount()))
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(new OneBitMutation())
                .build();
    }

    @FXML
    void handleSolve()  {
        disableTextField(true);

        handleCleanSolution();
        initProblemInstance();

        WSN wsn = (WSN) optimizationProblem.model();

        GA ga = buildImprovedGA(wsn); // In case choice box doesn't get initialized properly

        if (choiceBox.getValue().equals("Standard GA")) {
            ga = buildStandardGA(wsn);
        } else if (choiceBox.getValue().equals("Improved GA")) {
            ga = buildImprovedGA(wsn);
        } else if (choiceBox.getValue().equals("Greedy Algorithm")) {
            ga = null;
        } else {
            try {
                throw new Exception("No such algorithm available");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        ProgressTask progressTask = new ProgressTask(ga.getTerminalState());
        progressTask.valueProperty().addListener((observable, oldValue, newValue) -> mediator.setProgressLabelText(String.valueOf(newValue)));
        mediator.bindProgressBar(progressTask.progressProperty());

        Thread thread = new Thread(progressTask);
        thread.setDaemon(true);
        thread.start();

        GA finalGa = ga;
        Task<Solution> gaTask = new Task<>() {
            @Override
            protected Solution call() {
                return finalGa.perform();
            }
        };

        gaTask.setOnSucceeded(e -> {
            mediator.setProgressLabelText("GA is completed!");
            solution = gaTask.getValue();
            BitString bitString = (BitString) solution.getRepresentation();
            HashSet<Integer> indexes = bitString.ones();

            Point2D[] potentialPositionArray = wsn.getPotentialPositions();

            Sensor.initializeRadii(communicationRangeProperty.get(), sensingRangeProperty.get());

            for (Integer index: indexes) {
                Point2D potentialPosition = potentialPositionArray[index];
                Sensor sensor = null;

                try {
                    sensor = new Sensor(potentialPosition.getX(), potentialPosition.getY());
                } catch (IllegalStateException illegalStateException) {
                    throw new IllegalStateException(illegalStateException.getMessage());
                }

                mediator.addChild(sensor);
                addSensor(sensor);
            }
            mediator.display();
            disableTextField(false);
        });
        new Thread(gaTask).start();
        // Below line is duplicated on purpose. It will be removed in the refactoring phase
        mediator.setProgressLabelText("GA is completed!");
    }

    private void disableTextField(boolean bool) {
        txtM.setDisable(bool);
        txtK.setDisable(bool);
        txtCommunicationRange.setDisable(bool);
        txtSensingRange.setDisable(bool);
        txtGenerationCount.setDisable(bool);
        txtMutationRate.setDisable(bool);
    }

    private void initProblemInstance() {
        mediator.resizeMapPane(paneWidth, paneHeight);

        for (Point2D target: targets) {
            mediator.addChild(new Target(target.getX(), target.getY()));
        }

        for (Point2D potentialPosition: potentialPositions) {
            mediator.addChild(new PotentialPosition(potentialPosition.getX(), potentialPosition.getY()));
        }

        Point2D[] targetArray = new Point2D[targets.size()];
        Point2D[] potentialPositionArray = new Point2D[potentialPositions.size()];

        for (int i = 0; i < targetArray.length; i++) {
            targetArray[i] = targets.get(i);
        }

        for (int i = 0; i < potentialPositionArray.length; i++) {
            potentialPositionArray[i] = potentialPositions.get(i);
        }

        WSN wsn = new WSN(targetArray,
                potentialPositionArray,
                mProperty.get(),
                kProperty.get(),
                communicationRangeProperty.get(),
                sensingRangeProperty.get(),
                generationCountProperty.get(),
                mutationRateProperty.get());

        optimizationProblem = new WSNOptimizationProblem(wsn, new WSNMinimumSensorObjective());
    }

    @FXML
    void handleResetRegion() {
        mediator.resetRegion();
    }

    @FXML
    void handleRemoveChildren() {
        mediator.removeChildren();
    }

    @FXML
    public void handleResizeMapPane() {
        paneWidth = Integer.parseInt(txtAreaWidth.getText());
        paneHeight = Integer.parseInt(txtAreaHeight.getText());
        mediator.resizeMapPane(paneWidth, paneHeight);
    }

    public void addTarget(Target target) {
        targets.add(new Point2D(target.getCenterX(), target.getCenterY()));
    }

    public void addPotentialPosition(PotentialPosition potentialPosition) {
        potentialPositions.add(new Point2D(potentialPosition.getCenterX(), potentialPosition.getCenterY()));
    }

    public void addSensor(Sensor sensor) {
        double x = sensor.getCenterX();
        double y = sensor.getCenterY();

        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i < potentialPositions.size(); i++) {
            if (df.format(potentialPositions.get(i).getX()).equals(df.format(x))
                    && df.format(potentialPositions.get(i).getY()).equals(df.format(y))) {
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

        for (int i = 0; i < potentialPositions.size(); i++) {
            if (df.format(potentialPositions.get(i).getX()).equals(df.format(x))
            && df.format(potentialPositions.get(i).getY()).equals(df.format(y))) {
                BitString bitString = (BitString) solution.getRepresentation();
                bitString.set(i, false);
            }
        }

        sensors.remove(sensor);
    }

    public void clearTargets() {
        targets.clear();
    }

    public void clearPotentialPositions() {
        potentialPositions.clear();
    }

    public void clearSensors() {
        handleCleanSolution();
    }
}
