package com.targus.algorithm.aco.problem.wsn;


import core.base.OptimizationProblem;
import core.problems.wsn.Point2D;
import core.problems.wsn.WSN;
import core.representation.BitString;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.IntStream;

public class WSNHeuristic {

    private final Random random;
    private final WSNData wsnData;
    private final HashMap<Point2D, HashSet<Point2D>> targetToPotentialPositions;

    List<Integer> targets;

    public WSNHeuristic(WSN wsn) {
        random = new SecureRandom();
        this.wsnData = new WSNData(wsn);
        targetToPotentialPositions = wsn.getTargetToPotentialPositionMap();
        targets = new ArrayList<>(IntStream.range(0, wsnData.targetsSize()).boxed().toList());
    }

    public void reset()
    {
        Collections.shuffle(targets);
    }

    /**
     * Maps each potential position with its solutions state. For instance,
     * let potentialPositions = [ a, b, c, d] and solution = 1010. The returned
     * value would be { a:1, b:0, c:1, d:0 }
     * @param potentialPositions
     *      array of potential positions
     * @return LinkedHashMap that holds key (potential position)
     * value (potential position's state) pairs
     */
    private LinkedHashMap<Point2D, Boolean> createPotentialPositionStateMap(Point2D[] potentialPositions, BitString bitString) {
        LinkedHashMap<Point2D, Boolean> map = new LinkedHashMap<>();

        for (int i = 0; i < potentialPositions.length; i++)
            map.put(potentialPositions[i], bitString.get(i));

        return map;
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
            if (k >= wsnData.getK()) {
                continue;
            }
            for (Point2D turnedOffSensor : turnedOffSensors) {
                if (doesSensorContributeToCoverage(turnedOffSensor, target)) {
                    sensorToTurnOn.add(turnedOffSensor);
                    turnedOnSensors.add(turnedOffSensor);
                    k++;

                    if (k >= wsnData.getK()) {
                        break;
                    }
                }
            }
        }
        return sensorToTurnOn;
    }

    public
    int[] getSensorsToTurnOnFast(BitString bs) {
        HashSet<Integer> sensors = bs.ones();
        List<Integer> idles = new ArrayList<>(bs.zeros().stream().toList());
        //Collections.shuffle(idles);

        HashSet<Integer> sensorToTurnOn = new HashSet<>();

        for (Integer t : targets) {
            int k = wsnData.coverage(t,sensors);
            if (k >= wsnData.getK()) {
                continue;
            }
            for (Integer is:idles) {
                if ( wsnData.getCoveringPositions(t).contains(is))
                {
                    sensorToTurnOn.add(is);
                    sensors.add(is);
                    k++;

                    if (k >= wsnData.getK()) {
                        break;
                    }
                }
            }
        }
        if (sensorToTurnOn.isEmpty())
        {
            for (Integer s:sensors)
            {
                if (wsnData.connectivity(s,sensors)>=wsnData.getM())
                    continue;

                int[][] arr =wsnData.getConnectedPositions(s).stream().
                        filter(x->!sensors.contains(x)).
                        map(x->new int[]{x,wsnData.connectivity(x,sensors)}).sorted(Comparator.comparingInt(x->x[1])).
                        toArray(int[][]::new);

                sensorToTurnOn.add(arr[arr.length-1][0]);
            }
        }
        return sensorToTurnOn.stream().mapToInt(x->x).toArray();
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
        int coverage = 0;
        for (Point2D sensor : turnedOnSensors) {
            if (sensors.contains(sensor)) {
                coverage++;
            }
        }
        return coverage;
    }



    public int[] sensorToTurnOn(OptimizationProblem problem, BitString bs) {
        LinkedHashMap<Point2D, Boolean> potentialPositionStateMap = createPotentialPositionStateMap(wsnData.getPotentialPositions(), bs);
        Map<Point2D, Integer> turnedOnSensors = getTurnedOnSensors(potentialPositionStateMap);
        Map<Point2D, Integer> turnedOffSensors = getTurnedOffSensors(potentialPositionStateMap);

        List<Point2D> shuffledTurnedOffSensors = new ArrayList<>(turnedOffSensors.keySet());

        // check getSensorsToTurnOn() method to see why we create the list of turnedOnSensors
        List<Point2D> listOfTurnedOnSensors = new ArrayList<>(turnedOnSensors.keySet());
        List<Point2D> sensorsToTurnOn = getSensorsToTurnOn(listOfTurnedOnSensors, shuffledTurnedOffSensors);
        return sensorsToTurnOn.stream().mapToInt(turnedOffSensors::get).toArray();
    }

    public int[] allSensorsToTurnOn(BitString bs)
    {
        HashSet<Integer> sensors = bs.ones();
        HashSet<Integer> sensorsToTurnOn= new HashSet<>();
        targets.stream().
                filter(t->wsnData.coverage(t,sensors)<wsnData.getK()).
                forEach(t->sensorsToTurnOn.addAll(wsnData.getCoveringPositions(t).stream().
                        filter(x->!sensors.contains(x)).toList()));

        if (sensorsToTurnOn.isEmpty())
        {
            for (Integer s:sensors)
            {
                if (wsnData.connectivity(s,sensors)>=wsnData.getM())
                    continue;

                int[][] arr =wsnData.getConnectedPositions(s).stream().
                        filter(x->!sensors.contains(x)).
                        map(x->new int[]{x,wsnData.connectivity(x,sensors)}).sorted(Comparator.comparingInt(x->x[1])).
                        toArray(int[][]::new);

                sensorsToTurnOn.add(arr[arr.length-1][0]);
            }
        }
        return sensorsToTurnOn.stream().mapToInt(x->x).toArray();
    }


    public int[] sensorsToTurnOn(BitString bs)
    {
        HashSet<Integer> sensors = bs.ones();
        Collections.shuffle(targets);
        for (int t :targets) {
            if (wsnData.coverage(t,sensors)>=wsnData.getK())
                continue;

            int[] arr =wsnData.getCoveringPositions(t).stream().
                    filter(x->!sensors.contains(x)).
                    mapToInt(x->x).
                    toArray();
            if (arr.length>0)
                return arr;
        }
        HashSet<Integer> newSensors= new HashSet<>();
        for (Integer s:sensors)
        {
            if (wsnData.connectivity(s,sensors)>=wsnData.getM())
                continue;

            int[][] arr =wsnData.getConnectedPositions(s).stream().
                    filter(x->!sensors.contains(x)).
                    map(x->new int[]{x,wsnData.connectivity(x,sensors)}).sorted(Comparator.comparingInt(x->x[1])).
                    toArray(int[][]::new);

            newSensors.add(arr[arr.length-1][0]);
        }



        return newSensors.stream().mapToInt(x->x).toArray();
    }


}
