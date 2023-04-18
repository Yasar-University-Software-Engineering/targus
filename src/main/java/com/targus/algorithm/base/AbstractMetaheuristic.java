package com.targus.algorithm.base;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

public abstract class AbstractMetaheuristic implements SingleObjectiveOA {

    protected Solution bestSolution;
    SolutionGenerator solutionGenerator;

    public AbstractMetaheuristic(SolutionGenerator solutionGenerator) {
        this.solutionGenerator = solutionGenerator;
    }

    public Solution perform(OptimizationProblem problem) {
        init(problem);
        _perform(problem);

        return bestSolution;
    }

    protected abstract void init(OptimizationProblem problem);

    protected synchronized void updateBest(OptimizationProblem problem , Solution solution)
    {
        if ( bestSolution ==null || problem.objectiveType().betterThan(solution.objectiveValue(), bestSolution.objectiveValue()))
            bestSolution = solution;
    }

    protected abstract void _perform(OptimizationProblem problem);

}
