package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;

public interface SurvivalPolicy {

    void apply(OptimizationProblem problem, Population population);
}
