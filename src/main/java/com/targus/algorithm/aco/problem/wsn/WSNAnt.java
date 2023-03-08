package com.targus.algorithm.aco.problem.wsn;

import core.algorithm.SimpleSolution;
import core.algorithm.aco.BaseAnt;
import core.algorithm.aco.PheromoneTrails;
import core.base.OptimizationProblem;
import core.base.Solution;
import core.problems.wsn.WSN;
import core.representation.BitString;
import core.utils.random.RandUtils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;

public class WSNAnt extends BaseAnt {

    double heuristicSelectionRate = 0.6;

    Random rng = new SecureRandom();

    BitString currentAssignment;
    WSNHeuristic heuristic;
    private boolean _solutionConstructed;


    @Override
    public void init(OptimizationProblem problem, PheromoneTrails pheromoneTrails) {
        super.init(problem, pheromoneTrails);
        WSN wsn = (WSN) problem.model();
        heuristic = new WSNHeuristic(wsn);
    }

    @Override
    protected void generateSolution() {
        solution = new SimpleSolution(currentAssignment,problem.objectiveValue(currentAssignment));
    }

    @Override
    protected void next() {

        WSN wsn = (WSN) problem.model();
        WSNQPheromoneMatrix pheromoneMatrix = (WSNQPheromoneMatrix) pheromoneTrails;

        /*todo: Here sensors to be turned on can be get from Improve heuristic*/

        int[] sensorsToTurnOn = heuristic.getSensorsToTurnOnFast(currentAssignment);

        //int[] sensorsToTurnOn = heuristic.sensorsToTurnOn(currentAssignment);


        if (sensorsToTurnOn.length==0) {
            _solutionConstructed = true;
            return;
        }
        double[] sensorPheromones = IntStream.of(sensorsToTurnOn).mapToDouble(pheromoneMatrix::get).toArray();

        int sensor;
        if (rng.nextDouble()<heuristicSelectionRate)
            sensor = sensorsToTurnOn[RandUtils.rouletteSelectInverse(sensorPheromones)];
        else sensor = sensorsToTurnOn[rng.nextInt(sensorsToTurnOn.length)];

        currentAssignment.flip(sensor);

    }


    @Override
    protected boolean solutionConstructed() {

        WSN wsn = (WSN) problem.model();
        int[] idleSensors = idleSensors();
        boolean res = _solutionConstructed || idleSensors.length ==0 ||  wsn.isFeasible(currentAssignment);

        return res;
    }

    private int[] idleSensors() {
        HashSet<Integer> ones = currentAssignment.ones ();
        return IntStream.range(0,currentAssignment.length()).filter(x->!ones.contains(x)).toArray();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void reset() {
        WSN wsn = (WSN) problem.model();
        currentAssignment = new BitString(wsn.getSolutionSize());
        _solutionConstructed=false;
        heuristic.reset();
    }

    public static void main(String[] args) {


    }
}
