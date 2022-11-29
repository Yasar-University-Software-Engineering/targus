package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.List;
import java.util.stream.Collectors;

public class ElitismSelection implements SelectionPolicy{

    private static final int MINIMUM_ELITE_SIZE = 20;

    @Override
    public List<Solution> apply(OptimizationProblem problem, List<Solution> solutions) {
        double ELITE_PERCENTILE = .10;
        int eliteSize = (int) (solutions.size() * ELITE_PERCENTILE);
        if (eliteSize < MINIMUM_ELITE_SIZE) {
            eliteSize = solutions.size();
        }
        return solutions.stream().limit(eliteSize).collect(Collectors.toList());
    }
}
