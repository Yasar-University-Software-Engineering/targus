package com.targus.algorithm;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.HashSet;

public class GreedySelectionAlgorithm {

    OptimizationProblem problem;

    public GreedySelectionAlgorithm(OptimizationProblem problem) {
        this.problem = problem;
    }

    public Solution perform() {
        WSN wsn = (WSN) problem.model();
        Point2D[] potentialPositions = wsn.getPotentialPositions();
        Point2D[] targets = wsn.getTargets();
        HashMap<Point2D, HashSet<Point2D>> sensorToCommunicationSensorMap = wsn.getPositionsInCommRange();
        HashMap<Point2D, HashSet<Point2D>> sensorToSensingTargetMap = wsn.getTargetsInSensRange();
        int k = wsn.getK();
        int m = wsn.getM();

        // create a solution list to hold the indexes of pp's -> W
        // create a hashmap, key -> pp index, value -> pp cartesian product value
        // insert each pp with cartesian product value
        // sort the map by values

        // while each target is not covered m-connectivity is not achieved
        // get the pp with the highest value
        // add the pp to W
        // remove pp from the map

        // return W or the solution created from it

        return null;
    }

}
