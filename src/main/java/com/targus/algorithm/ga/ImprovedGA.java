package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.Experiment;
import com.targus.problem.BitStringSolution;
import com.targus.problem.wsn.SolutionImprover;
import com.targus.problem.wsn.WSN;
import com.targus.utils.Constants;

import java.util.*;

public class ImprovedGA extends GA {
    SolutionImprover improver;
    double immigrationRate;

    public ImprovedGA(Builder builder) {
        super(builder);
        this.improver = builder.improver;
        this.immigrationRate =  builder.immigrationRate;
    }

    @Override
    public Solution perform() {
        if (notRunnable()) {
            throw new NullPointerException("There are unassigned members in the class");
        }
        population.init(problem);

        StringBuilder plotData = new StringBuilder();
        StringBuilder timeBasedPlotData = new StringBuilder();
        StringBuilder bestSolutionTracker = new StringBuilder();
        StringBuilder bestWorstIndividual = new StringBuilder();

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
                population.addAll(problem, BitStringSolution.generate(problem, solutionSize, (int) (population.getIndividuals().size() * immigrationRate)));
            }
            survivalPolicy.apply(problem, population);
            terminalState.nextState();
            if (updateBestSolution(problem, population.getBestIndividual())) {
                bestSolutionTracker.append(bestSolution.objectiveValue()).append(",").append(terminalState.getCurrentState()).append("\n");
            }
            iterationCount++;
            plotData.append(iterationCount).append(",").append(bestSolution.objectiveValue()).append("\n");
            timeBasedPlotData.append(terminalState.getCurrentState()).append(",").append(bestSolution.objectiveValue()).append("\n");
            if (iterationCount % 100 == 1) {
                bestWorstIndividual.append(bestSolution.objectiveValue()).append(",").append(population.getWorstIndividual().objectiveValue()).append("\n");
            }
        }

        Experiment.writeToFile("plot_data_imp.txt", plotData.toString(), false);
        Experiment.writeToFile("time_based_plot_data_imp.txt", timeBasedPlotData.toString(), false);
        Experiment.writeToFile("best_solutions_imp.txt", bestSolutionTracker.append("\n\n").toString(), true);
        Experiment.writeToFile("best_worst_individual_imp.txt", bestWorstIndividual.toString(), true);
        Experiment.writeToFile("result_imp.txt", iterationCount + "," + bestSolution.objectiveValue(), true);

        terminalState.reset();
        population.clear();
        return bestSolution;
    }

    public static Builder builder(OptimizationProblem problem) {
        return new Builder(problem);
    }

    public static class Builder extends GA.Builder {

        protected SolutionImprover improver;
        protected double immigrationRate;

        public Builder(OptimizationProblem problem) {
            super(problem);

        }

        public Builder setSolutionImprover(SolutionImprover improver) {
            this.improver = improver;
            return this;
        }

        public Builder setMigrationCount(double immigrationRate) {
            this.immigrationRate = immigrationRate;
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
