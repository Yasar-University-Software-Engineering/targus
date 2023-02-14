package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.Experiment;
import com.targus.problem.wsn.SolutionImprover;
import com.targus.problem.wsn.WSNSolutionImprover;
import com.targus.utils.Constants;

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
        StringBuilder diagnostic = new StringBuilder(Experiment.getProblemInformation(problem));
        population.init(problem);

        while(!terminalState.isTerminal()) {
            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());
            List<Solution> mating = crossOverOperator.apply(problem, parents);
            List<Solution> mutated = mutationOperator.apply(problem, mating);
            List<Solution> improved = improver.improveAll(problem, mutated);

            population.addAll(problem, improved);
            survivalPolicy.apply(problem, population);
            terminalState.nextState();
            if (updateBestSolution(problem, population.getBestIndividual())) {
                diagnostic.append(Experiment.getBenchmarkTestInformation(bestSolution, terminalState));
                System.out.println("the best solution is changed");
                System.out.println("time is : " + terminalState.getCurrentState());
            }
        }

        Experiment.writeToFile(Constants.IMPROVED_GA_EXPERIMENT_FILE_NAME, diagnostic.toString());
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
            return new ImprovedGA(basicBuild());
        }
    }

    @Override
    public String getName() {
        return "Improved Genetic Algorithm";
    }

}
