package com.targus.algorithm.sa;

import com.targus.algorithm.base.SolutionGenerator;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.BitStringSolution;
import com.targus.problem.wsn.WSN;

import java.util.List;

public class RandomSolutionGenerator implements SolutionGenerator {
    @Override
    public Solution generate(OptimizationProblem problem) {
        WSN wsn = (WSN) problem.model();
        int solutionSize = wsn.getSolutionSize();
        return BitStringSolution.generate(problem, solutionSize);
    }

    @Override
    public List<Solution> generate(OptimizationProblem problem, int count) {
        return SolutionGenerator.super.generate(problem, count);
    }
}
