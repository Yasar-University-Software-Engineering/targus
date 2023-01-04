package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.lang.reflect.Field;
import java.util.*;

public class GA {
    OptimizationProblem problem;
    Population population;
    SelectionPolicy selectionPolicy;
    SurvivalPolicy survivalPolicy;
    CrossOverOperator crossOverOperator;
    MutationOperator mutationOperator;
    TerminalState terminalState;
    SolutionImprover improver;


    public GA(OptimizationProblem problem) {
        this.problem = problem;
        this.improver = new WSNSolutionImprover();
    }


    public Solution perform() {
        if (!isRunnable()) {
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

    public TerminalState getTerminalState() {
        return terminalState;
    }

    private boolean isRunnable() {
        Field[] fields = this.getClass().getDeclaredFields();

        return Arrays.stream(fields).noneMatch(Objects::isNull);
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
