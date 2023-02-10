package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.List;

public class StandardGA extends GA {

    public StandardGA(OptimizationProblem problem) {
        super(problem);
    }

    @Override
    public Solution perform() {
        if (notRunnable()) {
            throw new NullPointerException("There are unassigned members in GA class. Did you call GABuilder class before perform() method?");
        }

        population.init(problem);

        while(!terminalState.isTerminal()) {
            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());
            List<Solution> mating = crossOverOperator.apply(problem, parents);
            List<Solution> mutated = mutationOperator.apply(problem, mating);

            population.addAll(problem, mutated);
            survivalPolicy.apply(problem, population);
            terminalState.nextState();
        }
        return population.getBestIndividual();
    }

    @Override
    public String getName() {
        return "Standard Genetic Algorithm";
    }

}
