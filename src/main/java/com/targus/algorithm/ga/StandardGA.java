package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.Experiment;
import com.targus.utils.Constants;

import java.util.List;

public class StandardGA extends GA {

    public StandardGA(Builder builder) {
        super(builder);
    }

    @Override
    public Solution perform() {
        if (notRunnable()) {
            throw new NullPointerException("There are unassigned members in the class.");
        }
        population.init(problem);

        while(!terminalState.isTerminal()) {
            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());
            List<Solution> mating = crossOverOperator.apply(problem, parents);
            List<Solution> mutated = mutationOperator.apply(problem, mating);

            population.addAll(problem, mutated);
            survivalPolicy.apply(problem, population);
            terminalState.nextState();
            if (updateBestSolution(problem, population.getBestIndividual())) {
                System.out.println("the best solution is changed");
                System.out.println("time is : " + terminalState.getCurrentState());
            }
        }

        Experiment.writeToFile(Constants.STANDARD_GA_EXPERIMENT_FILE_NAME,
                Experiment.getProblemInformation(problem) + bestSolution.objectiveValue() + " " + terminalState.getCurrentState());
        return bestSolution;
    }


    public static Builder builder(OptimizationProblem problem) {
        return new Builder(problem);
    }

    public static class Builder extends GA.Builder {

        public Builder(OptimizationProblem problem) {
            super(problem);
        }

        @Override
        public GA build() {
            return new StandardGA((Builder) basicBuild());
        }
    }

    @Override
    public String getName() {
        return "Standard Genetic Algorithm";
    }

}
