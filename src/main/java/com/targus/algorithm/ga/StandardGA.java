package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.Experiment;

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

        StringBuilder plotData = new StringBuilder();
        StringBuilder bestSolutionTracker = new StringBuilder();
        StringBuilder bestWorstIndividual = new StringBuilder();

        long iterationCount = 0;
        while(!terminalState.isTerminal()) {
            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());
            List<Solution> mating = crossOverOperator.apply(problem, parents);
            List<Solution> mutated = mutationOperator.apply(problem, mating);

            population.addAll(problem, mutated);
            survivalPolicy.apply(problem, population);
            terminalState.nextState();
            if (updateBestSolution(problem, population.getBestIndividual())) {
                System.out.println("best solution is changed: " + terminalState.getCurrentState());
                bestSolutionTracker.append(bestSolution.objectiveValue()).append(",").append(terminalState.getCurrentState()).append("\n");
            }
            iterationCount++;
            plotData.append(iterationCount).append(",").append(bestSolution.objectiveValue()).append("\n");
            if (iterationCount % 100 == 1) {
                bestWorstIndividual.append(bestSolution.objectiveValue()).append(",").append(population.getWorstIndividual().objectiveValue()).append("\n");
            }
        }

        Experiment.writeToFile("plot_data_std.txt", plotData.toString(), false);
        Experiment.writeToFile("best_solutions_std.txt", bestSolutionTracker.append("\n").toString(), true);
        Experiment.writeToFile("best_worst_individual_std.txt", bestWorstIndividual.toString(), true);
        Experiment.writeToFile("result_std.txt", iterationCount + "," + bestSolution.objectiveValue(), true);
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
