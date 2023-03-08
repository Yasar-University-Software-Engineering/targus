package com.targus.algorithm.aco;

import core.base.OptimizationProblem;
import core.base.Solution;

public interface Ant {
    void init(OptimizationProblem problem, PheromoneTrails pheromoneTrails);
    Solution constructSolution();
    Solution getSolution();

    void reset();
}
