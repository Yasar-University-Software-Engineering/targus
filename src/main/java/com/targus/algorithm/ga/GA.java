package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class GA {
    OptimizationProblem problem;
    Population population;
    SelectionPolicy selectionPolicy;
    SurvivalPolicy survivalPolicy;
    CrossOverOperator crossOverOperator;
    MutationOperator mutationOperator;
    TerminalState terminalState;

    Log logger;

    public GA(OptimizationProblem problem) {
        this.problem = problem;
        // TODO: refactor this
        logger = new Log("ga.log");
        logger.logger.setLevel(Level.SEVERE);
    }

    public static String printListOfSolutions(List<Solution> solutions) {
        StringBuilder res = new StringBuilder("\n");
        for (Solution s : solutions) {
            res.append(s).append("\n");
        }
        return res.toString();
    }

    public Solution perform() {
        if (!isRunnable()) {
            throw new NullPointerException("There are unassigned members in GA class. Did you call GABuilder class before perform() method?");
        }

        population.init(problem);
        logger.logger.info("After the initialization of the population...");
        logger.logger.info(population.toString());

        population.sortIndividuals();
        logger.logger.info("After the sorting of the population...");
        logger.logger.info(population.toString());

        while (!terminalState.isTerminal()) {
            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());

            logger.logger.info("*Parents*");
            logger.logger.info(printListOfSolutions(parents));

            List<Solution> mating = crossOverOperator.apply(problem, parents);

            logger.logger.info("*Mating*");
            logger.logger.info(printListOfSolutions(mating));

            List<Solution> mutation = mutationOperator.apply(problem, population.getIndividuals());

            logger.logger.info("*Mutation*");
            logger.logger.info(printListOfSolutions(mutation));

            population.addAll(problem, mating);
            population.addAll(problem, mutation);

            logger.logger.info("*Removing duplicates*");
            logger.logger.info(population.toString());

            population.sortIndividuals();

            logger.logger.info("*Removing duplicates : Sorted*");
            logger.logger.info(population.toString());

            List<Solution> survivors = survivalPolicy.apply(problem, population.getIndividuals());

            population.clear();
            population.addAll(problem, survivors);

            logger.logger.info("*Survivors*");
            logger.logger.info(population.toString());

            terminalState.nextState();
        }

        return population.getBestIndividual();
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
