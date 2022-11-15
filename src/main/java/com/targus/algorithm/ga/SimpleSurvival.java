package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleSurvival implements SurvivalPolicy{
    @Override
    public List<Solution> apply(OptimizationProblem problem, List<Solution> solutions) {
        // I should be able to get this information from somewhere else rather than declaring myself
        int MAX_POPULATION_SIZE = 1000;
        int numberOfSurvivors = Math.min(solutions.size(), MAX_POPULATION_SIZE);
        return solutions.stream().limit(numberOfSurvivors).collect(Collectors.toList());
    }
}
