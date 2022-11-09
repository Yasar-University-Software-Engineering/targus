package com.targus.algorithm;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.List;

public interface Population {

    void init(OptimizationProblem problem);

    void add(OptimizationProblem problem, List<Solution> solutions);

    List<Solution> getIndividuals();

    void clear();

}
