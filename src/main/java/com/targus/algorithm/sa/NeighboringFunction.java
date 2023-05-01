package com.targus.algorithm.sa;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

public interface NeighboringFunction {
    Solution apply(OptimizationProblem problem, Solution solution);
}
