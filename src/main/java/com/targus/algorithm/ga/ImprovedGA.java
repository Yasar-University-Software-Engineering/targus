package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.Experiment;
import com.targus.problem.BitStringSolution;
import com.targus.problem.wsn.SolutionImprover;
import com.targus.problem.wsn.WSN;
import com.targus.represent.BitString;
import com.targus.utils.Constants;

import java.security.SecureRandom;
import java.util.*;

public class ImprovedGA extends GA {
    SolutionImprover improver;

    public ImprovedGA(Builder builder) {
        super(builder);
        this.improver = builder.improver;
    }

    @Override
    public Solution perform() {
        if (notRunnable()) {
            throw new NullPointerException("There are unassigned members in the class");
        }
        population.init(problem);

        long iterationCount = 0;
        WSN wsn = (WSN) problem.model();
        int solutionSize = wsn.getSolutionSize();
        while(!terminalState.isTerminal()) {
            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());
            List<Solution> mating = crossOverOperator.apply(problem, parents);
            List<Solution> mutated = mutationOperator.apply(problem, mating);
            List<Solution> improved = improver.improveAll(problem, mutated);

            population.addAll(problem, improved);
            if (iterationCount % Constants.DEFAULT_IMMIGRATION_PERIOD == 0) {
                population.addAll(problem, BitStringSolution.generate(problem, solutionSize, Constants.DEFAULT_IMMIGRANT_COUNT));
            }
            survivalPolicy.apply(problem, population);
            terminalState.nextState();
            if (updateBestSolution(problem, population.getBestIndividual())) {
                System.out.println("the best solution is changed");
                System.out.println("time is : " + terminalState.getCurrentState());
            }
            iterationCount++;
        }

        Experiment.writeToFile(Constants.IMPROVED_GA_EXPERIMENT_FILE_NAME,
                Experiment.getProblemInformation(problem) + bestSolution + " " + terminalState.getCurrentState());
        return bestSolution;
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
            return new ImprovedGA((Builder) basicBuild());
        }
    }

    @Override
    public String getName() {
        return "Improved Genetic Algorithm";
    }

}
