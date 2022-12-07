package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.utils.CustomLogger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GA {
    OptimizationProblem problem;
    Population population;
    SelectionPolicy selectionPolicy;
    SurvivalPolicy survivalPolicy;
    CrossOverOperator crossOverOperator;
    MutationOperator mutationOperator;
    TerminalState terminalState;

    Logger logger;

    public GA(OptimizationProblem problem) {
        this.problem = problem;
        try {
            CustomLogger customLogger = new CustomLogger("java.com.targus.algorithm.ga.Ga", "GA.log", true);
            logger = customLogger.getLogger();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.setLevel(Level.SEVERE);
    }

    private static String printListOfSolutions(List<Solution> solutions) {
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
        logger.info("After the initialization of the population...");
        logger.info(population.toString());

        population.sortIndividuals();
        logger.info("After the sorting of the population...");
        logger.info(population.toString());

        Solution bestSolution = population.getBestIndividual();
        Solution temp = population.getBestIndividual();
        System.out.println("The best individual is " + bestSolution);

        while (!terminalState.isTerminal()) {
            if (bestSolution.objectiveValue() < population.getBestIndividual().objectiveValue()) {
                bestSolution = population.getBestIndividual();
                System.out.println("The best individual is change to: " + bestSolution);
            }

            List<Solution> parents = selectionPolicy.apply(problem, population.getIndividuals());

            logger.info("*Parents*");
            logger.info(printListOfSolutions(parents));

            List<Solution> mating = crossOverOperator.apply(problem, parents);

            logger.info("*Mating*");
            logger.info(printListOfSolutions(mating));

            List<Solution> mutation = mutationOperator.apply(problem, mating);

            logger.info("*Mutation*");
            logger.info(printListOfSolutions(mutation));

            population.addAll(problem, mutation);

            logger.info("*Removing duplicates*");
            logger.info(population.toString());

            population.sortIndividuals();

            logger.info("*Removing duplicates : Sorted*");
            logger.info(population.toString());

            List<Solution> survivors = survivalPolicy.apply(problem, population.getIndividuals());

            population.clear();
            population.addAll(problem, survivors);

            logger.info("*Survivors*");
            logger.info(population.toString());

            bestSolution = population.getBestIndividual();
            terminalState.nextState();
        }
        System.out.println("The execution of GA is finished: " + bestSolution);
        return bestSolution;
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
