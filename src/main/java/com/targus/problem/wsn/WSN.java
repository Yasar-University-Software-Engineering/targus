package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.HashSet;

public class WSN implements ProblemModel {
    Point2D[] targetSet;
    Point2D[] potentialPositionSet;
    int m;
    int k;
    double commRange;
    double sensRange;

    HashMap<Point2D, HashSet<Point2D>> otherPositionsInCommRange;   // Potential Position to Potential Position Set
    HashMap<Point2D, HashSet<Point2D>> otherPositionsInSensRange;   // Target to Potential Position Set
    HashMap<Point2D, HashSet<Point2D>> otherTargetsInSensRange;     // Potential Position to Target Set

    public WSN(Point2D[] targetSet, Point2D[] potentialPositionSet, int m, int k, int commRange, int sensRange) {
        this.targetSet = targetSet;
        this.potentialPositionSet = potentialPositionSet;
        this.m = m;
        this.k = k;
        this.commRange = commRange;
        this.sensRange = sensRange;

        otherPositionsInCommRange = new HashMap<>();
        otherPositionsInSensRange = new HashMap<>();
        otherTargetsInSensRange = new HashMap<>();

        generateHashMap(potentialPositionSet, potentialPositionSet, otherPositionsInCommRange, commRange);
        generateHashMap(targetSet, potentialPositionSet, otherPositionsInSensRange, sensRange);
        generateHashMap(potentialPositionSet, targetSet, otherTargetsInSensRange, sensRange);
    }

    private void generateHashMap(
            Point2D[] coordinateSet1,
            Point2D[] coordinateSet2,
            HashMap<Point2D, HashSet<Point2D>> container,
            double distance) {

        HashSet<Point2D> point2Ds;
        for (int i = 0; i < coordinateSet1.length; i++) {
            point2Ds = new HashSet<>();
            for (int j = 0; j < coordinateSet2.length; j++) {
                if (coordinateSet1[i] == coordinateSet2[j]) { continue; }
                if (coordinateSet1[i].distance(coordinateSet2[j]) <= distance) {
                    point2Ds.add(coordinateSet2[j]);
                }
            }
            container.put(coordinateSet1[i], point2Ds);
        }
    }

    @Override
    public boolean isFeasible(Representation r) {
        return false;
    }
}
