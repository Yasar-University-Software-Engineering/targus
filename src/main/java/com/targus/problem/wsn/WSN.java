package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import javafx.geometry.Point2D;

import java.util.*;

public class WSN implements ProblemModel {
    private final Point2D[] targets;
    private final Point2D[] potentialPositions;
    private HashMap<Point2D, Integer> potentialPositionsToIndexMap;
    private final int m;
    private final int k;
    private final double commRange;
    private final double sensRange;
    private int generationCount;
    private double mutationRate;

    // holds potential position to potential position(s) map
    // keys represent each potential position
    // values represent other potential positions which are in the communication range
    HashMap<Point2D, HashSet<Point2D>> potentialPositionToPotentialPositionMap;

    // holds target to potential(s) position map
    // keys represent each target
    // values represent each potential position that can cover the target
    HashMap<Point2D, HashSet<Point2D>> targetToPotentialPositionMap;

    // holds potential position to target
    // keys represent each potential position
    // values represent each target that the potential position can cover
    HashMap<Point2D, HashSet<Point2D>> potentialPositionToTargetMap;

    public WSN(
            Point2D[] targets,
            Point2D[] potentialPositions,
            int m,
            int k,
            double commRange,
            double sensRange,
            int generationCount,
            double mutationRate) {

        this.targets = targets;
        this.potentialPositions = potentialPositions;
        this.m = m;
        this.k = k;
        this.commRange = commRange;
        this.sensRange = sensRange;
        this.generationCount = generationCount;
        this.mutationRate = mutationRate;

        potentialPositionToPotentialPositionMap = new HashMap<>();
        targetToPotentialPositionMap = new HashMap<>();
        potentialPositionToTargetMap = new HashMap<>();

        generateHashMap(potentialPositions, potentialPositions, potentialPositionToPotentialPositionMap, commRange);
        generateHashMap(targets, potentialPositions, targetToPotentialPositionMap, sensRange);
        generateHashMap(potentialPositions, targets, potentialPositionToTargetMap, sensRange);
        initPotentialPositionMap();
    }

    private void initPotentialPositionMap() {
        potentialPositionsToIndexMap = new HashMap<>();
        for (int i = 0; i < potentialPositions.length; i++) {
            potentialPositionsToIndexMap.put(potentialPositions[i], i);
        }
    }

    public int mConnSensors(Integer sensor, HashSet<Integer> sensors) {
        int count = 0;
        HashSet<Point2D> connSensors = potentialPositionToPotentialPositionMap.get(potentialPositions[sensor]);

        for (Point2D obj : connSensors) {
            Integer index = potentialPositionsToIndexMap.get(obj);
            if (sensors.contains(index))
                count++;
        }

        return count;
    }

    public int kCovTargets(Integer target, HashSet<Integer> sensors) {
        int count = 0;
        HashSet<Point2D> covSensors = targetToPotentialPositionMap.get(targets[target]);

        for (Point2D obj : covSensors) {
            Integer index = potentialPositionsToIndexMap.get(obj);
            if (sensors.contains(index))
                count++;
        }

        return count;
    }

    public Point2D[] getPotentialPositions() {
        return potentialPositions;
    }

    public Point2D[] getTargets() {
        return targets;
    }

    public int getM() {
        return m;
    }

    public int getK() {
        return k;
    }

    public int getSolutionSize() {
        return potentialPositions.length;
    }

    public double getCommRange() {
        return commRange;
    }

    public double getSensRange() {
        return sensRange;
    }

    public int getGenerationCount() {
        return generationCount;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int targetsSize() {
        return targets.length;
    }

    private void generateHashMap(
            Point2D[] coordinateSet1,
            Point2D[] coordinateSet2,
            HashMap<Point2D, HashSet<Point2D>> container,
            double distance) {

        HashSet<Point2D> point2Ds;
        for (Point2D point2D : coordinateSet1) {
            point2Ds = new HashSet<>();
            for (Point2D d : coordinateSet2) {
                if (point2D == d) {
                    continue;
                }
                if (point2D.distance(d) <= distance) {
                    point2Ds.add(d);
                }
            }
            container.put(point2D, point2Ds);
        }
    }

    public HashMap<Point2D, HashSet<Point2D>> getPotentialPositionToPotentialPositionMap() {
        return potentialPositionToPotentialPositionMap;
    }

    public HashMap<Point2D, HashSet<Point2D>> getTargetToPotentialPositionMap() {
        return targetToPotentialPositionMap;
    }

    public HashMap<Point2D, HashSet<Point2D>> getPotentialPositionToTargetMap() {
        return potentialPositionToTargetMap;
    }

    public HashMap<Point2D, Integer> getPotentialPositionToIndexMap() {
        return potentialPositionsToIndexMap;
    }
}
