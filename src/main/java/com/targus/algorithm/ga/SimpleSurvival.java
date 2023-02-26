package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.utils.Constants;


public class SimpleSurvival implements SurvivalPolicy{
    @Override
    public void apply(OptimizationProblem problem, Population population) {
        int removeCount = population.getIndividuals().size() - Constants.DEFAULT_POPULATION_COUNT;
        for (int i = 0; i < removeCount; i++) {
            Solution s = population.getWorstIndividual();
            population.remove(s);
        }
    }
}
