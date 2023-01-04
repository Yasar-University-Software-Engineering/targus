package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.BitStringSolution;
import com.targus.problem.wsn.WSN;
import com.targus.represent.BitString;
import javafx.geometry.Point2D;

import java.security.SecureRandom;
import java.util.*;

public class WSNSolutionImprover implements SolutionImprover {

    private Random random;

    public WSNSolutionImprover() {
        random = new SecureRandom();
    }

    /**
     * Maps each potential position with its solutions index. For instance,
     * let potentialPositions = [ a, b, c, d] and solution = 1010. The returned
     * value would be { a:1, b:0, c:1, d:0 }
     * @param potentialPositions
     *      array of potential positions
     * @param solution
     *      the current solution
     * @return LinkedHashMap that holds key (potential position)
     * value (potential position's index in the problem model) pairs
     */
    private LinkedHashMap<Point2D, Boolean> createPotentialPositionStateMap(Point2D[] potentialPositions, Solution solution) {
        LinkedHashMap<Point2D, Boolean> map = new LinkedHashMap<>();
        BitString bitString = (BitString) solution.getRepresentation();

        for (int i = 0; i < potentialPositions.length; i++)
            map.put(potentialPositions[i], bitString.get(i));

        return map;
    }

    /**
     * Checks if the given potential position covers a target
     * @param potentialPosition
     *      sensor to check if it covers any target
     * @param targetsInSensingRange
     *      map of each potential position that can cover a target
     * @return true if the given sensor covers any target, false otherwise
     */
    private boolean doesCoverTarget(Point2D potentialPosition, HashMap<Point2D, HashSet<Point2D>> targetsInSensingRange) {
        HashSet<Point2D> targets = targetsInSensingRange.get(potentialPosition);
        return targets.size() != 0;
    }

    /**
     * Checks if any of the given chain of sensors cover a target
     * @param chain
     *      a sequence of sensor coordinates
     * @param wsn
     *      problem model object
     * @return true if a sensor covers a target, false otherwise
     */
    private boolean doesChainCoverTarget(LinkedList<Point2D> chain, WSN wsn) {
        for (Point2D pp : chain) {
            if (doesCoverTarget(pp, wsn.getTargetsInSensRange()))
                return true;
        }
        return false;
    }

    /**
     * Finds each turned on sensors in the solution and maps them with their corresponding index values
     * @param sensors
     *      map of sensors that holds which sensor is on/off
     * @return map of sensors with their index values
     */
    private Map<Point2D, Integer> getTurnedOnSensors(LinkedHashMap<Point2D, Boolean> sensors) {
        Map<Point2D, Integer> map = new HashMap<>();
        int index = 0;
        for (Map.Entry<Point2D, Boolean> item : sensors.entrySet()) {
            if (item.getValue()) {
                map.put(item.getKey(), index);
            }
            index += 1;
        }
        return map;
    }

    /**
     * Finds each turned off sensors in the solution and maps them with their corresponding index values
     * @param sensors
     *      map of sensors that holds which sensor is on/off
     * @return map of sensors with their index values
     */
    private Map<Point2D, Integer> getTurnedOffSensors(LinkedHashMap<Point2D, Boolean> sensors) {
        Map<Point2D, Integer> map = new HashMap<>();
        int index = 0;
        for (Map.Entry<Point2D, Boolean> item : sensors.entrySet()) {
            if (!item.getValue()) {
                map.put(item.getKey(), index);
            }
            index += 1;
        }
        return map;
    }

    /**
     * Creates a map that holds the information of which sensor is currently communicating with which sensor
     * The result is similar to below. Note that the numbers are arbitrary and arrays represent Point2D objects:
     * Point2D [x = 35.0, y = 5.0] = [ Point2D [x = 10.0, y = 5.0], Point2D [x = 35.0, y = 30.0] ]
     * @param sensors
     *      map of turned on sensors
     * @param wsn
     *      problem model object
     * @return map of connectivity
     */
    private Map<Point2D, HashSet<Point2D>> createConnectivityMap(Map<Point2D, Integer> sensors, WSN wsn) {
        Map<Point2D, HashSet<Point2D>> map = new HashMap<>();
        Map<Point2D, HashSet<Point2D>> potentialPositionCommunicationMap = wsn.getPositionsInCommRange();
        Set<Point2D> sensorKeySet = sensors.keySet();
        for (Point2D sensor : sensorKeySet) {
            HashSet<Point2D> temp = new HashSet<>();
            Set<Point2D> neighborSensors = potentialPositionCommunicationMap.get(sensor);
            for (Point2D s : neighborSensors) {
                if (sensorKeySet.contains(s)) {
                    temp.add(s);
                }
            }
            map.put(sensor, temp);
        }
        return map;
    }

    /**
     * Removes turned off sensors from the map
     * @param connectivityMap
     *      map that holds the information of which sensor is communicating with which sensor
     * @param chain
     *      a series of sensors that does not cover any target
     */
    private void updateConnectivityMap(Map<Point2D, HashSet<Point2D>> connectivityMap, LinkedList<Point2D> chain) {
        Iterator<Map.Entry<Point2D, HashSet<Point2D>>> iter = connectivityMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Point2D, HashSet<Point2D>> entry = iter.next();
            Point2D key = entry.getKey();
            HashSet<Point2D> value = entry.getValue();
            if (value.isEmpty() || chain.contains(key)) {
                iter.remove();
            }
            /*for (Point2D s : chain) {
                if (value.contains(s) || value.contains(key)) {
                    connectivityMap.get(key).remove(s);
                }
            }*/
        }
    }

    private Point2D getRandomElementFromSet(Set<Point2D> set) {
        int index = random.nextInt(set.size());
        int i = 0;
        for (Point2D s : set) {
            if (i == index) {
                return s;
            }
            i += 1;
        }
        return null;
    }

    private boolean doesContainCycle(LinkedList<Point2D> linkedList) {
        Set<Point2D> set = new HashSet<>();
        for (Point2D elem : linkedList) {
            if (set.contains(elem)) {
                return true;
            }
            set.add(elem);
        }
        return false;
    }

    /**
     * Finds unnecessary sensors and returns them in a list
     * @param connectivityMap
     *      map that holds the information of which sensors are communicating with which sensors
     * @param wsn
     *      problem model object
     * @return list of turned off sensors
     */
    private List<Point2D> getUnnecessarySensorList(Map<Point2D, HashSet<Point2D>> connectivityMap, WSN wsn) {
        List<Point2D> sensorsToRemove = new ArrayList<>();
        for (Map.Entry<Point2D, HashSet<Point2D>> entry : connectivityMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                sensorsToRemove.add(entry.getKey());
            }
        }
        for (Point2D sensor : sensorsToRemove) {
            connectivityMap.remove(sensor);
        }

        for (Map.Entry<Point2D, HashSet<Point2D>> entry : connectivityMap.entrySet()) {
            Point2D sensor = entry.getKey();
            HashSet<Point2D> otherSensors = entry.getValue();
            LinkedList<Point2D> chain = new LinkedList<>();
            chain.add(sensor);
            do {
                sensor = getRandomElementFromSet(otherSensors);
                chain.add(sensor);
                otherSensors = connectivityMap.get(sensor);
            } while (chain.getFirst() != chain.getLast() && !doesContainCycle(chain));
            if (!doesChainCoverTarget(chain, wsn)) {
                chain.removeLast();
                sensorsToRemove.addAll(chain);
            }
        }
        return sensorsToRemove;
    }

    private int getKCoverageForTarget(Point2D target, WSN wsn) {
        HashMap<Point2D, HashSet<Point2D>> targetsToSensorsMap =  wsn.getPositionsInSensRange();
        return targetsToSensorsMap.get(target).size();
    }

    private HashSet<Point2D>getTargetsForSensor(Point2D sensor, WSN wsn) {
        HashMap<Point2D, HashSet<Point2D>> sensorsToTargetsMap = wsn.getTargetsInSensRange();
        return sensorsToTargetsMap.get(sensor);
    }

    private List<Point2D> getNecessarySensorList(Set<Point2D> sensors, WSN wsn) {
        int K = wsn.getK();
        List<Point2D> sensorToAdd = new ArrayList<>();
        for (Point2D sensor : sensors) {
            HashSet<Point2D> targetsForSensor = getTargetsForSensor(sensor, wsn);
            List<Integer> kCoveragesForSensor = new ArrayList<>();
            for (Point2D target : targetsForSensor) {
                kCoveragesForSensor.add(getKCoverageForTarget(target, wsn));
            }
            if (kCoveragesForSensor.stream().anyMatch(k -> k < K)) {
                sensorToAdd.add(sensor);
            }
        }
        return sensorToAdd;
    }

    @Override
    public Solution improve(OptimizationProblem problem, Solution solution) {
        WSN wsn = (WSN) problem.model();
        LinkedHashMap<Point2D, Boolean> potentialPositionStateMap = createPotentialPositionStateMap(wsn.getPotentialPositions(), solution);
        Map<Point2D, Integer> turnedOnSensors = getTurnedOnSensors(potentialPositionStateMap);
        Map<Point2D, Integer> turnedOffSensors = getTurnedOffSensors(potentialPositionStateMap);
        BitString improvedSequence = (BitString) solution.getRepresentation();

        Map<Point2D, HashSet<Point2D>> connectivityMap = createConnectivityMap(turnedOnSensors, wsn);
        List<Point2D> removedSensors = getUnnecessarySensorList(connectivityMap, wsn);
        for (Point2D sensor : removedSensors) {
            improvedSequence.set(turnedOnSensors.get(sensor), false);
        }

        List<Point2D> addedSensors = getNecessarySensorList(turnedOffSensors.keySet(), wsn);
        for (Point2D sensor : addedSensors) {
            improvedSequence.set(turnedOffSensors.get(sensor), true);
        }

        return new BitStringSolution(improvedSequence, problem.objectiveValue(improvedSequence));
    }

    @Override
    public List<Solution> improveAll(OptimizationProblem problem, List<Solution> solutions) {
        List<Solution> improvedSolutions = new ArrayList<>();
        for (Solution solution : solutions) {
            improvedSolutions.add(improve(problem, solution));
        }
        return improvedSolutions;
    }
}
