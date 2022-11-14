package com.targus.base;

import com.targus.problem.ObjectiveType;

public interface OptimizationProblem {
    ProblemModel model();
    double objectiveValue(Representation r);
    ObjectiveType objectiveType();
}