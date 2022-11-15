package com.targus.problem.wsn;

import com.targus.base.SingleObjectiveOA;
import com.targus.base.Solution;
import com.targus.represent.BitString;
import javafx.geometry.Point2D;

import java.util.List;

public class WSNSensorOptimizationSolver {
    private SingleObjectiveOA singleObjectiveOA;
    private Solution solution;
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

    }

    public List<Integer> solve() {
        solution = singleObjectiveOA.perform(wsnOptimizationProblem);

        BitString bitString = (BitString) solution.getRepresentation();
        return bitString.ones();
    }

}
