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
    }

    @Override
    public boolean isFeasible(Representation r) {
        return false;
    }
}
