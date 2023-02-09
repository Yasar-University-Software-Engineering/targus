package com.targus.problem.wsn;

import com.targus.algorithm.ga.GA;
import com.targus.algorithm.ga.GABuilder;
import com.targus.algorithm.ga.StandardGA;
import com.targus.problem.BitStringSolution;
import com.targus.represent.BitString;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.List;

public class WSNSensorOptimizationSolver {
    private GA ga;
    private BitStringSolution bitStringSolution;
    private WSN wsn;
    private WSNMinimumSensorObjective wsnMinimumSensorObjective;
    private WSNOptimizationProblem wsnOptimizationProblem;

    public WSNSensorOptimizationSolver(
                Point2D[] targets,
                Point2D[] potentialPositions,
                int m,
                int k,
                int commRange,
                int sensRange,
                int generationCount,
                double mutationRate) {

        wsn = new WSN(targets, potentialPositions, m, k, commRange, sensRange, generationCount, mutationRate);
        wsnMinimumSensorObjective = new WSNMinimumSensorObjective();
        wsnOptimizationProblem = new WSNOptimizationProblem(wsn, wsnMinimumSensorObjective);

        GABuilder gaBuilder = new GABuilder(new StandardGA(wsnOptimizationProblem));
        ga = gaBuilder.build();
    }

    public HashSet<Integer> solve() {
        bitStringSolution = (BitStringSolution) ga.perform();

        BitString bitString = (BitString) bitStringSolution.getRepresentation();
        return bitString.ones();
    }

}
