package com.targus.algorithm.aco;

import core.base.OptimizationProblem;

import java.util.List;

public interface PheromoneTrails {
    void init(OptimizationProblem problem);

    void update(OptimizationProblem problem, Ant ant);

    default void update(OptimizationProblem problem, List<Ant> colony){
        for (Ant a:colony)
        {
            update(problem,a);
        }
    }
}
