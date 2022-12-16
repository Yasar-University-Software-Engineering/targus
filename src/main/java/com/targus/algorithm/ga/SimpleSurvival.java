package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.Collections;
import java.util.Comparator;

public class SimpleSurvival implements SurvivalPolicy{
    @Override
    public void apply(OptimizationProblem problem, Population population) {
        for (int i = 0; i < 20; i++) {
            Solution s = Collections.min(population.getIndividuals(), Comparator.comparingDouble(Solution::objectiveValue));
            population.remove(s);
        }
    }
}
