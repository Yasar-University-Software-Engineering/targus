package com.targus.algorithm.aco.problem.tsp;

import core.algorithm.aco.Ant;
import core.algorithm.aco.PheromoneTrails;
import core.base.OptimizationProblem;
import core.base.Solution;
import core.representation.Permutation;

import java.util.List;

public class TSPPheromoneMatrix implements PheromoneTrails {

    double initialValue;
    double pheromone[][];

    double evaporationRatio;
    int colonySize;

    public TSPPheromoneMatrix(double initialValue, int colonySize, double evaporationRatio) {
        this.initialValue = initialValue;
        this.colonySize = colonySize;
        this.evaporationRatio = evaporationRatio;
    }

    @Override
    public void init(OptimizationProblem problem) {
        TSP tsp = (TSP) problem.model();

        pheromone = new double[tsp.getN()][tsp.getN()];
        for (int r = 0; r < tsp.getN(); r++) {
            for (int c = 0; c < tsp.getN(); c++) {
                pheromone[r][c] = initialValue;
            }
        }
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
        update(tour,1.0/s.objectiveValue());

    }

    private synchronized void update(Permutation tour, double delta) {
        for (int i = 0; i < tour.size(); i++) {
            int c1 = tour.get(i);
            int c2 = tour.get((i+1)%tour.size());

            pheromone[c1][c2] += delta;
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
