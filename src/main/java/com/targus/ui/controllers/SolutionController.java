package com.targus.ui.controllers;

import com.targus.algorithm.ga.GA;
import com.targus.base.Solution;
import com.targus.problem.BitStringSolution;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.represent.BitString;
import com.targus.ui.Mediator;
import com.targus.ui.widgets.Sensor;
import com.targus.utils.AlgorithmGenerator;
import javafx.application.Platform;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashSet;

public class SolutionController {
    private boolean initialSolutionApplied = false;
    private final ArrayList<Sensor> sensors = new ArrayList<>();
    private Solution solution;
    private Mediator mediator;

    public ArrayList<Sensor> getSensors() {
        return sensors;
    }

    public Solution getSolution() {
        return solution;
    }

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
        Point2D[] potentialPositionArray = wsn.getPotentialPositions();

        Sensor.initializeRadii(
                wsn.getCommRange(),
                wsn.getSensRange());

        mediator.configureChart(ga.getTerminalState(), terminationType);
        solution = ga.perform();

//        displaySolution(solution, potentialPositionArray);
    }

    public void displaySolution(Solution oldSolution, Solution newSolution, Point2D[] potentialPositionArray) {
        BitString oldSolutionBitString = (BitString) oldSolution.getRepresentation();
        BitString newSolutionBitString = (BitString) newSolution.getRepresentation();
        BitString xoredBitString = oldSolutionBitString.xor(newSolutionBitString);

        if (!initialSolutionApplied) {
            initialSolutionApplied = true;
            BitString zeroBitString = new BitString(oldSolutionBitString.length());
            Solution zeroSolution = new BitStringSolution(zeroBitString, 0);
            displaySolution(oldSolution, zeroSolution, potentialPositionArray);
            return;
        }

        HashSet<Integer> indexes = xoredBitString.ones();

        for (Integer index : indexes) {
            Point2D potentialPosition = potentialPositionArray[index];
            Sensor sensor = new Sensor(potentialPosition.getX(), potentialPosition.getY(), index);
            sensors.add(sensor);
            Platform.runLater(() -> mediator.addOrRemoveSensor(sensor));
        }
    }

    public void cleanSolution() {
        sensors.clear();
        solution = null;
    }
}
