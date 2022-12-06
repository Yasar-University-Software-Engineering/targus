package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import javafx.geometry.Point2D;

import java.util.*;

public class WSN implements ProblemModel {
    private final Point2D[] targets;
    private final Point2D[] potentialPositions;
    private final int m;
    private final int k;
    private final double commRange;
    private final double sensRange;
    private int generationCount;
    private double mutationRate;

    HashMap<Point2D, HashSet<Point2D>> positionsInCommRange;   // Potential Position to Potential Position Collection
    HashMap<Point2D, HashSet<Point2D>> positionsInSensRange;   // Target to Potential Position Collection
    HashMap<Point2D, HashSet<Point2D>> targetsInSensRange;     // Potential Position to Target Collection

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

        positionsInCommRange = new HashMap<>();
        positionsInSensRange = new HashMap<>();
        targetsInSensRange = new HashMap<>();

        generateHashMap(potentialPositions, potentialPositions, positionsInCommRange, commRange);
        generateHashMap(targets, potentialPositions, positionsInSensRange, sensRange);
        generateHashMap(potentialPositions, targets, targetsInSensRange, sensRange);
    }

    public int mConnSensors(Integer sensor, List<Integer> sensors) {
        int count = 0;
        HashSet<Point2D> connSensors = positionsInCommRange.get(potentialPositions[sensor]);

        for (Point2D obj : connSensors) {
            Integer index = Arrays.asList(potentialPositions).indexOf(obj);
            if (sensors.contains(index))
                count++;
        }

        return count;
    }

    public int kCovTargets(Integer target, List<Integer> sensors) {
        int count = 0;
        HashSet<Point2D> covSensors = positionsInSensRange.get(targets[target]);

        for (Point2D obj : covSensors) {
            Integer index = Arrays.asList(potentialPositions).indexOf(obj);
            if (sensors.contains(index))
                count++;
        }

        return count;
    }

    public int getM() {
        return m;
    }

    public int getK() {
        return k;
    }

    /*
    * TODO: There is a duplicate of this method. I could not rename it
    *  to something meaningful. Therefore, left it that way.
    * */
    public int getPopulationSize() {
        return potentialPositions.length;
    }

    public int getSolutionSize() {
        return potentialPositions.length;
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

}
