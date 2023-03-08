package com.targus.algorithm.aco.problem.wsn;


import core.algorithm.aco.Ant;
import core.algorithm.aco.PheromoneTrails;
import core.base.OptimizationProblem;
import core.base.Solution;
import core.problems.wsn.WSN;
import core.representation.BitString;

import java.util.BitSet;

public class WSNQPheromoneMatrix implements PheromoneTrails {

    double initialValue;
    double pheromone[];

    double evaporationRatio;
    int colonySize;
    private double base;

    double alpha = 0.9;

    public WSNQPheromoneMatrix(double initialValue, int colonySize, double evaporationRatio) {
        this.initialValue = initialValue;
        this.colonySize = colonySize;
        this.evaporationRatio = evaporationRatio;
    }

    @Override
    public void init(OptimizationProblem problem) {
        WSN wsn = (WSN) problem.model();
        base = calculateBase(problem);
        pheromone = new double[wsn.getSolutionSize()];
        for (int r = 0; r < pheromone.length; r++) {
            pheromone[r] = 1.0;
        }
    }

    private double calculateBase( OptimizationProblem problem) {
        WSN wsn = (WSN) problem.model();
        BitString base = new BitString(new BitSet(wsn.getSolutionSize()),wsn.getSolutionSize());
        for (int i = 0; i < wsn.getSolutionSize(); i++) {
            base.set(i,true);
        }
        double baseCost= problem.objectiveValue(base);

        return baseCost;
    }

    @Override
    public void update(OptimizationProblem problem, Ant ant) {
        WSN wsn = (WSN) problem.model();
        Solution s = ant.getSolution();
        BitString bs = (BitString) s.getRepresentation();

        if (s.objectiveValue()<=base) {
            evaporate(evaporationRatio / colonySize);
            update(bs, s.objectiveValue());
        }
    }

    private synchronized void update(BitString assignment, double cost) {
        double delta= (base-cost)/base;
        delta= Math.max(0,delta);
        for (int i = 0; i < assignment.length(); i++) {
            int c1 = assignment.get(i)? 1:0;

            pheromone[i] += c1*(evaporationRatio)*delta;
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
