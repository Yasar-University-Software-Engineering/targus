package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GA {

    public final static double MUTATION_PROBABILITY = .2;

    OptimizationProblem problem;
    Population population;
    SelectionPolicy selectionPolicy;
    SurvivalPolicy survivalPolicy;
    CrossOverOperator crossOverOperator;
    MutationOperator mutationOperator;
    TerminalState terminalState;

    public GA(OptimizationProblem problem) {
        this.problem = problem;
    }

    public Solution perform() {
        if (!isRunnable()) {
            throw new NullPointerException("There are unassigned members in GA class. Did you call GABuilder class before perform() method?");
        }
        population.init(problem);

        population.sortIndividuals();

        while (!terminalState.isTerminal()) {
            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());
            List<Solution> mating = crossOverOperator.apply(problem, parents);
            List<Solution> mutation = mutationOperator.apply(problem, population.getIndividuals());

            population.addAll(problem, mating);
            population.addAll(problem, mutation);
            population.sortIndividuals();

            List<Solution> survivors = survivalPolicy.apply(problem, population.getIndividuals());

            population.clear();
            population.addAll(problem, survivors);

            terminalState.nextState();
        }

        return population.getBestIndividual();
    }

    private boolean isRunnable() {
        Field[] fields = this.getClass().getDeclaredFields();

        return Arrays.stream(fields).anyMatch(Objects::isNull);
    }

    public void setProblem(OptimizationProblem problem) {
        this.problem = problem;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public void setSurvivalPolicy(SurvivalPolicy survivalPolicy) {
        this.survivalPolicy = survivalPolicy;
    }

    public void setCrossOverOperator(CrossOverOperator crossOverOperator) {
        this.crossOverOperator = crossOverOperator;
    }

    public void setMutationOperator(MutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    public void setTerminalState(TerminalState terminalState) {
        this.terminalState = terminalState;
    }

}
