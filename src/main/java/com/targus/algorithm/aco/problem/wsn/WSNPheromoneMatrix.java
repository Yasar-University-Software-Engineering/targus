package com.targus.algorithm.aco.problem.wsn;

import core.algorithm.aco.Ant;
import core.algorithm.aco.PheromoneTrails;
import core.base.OptimizationProblem;
import core.base.Solution;
import core.problems.wsn.WSN;
import core.representation.BitString;

import java.util.Arrays;

public class WSNPheromoneMatrix  implements PheromoneTrails {

    double initialValue;
    double pheromone[];

    double evaporationRatio;
    int colonySize;

    public WSNPheromoneMatrix(double initialValue, int colonySize, double evaporationRatio) {
        this.initialValue = initialValue;
        this.colonySize = colonySize;
        this.evaporationRatio = evaporationRatio;
    }

    @Override
    public void init(OptimizationProblem problem) {
        WSN wsn = (WSN) problem.model();

        pheromone = new double[wsn.getSolutionSize()];
        Arrays.fill(pheromone, initialValue);
    }


    @Override
    public void update(OptimizationProblem problem, Ant ant) {
        WSN wsn = (WSN) problem.model();
        Solution s = ant.getSolution();
        BitString bs = (BitString) s.getRepresentation();

        evaporate(evaporationRatio/colonySize);
        update(bs,1.0/s.objectiveValue());

    }

    private synchronized void update(BitString assignment, double delta) {
        for (int i = 0; i < assignment.length(); i++) {
            int c1 = assignment.get(i)? 1:0;

            pheromone[i] += c1*delta;
        }
    }

    private synchronized void evaporate(double ratio) {
        for (int r = 0; r < pheromone.length; r++) {
            pheromone[r] *= (1-ratio);
        }
    }


    public double get(int c1)
    {
        return pheromone[c1];
    }
}
