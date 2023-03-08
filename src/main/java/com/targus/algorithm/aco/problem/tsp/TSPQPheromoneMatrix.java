package com.targus.algorithm.aco.problem.tsp;

import core.algorithm.aco.Ant;
import core.algorithm.aco.PheromoneTrails;
import core.base.OptimizationProblem;
import core.base.Solution;
import core.representation.Permutation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TSPQPheromoneMatrix implements PheromoneTrails {

    double initialValue;
    double pheromone[][];

    double evaporationRatio;
    int colonySize;
    private double upperBound;

    double alpha = 0.9;


    public TSPQPheromoneMatrix(double initialValue, int colonySize, double evaporationRatio) {
        this.initialValue = initialValue;
        this.colonySize = colonySize;
        this.evaporationRatio = evaporationRatio;
    }

    @Override
    public void init(OptimizationProblem problem) {
        TSP tsp = (TSP) problem.model();

        upperBound = calculateUB(tsp);
        pheromone = new double[tsp.getN()][tsp.getN()];
        for (int r = 0; r < tsp.getN(); r++) {
            for (int c = 0; c < tsp.getN(); c++) {
                pheromone[r][c] = initialValue;
            }
        }
    }

    private double calculateUB(TSP tsp) {

        List<Integer> visited= new ArrayList<>();
        List<Integer> nonVisited= IntStream.range(1,tsp.n).boxed().collect(Collectors.toList());
        visited.add(0);
        int current =0;
        double tourLength=0;
        while (!nonVisited.isEmpty())
        {
            int next = IntStream.range(0,nonVisited.size()).boxed().min(Comparator.comparingDouble(x->tsp.distances[0][x])).get();
            visited.add(next);
            tourLength = tsp.distances[current][next];
            nonVisited.removeIf(x->x.equals(next));
            current=next;
        }
        tourLength += tsp.distances[current][0];
        return tourLength;
    }

    @Override
    public void update(OptimizationProblem problem, List<Ant> colony) {
        for (Ant a:colony)
        {
            update(problem,a);
        }
    }

    @Override
    public void update(OptimizationProblem problem, Ant ant) {
        TSP tsp = (TSP) problem.model();
        Solution s = ant.getSolution();
        Permutation tour = (Permutation) s.getRepresentation();
        evaporate(evaporationRatio/colonySize);
        update(tour,s.objectiveValue());

    }

    private synchronized void update(Permutation tour, double tourLength) {
        double delta = upperBound-tourLength;

        for (int i = 0; i < tour.size(); i++) {
            int c1 = tour.get(i);
            int c2 = tour.get((i+1)%tour.size());

            pheromone[c1][c2] = pheromone[c1][c2] + alpha*(delta-pheromone[c1][c2]);
        }
    }


    private synchronized void evaporate(double ratio) {
        for (int r = 0; r < pheromone.length; r++) {
            for (int c = 0; c < pheromone[0].length; c++) {
                pheromone[r][c] *= (1-ratio);
            }
        }
    }


    public double get(int c1, int c2)
    {
        return pheromone[c1][c2];
    }
}
