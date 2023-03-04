package com.targus.problem.wsn;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.BitStringSolution;
import com.targus.represent.BitString;
import javafx.geometry.Point2D;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class WSNSolutionImprover implements SolutionImprover {

    private final Random random;
    private final WSN wsn;
    private final HashMap<Point2D, HashSet<Point2D>> targetToPotentialPositions;
    private final HashMap<Point2D, HashSet<Point2D>> potentialPositionToTargets;
    private final HashMap<Point2D, HashSet<Point2D>> potentialPositionToPotentialPosition;
    private final double IMPROVE_PROBABILITY;

    public WSNSolutionImprover(WSN wsn, double improveProbability) {
        random = new SecureRandom();
        this.wsn = wsn;
        targetToPotentialPositions = wsn.getTargetToPotentialPositionMap();
        potentialPositionToTargets = wsn.getPotentialPositionToTargetMap();
        potentialPositionToPotentialPosition = wsn.getPotentialPositionToPotentialPositionMap();
        IMPROVE_PROBABILITY = improveProbability;
    }

    /**
     * Maps each potential position with its solutions state. For instance,
     * let potentialPositions = [ a, b, c, d] and solution = 1010. The returned
     * value would be { a:1, b:0, c:1, d:0 }
     * @param potentialPositions
     *      array of potential positions
     * @param solution
     *      the current solution
     * @return LinkedHashMap that holds key (potential position)
     * value (potential position's state) pairs
     */
    private LinkedHashMap<Point2D, Boolean> createPotentialPositionStateMap(Point2D[] potentialPositions, Solution solution) {
        LinkedHashMap<Point2D, Boolean> map = new LinkedHashMap<>();
        BitString bitString = (BitString) solution.getRepresentation();

        for (int i = 0; i < potentialPositions.length; i++)
            map.put(potentialPositions[i], bitString.get(i));

        return map;
    }

    // TODO: this method will be removed
    /**
     * Checks if the given potential position covers a target
     * @param potentialPosition
     *      sensor to check if it covers any target
     * @return true if the given sensor covers any target, false otherwise
     */
    private boolean doesCoverTarget(Point2D potentialPosition) {
        HashSet<Point2D> targets = potentialPositionToTargets.get(potentialPosition);
        return targets.size() != 0;
    }

    // TODO: this method will be removed
    /**
     * Checks if any of the given chain of sensors cover a target
     * @param chain
     *      a sequence of sensor coordinates
     * @return true if a sensor covers a target, false otherwise
     */
    private boolean doesChainCoverTarget(LinkedList<Point2D> chain) {
        for (Point2D pp : chain) {
            if (doesCoverTarget(pp))
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

    // TODO: this method will be removed
    /**
     * Creates a map that holds the information of which sensor is currently communicating with which sensor
     * The result is similar to below. Note that the numbers are arbitrary and arrays represent Point2D objects:
     * Point2D [x = 35.0, y = 5.0] = [ Point2D [x = 10.0, y = 5.0], Point2D [x = 35.0, y = 30.0] ]
     * @param sensors
     *      map of turned on sensors
     * @return map of connectivity
     */
    private Map<Point2D, HashSet<Point2D>> createConnectivityMap(Map<Point2D, Integer> sensors) {
        Map<Point2D, HashSet<Point2D>> map = new HashMap<>();
        Set<Point2D> sensorKeySet = sensors.keySet();
        for (Point2D sensor : sensorKeySet) {
            HashSet<Point2D> temp = new HashSet<>();
            Set<Point2D> neighborSensors = potentialPositionToPotentialPosition.get(sensor);
            for (Point2D s : neighborSensors) {
                if (sensorKeySet.contains(s)) {
                    temp.add(s);
                }
            }
            map.put(sensor, temp);
        }
        return map;
    }

    // TODO: this method will be removed
    /**
     * Gets random element from the given set
     * @param set
     *      set to pick random element from
     * @return random element from the given set
     */
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

    // TODO: this method will be removed
    /**
     * Checks whether the given linked list contains cycle
     * @param linkedList
     *      linked list to check
     * @return true if the linked list contains cycle, false otherwise
     */
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

    /* TODO: this method will be removed. because the amount of work it does does not worth the trouble
        to gain performance we do not call this method. we plan to remove this later on but just for now
        we will keep it
     */
    /**
     * Finds unnecessary sensors and returns them in a list
     * @param connectivityMap
     *      map that holds the information of which sensors are communicating with which sensors
     * @return list of turned off sensors
     */
    private List<Point2D> getSensorsToTurnOff(Map<Point2D, HashSet<Point2D>> connectivityMap) {
        List<Point2D> sensorsToRemove = new ArrayList<>();

        connectivityMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isEmpty())
                .forEach(entry -> sensorsToRemove.add(entry.getKey()));

        sensorsToRemove.forEach(connectivityMap::remove);

        for (Map.Entry<Point2D, HashSet<Point2D>> entry : connectivityMap.entrySet()) {
            Point2D sensor = entry.getKey();
            HashSet<Point2D> communicatingSensors = entry.getValue();

            LinkedList<Point2D> chain = new LinkedList<>();
            chain.add(sensor);
            do {
                sensor = getRandomElementFromSet(communicatingSensors);
                chain.add(sensor);
                communicatingSensors = connectivityMap.get(sensor);
            } while (!doesContainCycle(chain));
            if (!doesChainCoverTarget(chain)) {
                chain.removeLast();
                sensorsToRemove.addAll(chain);
            }
        }
        return sensorsToRemove;
    }

    // TODO: this method will be removed
    private void removeSensorFromConnectivityMap(Map<Point2D, HashSet<Point2D>> connectivityMap, Point2D sensor) {
        HashSet<Point2D> sensorKeysToUpdate = connectivityMap.get(sensor);
        for (Point2D sensorToUpdate : sensorKeysToUpdate) {
            HashSet<Point2D> values = connectivityMap.get(sensorToUpdate);
            HashSet<Point2D> newValues = (HashSet<Point2D>) values.stream().filter(s -> !s.equals(sensor)).collect(Collectors.toSet());
            // values.remove(sensor);
            connectivityMap.replace(sensorToUpdate, newValues);
        }
    }

    // TODO: this method will be removed
    private boolean doesSensorContributeToConnectivity(Map<Point2D, HashSet<Point2D>> connectivityMap, Point2D sensor, int m) {
        boolean doesContribute = false;
        HashSet<Point2D> sensorsToCheck = connectivityMap.get(sensor);
        for (Point2D s : sensorsToCheck) {
            HashSet<Point2D> sensors = connectivityMap.get(s);
            if (sensors.size() - 1 < m * m) {
                doesContribute = true;
                break;
            }
        }
        return doesContribute;
    }

    /**
     * Finds sensors that can increase the k-coverage
     * @param turnedOnSensors a list of sensors that are turned on
     * @param turnedOffSensors a list of sensors that are turned off
     * @return a list of sensors that will be turned on
     */
    private List<Point2D> getSensorsToTurnOn(List<Point2D> turnedOnSensors, List<Point2D> turnedOffSensors) {
        List<Point2D> sensorToTurnOn = new ArrayList<>();
        // Initially we declared the type of turnedOnSensors as Set and the caller of this method would
        // pass this parameter from a map by mapName.keySet() which returns a set that does not support
        // add and addAll operations that we use below. Since we need to add a sensor to turnedOnSensors list
        // every time we turn on a sensor, we declared this parameter as list.
        Set<Point2D> targets = targetToPotentialPositions.keySet();
        for (Point2D target : targets) {
            int k = getCoverageForTarget(turnedOnSensors, target);
            if (k >= wsn.getK()) {
                continue;
            }
            for (Point2D turnedOffSensor : turnedOffSensors) {
                if (doesSensorContributeToCoverage(turnedOffSensor, target)) {
                    sensorToTurnOn.add(turnedOffSensor);
                    turnedOnSensors.add(turnedOffSensor);
                    k++;

                    if (k >= wsn.getK()) {
                        break;
                    }
                }
            }
        }
        return sensorToTurnOn;
    }

    /**
     * Checks if the given sensors can cover the given target
     * @param turnedOffSensor a sensor
     * @param target a target
     * @return true if the given sensor can cover the given target, false otherwise
     */
    private boolean doesSensorContributeToCoverage(Point2D turnedOffSensor, Point2D target) {
        HashSet<Point2D> sensors = targetToPotentialPositions.get(target);
        return sensors.contains(turnedOffSensor);
    }

    private int getCoverageForTarget(List<Point2D> turnedOnSensors, Point2D target) {
        HashSet<Point2D> sensors = targetToPotentialPositions.get(target);
        int k = 0;
        for (Point2D sensor : turnedOnSensors) {
            if (sensors.contains(sensor)) {
                k++;
            }
        }
        return k;
    }

    // TODO: this method will be removed
    private void improveMConnectivity(BitString sequence, Map<Point2D, Integer> turnedOnSensors) {
        Map<Point2D, HashSet<Point2D>> connectivityMap = createConnectivityMap(turnedOnSensors);
        List<Point2D> removedSensors = getSensorsToTurnOff(connectivityMap);
        for (Point2D sensor : removedSensors) {
            sequence.set(turnedOnSensors.get(sensor), false);
        }
    }

    /**
     * The heuristic that increases the k-coverage
     * <p>
     * Calculates each target's coverage (k) value and if the k value is lower
     * than the aimed k, it turns on sensors to increase the k-coverage
     * @param sequence current solution's bitstring sequence
     * @param turnedOnSensors map that holds each turned on sensor and their index in the solution
     * @param turnedOffSensors map that holds each turned off sensor and their index in the solution
     */
    private void improveKCoverage(BitString sequence, Map<Point2D, Integer> turnedOnSensors, Map<Point2D, Integer> turnedOffSensors) {
        List<Point2D> shuffledTurnedOffSensors = new ArrayList<>(turnedOffSensors.keySet());
        // check getSensorsToTurnOn() method to see why we create the list of turnedOnSensors
        List<Point2D> listOfTurnedOnSensors = new ArrayList<>(turnedOnSensors.keySet());
        List<Point2D> sensorsToTurnOn = getSensorsToTurnOn(listOfTurnedOnSensors, shuffledTurnedOffSensors);
        for (Point2D sensor : sensorsToTurnOn) {
            sequence.set(turnedOffSensors.get(sensor), true);
        }
    }

    @Override
    public Solution improve(OptimizationProblem problem, Solution solution) {
        LinkedHashMap<Point2D, Boolean> potentialPositionStateMap = createPotentialPositionStateMap(wsn.getPotentialPositions(), solution);
        Map<Point2D, Integer> turnedOnSensors = getTurnedOnSensors(potentialPositionStateMap);
        Map<Point2D, Integer> turnedOffSensors = getTurnedOffSensors(potentialPositionStateMap);
        BitString improvedSequence = (BitString) solution.getRepresentation();

        improveKCoverage(improvedSequence, turnedOnSensors, turnedOffSensors);

        return new BitStringSolution(improvedSequence, problem.objectiveValue(improvedSequence));
    }

    @Override
    public List<Solution> improveAll(OptimizationProblem problem, List<Solution> solutions) {
        List<Solution> improvedSolutions = new ArrayList<>();
        for (Solution solution : solutions) {
            double probability = random.nextDouble();
            if (probability < IMPROVE_PROBABILITY) {
                improvedSolutions.add(improve(problem, solution));
            }
            else {
                improvedSolutions.add(solution);
            }
        }
        return improvedSolutions;
    }
}
