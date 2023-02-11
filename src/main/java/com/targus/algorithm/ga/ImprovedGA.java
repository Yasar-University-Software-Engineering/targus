package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.SolutionImprover;
import com.targus.problem.wsn.WSNSolutionImprover;

import java.util.*;

public class ImprovedGA extends GA {
    SolutionImprover improver;

    public ImprovedGA(OptimizationProblem problem) {
        super(problem);
        this.improver = new WSNSolutionImprover();
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
            List<Solution> improved = improver.improveAll(problem, mutated);

            population.addAll(problem, improved);
            survivalPolicy.apply(problem, population);
            terminalState.nextState();
        }
        return population.getBestIndividual();
    }

    @Override
    public String getName() {
        return "Improved Genetic Algorithm";
    }

}
