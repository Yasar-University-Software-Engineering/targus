package com.targus.algorithm.base;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.ArrayList;
import java.util.List;

public interface SolutionGenerator {
    Solution generate(OptimizationProblem problem);

    default List<Solution> generate(OptimizationProblem problem, int count) {
        List<Solution> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(generate(problem));
        }
        return list;
    }

}
