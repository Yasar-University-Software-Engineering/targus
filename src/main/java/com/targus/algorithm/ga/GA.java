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

    public GA(OptimizationProblem problem) {
        this.problem = problem;
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

//    public static void main(String[] args) {
//        GA ga = new GA(null);
//        GABuilder builder = new GABuilder(ga);
//
//        ga = builder.build();
//        ga.perform();
//    }

}
