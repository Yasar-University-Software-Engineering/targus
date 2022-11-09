package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.List;

public interface CrossOverOperator {
    List<Solution> apply(OptimizationProblem problem, List<Solution> solutions);
}
