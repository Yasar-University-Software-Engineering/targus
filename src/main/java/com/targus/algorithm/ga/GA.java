package com.targus.algorithm.ga;

import com.targus.algorithm.Population;
import com.targus.algorithm.TerminalState;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.util.List;

public class GA {

    OptimizationProblem problem;
    Population population;
    SelectionPolicy selectionPolicy;
    SurvivalPolicy survivalPolicy;
    CrossOverOperator crossOverOperator;
    MutationOperator mutationOperator;
    TerminalState terminalState;

    public GA(OptimizationProblem problem, Population population, SelectionPolicy selectionPolicy, SurvivalPolicy survivalPolicy, CrossOverOperator crossOverOperator, MutationOperator mutationOperator, TerminalState terminalState) {
        this.problem = problem;
        this.population = population;
        this.selectionPolicy = selectionPolicy;
        this.survivalPolicy = survivalPolicy;
        this.crossOverOperator = crossOverOperator;
        this.mutationOperator = mutationOperator;
        this.terminalState = terminalState;
    }

    public void perform() {
        population.init(problem);

        // evaluate fitness value for each individual

        while (!terminalState.isTerminal()) {
            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());
            List<Solution> mating = crossOverOperator.apply(problem, parents);
            List<Solution> mutation = mutationOperator.apply(problem, population.getIndividuals());

            population.add(problem, mating);
            population.add(problem, mutation);

            // evaluate fitness value for each individual

            List<Solution> survivers = survivalPolicy.apply(problem, population.getIndividuals());

            population.clear();
            population.add(problem, survivers);

            terminalState.nextState();
        }
    }

}
