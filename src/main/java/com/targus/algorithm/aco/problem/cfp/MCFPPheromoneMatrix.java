package com.targus.algorithm.aco.problem.cfp;

import core.algorithm.aco.Ant;
import core.algorithm.aco.PheromoneTrails;
import core.base.OptimizationProblem;
import core.base.Solution;
import core.problems.coalitionFormation.MCFPModel;
import core.problems.coalitionFormation.MultiCoalition;
import core.representation.IntegerAssignment;

import java.util.List;

public class MCFPPheromoneMatrix implements PheromoneTrails {

    double initialValue;
    double pheromone[][];

    double evaporationRatio;
    int colonySize;

    public MCFPPheromoneMatrix(double initialValue, int colonySize, double evaporationRatio) {
        this.initialValue = initialValue;
        this.colonySize = colonySize;
        this.evaporationRatio = evaporationRatio;
    }

    @Override
    public void init(OptimizationProblem problem) {
        MCFPModel mcfp = (MCFPModel) problem.model();

        pheromone = new double[mcfp.getAgentCount()][mcfp.getAgentCount()+1];
        for (int r = 0; r < pheromone.length; r++) {
            for (int c = 0; c < pheromone[r].length; c++) {
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
        MCFPModel cfp = (MCFPModel) problem.model();
        Solution s = ant.getSolution();
        MultiCoalition mc = (MultiCoalition) s.getRepresentation();
        IntegerAssignment assignment = mc.getCoalitionAssignment();
        evaporate(evaporationRatio/colonySize);
        update(assignment,1.0/s.objectiveValue());

    }

    private synchronized void update(IntegerAssignment assignment, double delta) {
        for (int i = 0; i < assignment.getLength(); i++) {
            int c1 = assignment.get(i);

            pheromone[i][c1] += delta;
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
