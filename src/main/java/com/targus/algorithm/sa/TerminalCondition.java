package com.targus.algorithm.sa;

import com.targus.algorithm.base.OptimizationAlgorithm;
import com.targus.base.OptimizationProblem;

public interface TerminalCondition {
    boolean isSatisfied(OptimizationProblem problem, OptimizationAlgorithm alg);

    void init();

    TerminalCondition clone();
}
