package com.targus.ui.controllers;

import com.targus.algorithm.GreedySelectionAlgorithm;
import com.targus.algorithm.base.SingleObjectiveOA;
import com.targus.algorithm.ga.ImprovedGA;
import com.targus.algorithm.ga.StandardGA;
import com.targus.algorithm.sa.SA;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.represent.BitString;
import com.targus.ui.Mediator;
import com.targus.ui.widgets.Sensor;
import com.targus.utils.AlgorithmGenerator;
import com.targus.utils.Constants;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.util.HashSet;

public class SolutionController {
    private Mediator mediator;
    private static Solution solution;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void solve(WSNOptimizationProblem wsnOptimizationProblem,
                      String algorithmType,
                      String mutationType,
                      double mutationRate,
                      String terminationType,
                      int terminationValue) {

        AlgorithmGenerator algorithmGenerator = new AlgorithmGenerator(wsnOptimizationProblem);
        SingleObjectiveOA algorithm = null;
        try {
            algorithm = algorithmGenerator.generateAlgorithm(
                    algorithmType,
                    mutationType,
                    mutationRate,
                    terminationType,
                    terminationValue);
            if (algorithm == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Algorithm Selection");
            alert.setContentText("Invalid algorithm or algorithm parameters were selected!");
            alert.showAndWait();
            return;
        }

        WSN wsn = (WSN) wsnOptimizationProblem.model();

        Sensor.initializeRadii(
                wsn.getCommRange(),
                wsn.getSensRange());

        mediator.cleanSolution();
        mediator.addSensorsToPane(Sensor.fillPotentialPositions(wsn.getPotentialPositions()));

        String algorithmName = algorithm.getName();
        switch (algorithmName) {
            case Constants.STANDARD_GA -> {
                StandardGA stdGA = (StandardGA) algorithm;
                mediator.configureChart(stdGA.getTerminalState(), terminationType);
            }
            case Constants.IMPROVED_GA -> {
                ImprovedGA improvedGA = (ImprovedGA) algorithm;
                mediator.configureChart(improvedGA.getTerminalState(), terminationType);
            }
            case Constants.SIMULATED_ANNEALING -> {
                SA sa = (SA) algorithm;
                mediator.configureChart(sa.getTerminalState(), terminationType);
            }
            case Constants.GREEDY_ALGORITHM -> {
                GreedySelectionAlgorithm greedy = (GreedySelectionAlgorithm) algorithm;
                mediator.configureChart(greedy.getTerminalState(), terminationType);
            }
        }

        SingleObjectiveOA finalGa = algorithm;

        Task<Solution> gaTask = new Task<>() {
            @Override
            protected Solution call() {
                mediator.disableTextField(true);
                Solution lastSolution = finalGa.perform();
                mediator.disableTextField(false);
                return lastSolution;
            }
        };

        gaTask.setOnSucceeded(event -> solution = gaTask.getValue());

        Thread gaPerformThread = new Thread(gaTask);
        gaPerformThread.setDaemon(true);
        gaPerformThread.start();
    }

    public void displaySolution(Solution solution) {
        if (solution == null) {
            return;
        }
        BitString solutionBitString = (BitString) solution.getRepresentation();
        mediator.turnOffAllSensors();
        HashSet<Integer> indexes = solutionBitString.ones();
        applyIndexesToPane(indexes);
    }

    public void applyIndexesToPane(HashSet<Integer> indexes) {
        Sensor.setTurnedOnSensors(indexes);
        for (Integer index : indexes) {
            Sensor sensor = Sensor.retrieveSensorFromHashMapByIndex(index);
            sensor.turnOn();
        }
    }
}
