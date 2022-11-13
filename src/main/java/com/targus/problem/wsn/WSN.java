package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    /*
    public int mConnPenSum(Representation r) { // Ignore bitset for now
        int penSum = 0;

        for(Map.Entry<Point2D, HashSet<Point2D>> entry : otherPositionsInCommRange.entrySet()) {
            Point2D key = entry.getKey();
            HashSet<Point2D> hashSet = entry.getValue();

            if (hashSet.size() < m) {
                penSum += m - hashSet.size();
            }
        }

        return penSum;
    }*/

    public int mConnSensors(Integer sensor, List<Integer> sensors) {
        HashSet<Point2D> connSensors = otherPositionsInCommRange.get(potentialPositionSet[sensor]);
        int count =0;
        for (Integer i:sensors)
        {
            if (i==sensor) continue;
            if (connSensors.contains(potentialPositionSet[i]))
                count++;
        }
        return count;
    }

    public int kCoverPenSum(Representation r) { // Ignore bitset for now
        int penSum = 0;

        for(Map.Entry<Point2D, HashSet<Point2D>> entry : otherPositionsInSensRange.entrySet()) {
            Point2D key = entry.getKey();
            HashSet<Point2D> hashSet = entry.getValue();

            if (hashSet.size() < k) {
                penSum += k - hashSet.size();
            }
        }

        return penSum;
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

    public int getM() {
        return m;
    }


}
