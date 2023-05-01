package com.targus.algorithm.base;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

public abstract class AbstractSMetaheuristic extends AbstractMetaheuristic {

    private Solution currentSolution;

    public AbstractSMetaheuristic(SolutionGenerator solutionGenerator) {
        super(solutionGenerator);
    }

    protected Solution getCurrentSolution() {
        return currentSolution;
    }


    protected void init(OptimizationProblem problem) {
        if (currentSolution == null)
            setCurrentSolution(problem, solutionGenerator.generate(problem));

        _init(problem);
    }

    protected abstract void _init(OptimizationProblem problem);

    public void setCurrentSolution(OptimizationProblem problem, Solution solution) {
        currentSolution = solution;
        updateBest(problem, solution.clone());
    }



}