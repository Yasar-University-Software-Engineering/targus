package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.SolutionImprover;
import com.targus.problem.wsn.WSNSolutionImprover;

import java.util.*;

public class ImprovedGA extends GA {
    SolutionImprover improver;

    public ImprovedGA(GA.Builder builder) {
        super(builder);
        this.improver = new WSNSolutionImprover();
    }

    @Override
    public Solution perform() {
        if (notRunnable()) {
            throw new NullPointerException("There are unassigned members in the class");
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

    public static Builder builder(OptimizationProblem problem) {
        return new Builder(problem);
    }

    public static class Builder extends GA.Builder {

        protected SolutionImprover improver;

        public Builder(OptimizationProblem problem) {
            super(problem);

        }

        public Builder setSolutionImprover(SolutionImprover improver) {
            this.improver = improver;
            return this;
        }

        @Override
        public GA build() {
            basicBuild();
            return new ImprovedGA(this);
        }
    }

    @Override
    public String getName() {
        return "Improved Genetic Algorithm";
    }

}
