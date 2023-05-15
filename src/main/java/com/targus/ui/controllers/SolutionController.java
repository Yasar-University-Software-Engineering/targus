package com.targus.ui.controllers;

import com.targus.algorithm.ga.GA;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.represent.BitString;
import com.targus.ui.Mediator;
import com.targus.ui.widgets.Sensor;
import com.targus.utils.AlgorithmGenerator;
import com.targus.utils.ChartTask;
import com.targus.utils.DisplaySolutionTask;
import com.targus.utils.ProgressTask;
import javafx.concurrent.Task;

import java.util.HashSet;

public class SolutionController {
    private Mediator mediator;

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
        GA ga = algorithmGenerator.generateAlgorithm(
                algorithmType,
                mutationType,
                mutationRate,
                terminationType,
                terminationValue);

        WSN wsn = (WSN) wsnOptimizationProblem.model();

        Sensor.initializeRadii(
                wsn.getCommRange(),
                wsn.getSensRange());

        mediator.addSensorsToPane(Sensor.fillPotentialPositions(wsn.getPotentialPositions()));

        mediator.configureChart(ga.getTerminalState(), terminationType);

        ProgressTask progressTask = new ProgressTask(ga.getTerminalState());
        mediator.bindProgressBar(progressTask.progressProperty());
        Thread progressThread = new Thread(progressTask);
        progressThread.setDaemon(true);
        progressThread.start();

        DisplaySolutionTask displaySolutionTask = new DisplaySolutionTask(ga, mediator, ga.getTerminalState());
        Thread displaySolutionThread = new Thread(displaySolutionTask);
        displaySolutionThread.setDaemon(true);
        displaySolutionThread.start();

        ChartTask chartTask = new ChartTask(ga, mediator);
        Thread chartThread = new Thread(chartTask);
        chartThread.setDaemon(true);
        chartThread.start();

        Thread gaPerformThread = new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                mediator.setProgressBarVisible(true);
                ga.perform();
                mediator.setProgressBarVisible(false);
                return null;
            }
        });
        gaPerformThread.setDaemon(true);
        gaPerformThread.start();
    }

    public void displaySolution(Solution solution) {
        if (solution == null) {
            return;
        }
        BitString solutionBitString = (BitString) solution.getRepresentation();
        mediator.removeSensorsFromPane();
        HashSet<Integer> indexes = solutionBitString.ones();
        applyIndexesToPane(indexes);
    }

    public void applyIndexesToPane(HashSet<Integer> indexes) {
        for (Integer index : indexes) {
            Sensor sensor = Sensor.retrieveSensorFromHashMapByIndex(index);
            sensor.turnOn();
        }
    }
}
