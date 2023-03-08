package com.targus.algorithm.aco.problem.tsp;

import core.algorithm.SimpleSolution;
import core.algorithm.aco.BaseAnt;
import core.base.Solution;
import core.representation.Permutation;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TSPAnt extends BaseAnt {

    Random rng = new SecureRandom();

    List<Integer> currentTour;



    @Override
    protected void generateSolution() {
        Permutation permutation = new Permutation(currentTour);
        solution = new SimpleSolution(permutation,problem.objectiveValues(permutation));

    }

    @Override
    protected void next() {

        TSP tsp = (TSP) problem.model();
        TSPPheromoneMatrix pheromoneMatrix = (TSPPheromoneMatrix) pheromoneTrails;
        List<Integer> nonVisited = IntStream.range(0,tsp.getN()).boxed().filter((x)->!currentTour.contains(x)).collect(Collectors.toList());

        int c1 = currentTour.get(currentTour.size()-1);

        List<Double> pheromones = nonVisited.stream().map((x)->pheromoneMatrix.get(c1,x)).collect(Collectors.toList());

        int next = randomCity(pheromones);

        currentTour.add(nonVisited.get(next));


    }

    private int randomCity(List<Double> pVals) {

        double sum = pVals.stream().mapToDouble(Double::doubleValue).sum();

        if (sum==0)
            return rng.nextInt(pVals.size());

        double d = rng.nextDouble();
        double dSum =0;
        for (int i = 0; i < pVals.size(); i++) {
            dSum += pVals.get(i)/sum;
            if (d<dSum)
                return i;
        }

        return -1;
    }

    @Override
    protected boolean solutionConstructed() {
        TSP tsp = (TSP) problem.model();
        return currentTour.size()>=tsp.getN();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void reset() {
        TSP tsp = (TSP) problem.model();

        currentTour = new ArrayList<>();

        currentTour.add(rng.nextInt(tsp.getN()));

    }
}
