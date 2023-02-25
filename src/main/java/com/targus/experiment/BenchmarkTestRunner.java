package com.targus.experiment;

import com.targus.algorithm.ga.*;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.wsn.WSNProblemGenerator;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNSolutionImprover;
import com.targus.utils.Constants;
import javafx.geometry.Point2D;

public class BenchmarkTestRunner {

    public static GA buildTimeBasedImprovedGA(OptimizationProblem optimizationProblem, WSN wsn) {
        return ImprovedGA.builder(optimizationProblem)
                .setSolutionImprover(new WSNSolutionImprover())
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(new OneBitMutation())
                .setSurvivalPolicy(new RouletteWheelSurvival())
                .setTerminalState(new TimeBasedTerminal(wsn.getGenerationCount()))
                .setPopulation(new SimplePopulation(optimizationProblem, Constants.DEFAULT_POPULATION_COUNT))
                .build();
    }

    public static GA buildIterationBasedImprovedGA(OptimizationProblem optimizationProblem, WSN wsn) {
        return ImprovedGA.builder(optimizationProblem)
                .setSolutionImprover(new WSNSolutionImprover())
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(new OneBitMutation())
                .setSurvivalPolicy(new RouletteWheelSurvival())
                .setTerminalState(new IterativeTerminal(wsn.getGenerationCount()))
                .setPopulation(new SimplePopulation(optimizationProblem, Constants.DEFAULT_POPULATION_COUNT))
                .build();
    }

    public static GA buildTimeBasedStandardGA(OptimizationProblem optimizationProblem, WSN wsn) {
        return ImprovedGA.builder(optimizationProblem)
                .setSolutionImprover(new WSNSolutionImprover())
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(new OneBitMutation())
                .setSurvivalPolicy(new RouletteWheelSurvival())
                .setTerminalState(new TimeBasedTerminal(wsn.getGenerationCount()))
                .setPopulation(new SimplePopulation(optimizationProblem, Constants.DEFAULT_POPULATION_COUNT))
                .build();
    }

    public static GA buildIterationBasedStandardGA(OptimizationProblem optimizationProblem, WSN wsn) {
        return ImprovedGA.builder(optimizationProblem)
                .setSolutionImprover(new WSNSolutionImprover())
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(new OneBitMutation())
                .setSurvivalPolicy(new RouletteWheelSurvival())
                .setTerminalState(new IterativeTerminal(wsn.getGenerationCount()))
                .setPopulation(new SimplePopulation(optimizationProblem, Constants.DEFAULT_POPULATION_COUNT))
                .build();
    }

    public static void benchmarkTestOne() {
        WSNProblemGenerator wsnProblemGenerator = WSNProblemGenerator.builder()
                .communicatingRange(Constants.DEFAULT_COMMUNICATION_RANGE)
                .sensingRange(Constants.DEFAULT_SENSING_RANGE)
                .m(Constants.DEFAULT_M)
                .k(Constants.DEFAULT_K)
                .terminationValue(300)
                .mutationRate(Constants.DEFAULT_MUTATION_RATE)
                .build();
        Point2D dimensions = new Point2D(600, 600);
        OptimizationProblem optimizationProblem = wsnProblemGenerator.generateProblemInstance(
                WSNProblemGenerator.generateGrid(dimensions, Constants.DEFAULT_GRID_PADDING, Constants.DEFAULT_GRID_SIZE),
                WSNProblemGenerator.generateRandomPoint2D(dimensions, 300));
        WSN wsn = (WSN) optimizationProblem.model();

        GA ga = buildTimeBasedStandardGA(optimizationProblem, wsn);
        Solution solution = ga.perform();
        System.out.println("Standard GA: " + solution.objectiveValue());

        ga = buildTimeBasedImprovedGA(optimizationProblem, wsn);
        solution = ga.perform();
        System.out.println("Improved GA: " + solution.objectiveValue());
    }

    public static void benchmarkTestOnlyImproved() {
        WSNProblemGenerator wsnProblemGenerator = WSNProblemGenerator.builder()
                .communicatingRange(Constants.DEFAULT_COMMUNICATION_RANGE)
                .sensingRange(Constants.DEFAULT_SENSING_RANGE)
                .m(Constants.DEFAULT_M)
                .k(Constants.DEFAULT_K)
                .terminationValue(180)
                .mutationRate(Constants.DEFAULT_MUTATION_RATE)
                .build();
        Point2D dimensions = new Point2D(400, 400);
        OptimizationProblem optimizationProblem = wsnProblemGenerator.generateProblemInstance(
                WSNProblemGenerator.generateGrid(dimensions, Constants.DEFAULT_GRID_PADDING, Constants.DEFAULT_GRID_SIZE),
                WSNProblemGenerator.generateRandomPoint2D(dimensions, 200));
        WSN wsn = (WSN) optimizationProblem.model();
        GA ga = buildTimeBasedImprovedGA(optimizationProblem, wsn);
        Solution solution = ga.perform();
        System.out.println("Improved GA: " + solution.objectiveValue());
    }
}
