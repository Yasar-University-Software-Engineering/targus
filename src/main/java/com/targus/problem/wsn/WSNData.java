package com.targus.problem.wsn;

import core.problems.wsn.Point2D;
import core.representation.BitString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class WSNData {
    private final Point2D[] targets;
    private final Point2D[] potentialPositions;
    private HashMap<Point2D, Integer> potentialPositionsToIndexMap;
    private final int m;
    private final int k;

    private final HashMap<Integer, HashSet<Integer>> targetToPositions;
    private final HashMap<Integer, HashSet<Integer>> positionToPositions;

    public WSNData(WSN wsn)
    {
        m = wsn.getM();
        k = wsn.getK();
        targets = wsn.getTargets();
        potentialPositions=wsn.getPotentialPositions();
        potentialPositionsToIndexMap = wsn.getPotentialPositionToIndexMap();

        targetToPositions = new HashMap<>();

        for (int t = 0; t < targets.length; t++) {
            HashSet<Integer> positions= wsn.getTargetToPotentialPositionMap().get(targets[t]).stream().
                                            mapToInt(potentialPositionsToIndexMap::get).
                                            boxed().collect(Collectors.toCollection(HashSet::new));
            targetToPositions.put(t,positions);
        }

        positionToPositions = new HashMap<>();
        for (int pp = 0; pp < wsn.getSolutionSize(); pp++) {
            HashSet<Integer> positions= wsn.getPotentialPositionToPotentialPositionMap().get(potentialPositions[pp]).stream().
                    mapToInt(potentialPositionsToIndexMap::get).
                    boxed().collect(Collectors.toCollection(HashSet::new));
            positionToPositions.put(pp,positions);
        }
    }


    public int getK() {
        return k;
    }

    public Point2D[] getPotentialPositions() {
        return potentialPositions;
    }

    public int targetsSize() {
        return targets.length;
    }

    public int coverage(Integer target, HashSet<Integer> sensors) {
        return (int) targetToPositions.
                get(target).
                stream().
                filter(sensors::contains).
                count();
    }

    public int connectivity(Integer sensor, HashSet<Integer> sensors) {
        return (int) positionToPositions.
                get(sensor).
                stream().
                filter(sensors::contains).
                count();
    }

    public HashSet<Integer> getCoveringPositions(int target)
    {
        return targetToPositions.get(target);
    }

    public HashSet<Integer> getConnectedPositions(int sensor)
    {
        return positionToPositions.get(sensor);
    }

    public boolean isFeasible(BitString bs) {
        HashSet<Integer> sensors = bs.ones();
        for (int t = 0; t < targets.length; t++) {
            if (coverage(t,sensors)<k)
                return false;
        }
        for (Integer s :sensors)
        {
            if (connectivity(s,sensors)<m)
                return false;
        }

        return true;
    }

    public int getM() {
        return m;
    }
}
