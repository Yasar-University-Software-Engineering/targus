package com.targus.algorithm.aco;

import core.algorithm.AbstractSMetaheuristic;
import core.base.OptimizationProblem;
import core.base.Solution;

public class HybridAnt implements Ant{

    OptimizationProblem problem;
    PheromoneTrails trails;
    AbstractSMetaheuristic sMetaheuristic;
    Ant internalAnt;
    Solution solution;

    @Override
    public void init(OptimizationProblem problem, PheromoneTrails trails) {
        internalAnt.init(problem,trails);
        this.problem =problem;
        this.trails = trails;

    }

    @Override
    public Solution constructSolution() {
        solution = internalAnt.constructSolution();

        sMetaheuristic.setCurrentSolution(problem,solution);

        solution = sMetaheuristic.perform(problem);

        return solution;
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void reset() {
        internalAnt.reset();
    }
}
