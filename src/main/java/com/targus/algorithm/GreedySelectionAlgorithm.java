package com.targus.algorithm;

import com.targus.algorithm.base.SingleObjectiveOA;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.BitStringSolution;
import com.targus.problem.wsn.WSN;
import com.targus.represent.BitString;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.stream.Collectors;

public class GreedySelectionAlgorithm implements SingleObjectiveOA {

    OptimizationProblem problem;
    private final WSN wsn;
    private final HashMap<Point2D, HashSet<Point2D>> sensorToCommunicationSensorMap;
    private final HashMap<Point2D, HashSet<Point2D>> sensorToSensingTargetMap;
    private final Point2D[] potentialPositions;

    public GreedySelectionAlgorithm(OptimizationProblem problem) {
        this.problem = problem;
        wsn = (WSN) problem.model();
        sensorToCommunicationSensorMap = wsn.getPotentialPositionToPotentialPositionMap();
        sensorToSensingTargetMap = wsn.getPotentialPositionToTargetMap();
        potentialPositions = wsn.getPotentialPositions();
    }

    @Override
    public Solution perform() {
        Map<Integer, Integer> indexToPotentialPositionValueMap = mapIndexesToPotentialPositions();
        LinkedHashMap<Integer, Integer> sortedIndexToPotentialPositionValueMap = sortMapByValue(indexToPotentialPositionValueMap);

        List<Integer> potentialPositionIndexes = new ArrayList<>();
        while (isObjectiveSatisfied(wsn, potentialPositionIndexes)) {
            try {
                int key = sortedIndexToPotentialPositionValueMap.entrySet().iterator().next().getKey();
                potentialPositionIndexes.add(key);
                sortedIndexToPotentialPositionValueMap.remove(key);
            }
            catch (NoSuchElementException e) {
                break;
            }
        }

        return generateSolution(potentialPositionIndexes);
    }

    private BitStringSolution generateSolution(List<Integer> potentialPositionIndexes) {
        BitSet bitSetSolution = new BitSet();
        for (Integer index : potentialPositionIndexes) {
            bitSetSolution.set(index);
        }
        // 0 is given for temporarily. Its side effect is not tested. Check this code before running this algorithm
        BitString bitString = new BitString(bitSetSolution, 0);
        return new BitStringSolution(bitString, problem.objectiveValue(bitString));
    }

    private Map<Integer, Integer> mapIndexesToPotentialPositions() {
        Map<Integer, Integer> indexToPotentialPositionValueMap = new HashMap<>();
        for (int i = 0; i < potentialPositions.length; i++) {
            Point2D pp = potentialPositions[i];
            int value = sensorToCommunicationSensorMap.get(pp).size() * sensorToSensingTargetMap.get(pp).size();
            indexToPotentialPositionValueMap.put(i, value);
        }
        return indexToPotentialPositionValueMap;
    }

    private LinkedHashMap<Integer, Integer> sortMapByValue(Map<Integer, Integer> indexToPotentialPositionValueMap) {
        List<Integer> values = indexToPotentialPositionValueMap.values()
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        LinkedHashMap<Integer, Integer> sortedIndexToPotentialPositionValueMap = new LinkedHashMap<>();
        for (Integer value : values) {
            for (Map.Entry<Integer, Integer> entry : indexToPotentialPositionValueMap.entrySet()) {
                if (entry.getValue().equals(value)) {
                    sortedIndexToPotentialPositionValueMap.put(entry.getKey(), value);
                }
            }
        }
        return sortedIndexToPotentialPositionValueMap;
    }

    private boolean isObjectiveSatisfied(WSN wsn, List<Integer> potentialPositionIndexes) {
        return !checkKCoverage(wsn, potentialPositionIndexes) || !checkMConnectivity(wsn, potentialPositionIndexes);
    }

    private boolean checkMConnectivity(WSN wsn, List<Integer> currentSensorIndexes) {
        int m = wsn.getM();
        List<Point2D> currentPotentialPositions = getPotentialPositionsFromIndexes(wsn, currentSensorIndexes);
        HashMap<Point2D, HashSet<Point2D>> sensorToCommunicationSensorMap = wsn.getPotentialPositionToPotentialPositionMap();
        for (Map.Entry<Point2D, HashSet<Point2D>> entry : sensorToCommunicationSensorMap.entrySet()) {
            HashSet<Point2D> potentialPositions = entry.getValue();
            int currentM = -1;
            for (Point2D pp : potentialPositions) {
                if (currentPotentialPositions.contains(pp)) {
                    currentM++;
                }
            }
            if (currentM < m) {
                return false;
            }
        }
        return true;
    }

    private boolean checkKCoverage(WSN wsn, List<Integer> currentSensorIndexes) {
        int k = wsn.getK();
        List<Point2D> currentPotentialPositions = getPotentialPositionsFromIndexes(wsn, currentSensorIndexes);
        HashMap<Point2D, HashSet<Point2D>> targetToSensorMap = wsn.getTargetToPotentialPositionMap();
        for (Map.Entry<Point2D, HashSet<Point2D>> entry : targetToSensorMap.entrySet()) {
            HashSet<Point2D> potentialPositions = entry.getValue();
            int currentK = 0;
            for (Point2D pp : potentialPositions) {
                if (currentPotentialPositions.contains(pp)) {
                    currentK++;
                }
            }
            if (currentK < k) {
                return false;
            }
        }
        return true;
    }

    private List<Point2D> getPotentialPositionsFromIndexes(WSN wsn, List<Integer> indexList) {
        Point2D[] potentialPositions = wsn.getPotentialPositions();
        List<Point2D> result = new ArrayList<>();
        for (Integer index : indexList) {
            result.add(potentialPositions[index]);
        }

        return result;
    }

    @Override
    public String getName() {
        return "Greedy Selection Algorithm";
    }
}
