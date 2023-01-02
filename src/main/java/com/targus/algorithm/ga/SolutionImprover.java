package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.List;

public interface SolutionImprover {

    Solution improve(OptimizationProblem problem, Solution solution);

    List<Solution> improveAll(OptimizationProblem problem, List<Solution> solutions);
}
