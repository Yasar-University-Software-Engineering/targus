package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.List;

public interface Population {

    void init(OptimizationProblem problem);

    void add(OptimizationProblem problem, Solution solution);

    void addAll(OptimizationProblem problem, List<Solution> solutions);

    void remove(Solution o);

    Solution getBestIndividual();

    Solution getWorstIndividual();

    List<Solution> getIndividuals();

    void clear();

}
