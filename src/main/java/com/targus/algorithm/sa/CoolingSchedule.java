package com.targus.algorithm.sa;

import com.targus.base.OptimizationProblem;

public interface CoolingSchedule {
    void init(OptimizationProblem problem);

    boolean cooledDown();

    void updateTemp(OptimizationProblem problem);

    double temperature();
}
