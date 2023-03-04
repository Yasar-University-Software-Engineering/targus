package com.targus.experiment;

import com.targus.algorithm.ga.*;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.wsn.WSNProblemGenerator;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNSolutionImprover;
import com.targus.utils.Constants;
import javafx.geometry.Point2D;

import java.security.InvalidParameterException;

public class BenchmarkTestRunner {

    public static GA buildGA(OptimizationProblem optimizationProblem, WSN wsn, String gaType, MutationOperator mutationOperator, TerminalState terminalState) {
        GA ga = null;
        switch (gaType) {
            case "Standard":
                ga = StandardGA.builder(optimizationProblem)
                        .setCrossOverOperator(new OnePointCrossOver())
                        .setMutationOperator(mutationOperator)
                        .setSurvivalPolicy(new RouletteWheelSurvival())
                        .setTerminalState(terminalState)
                        .setPopulation(new SimplePopulation(optimizationProblem, Constants.DEFAULT_POPULATION_COUNT))
                        .build();
                break;
            case "Improved":
                ga = ImprovedGA.builder(optimizationProblem)
                        .setSolutionImprover(new WSNSolutionImprover(wsn, Constants.DEFAULT_IMPROVE_PROBABILITY))
                        .setCrossOverOperator(new OnePointCrossOver())
                        .setMutationOperator(mutationOperator)
                        .setSurvivalPolicy(new RouletteWheelSurvival())
                        .setTerminalState(terminalState)
                        .setPopulation(new SimplePopulation(optimizationProblem, Constants.DEFAULT_POPULATION_COUNT))
                        .build();
                break;
            default:
                System.out.println("Invalid class name. Make sure there is no typo in the class name");
                break;
        }

        if (ga == null) {
            throw new InvalidParameterException("Type " + gaType + " could not found.");
        }

        return ga;
    }

    public static OptimizationProblem buildRandomProblemInstanceWithDefault(Point2D[] targets, Point2D[] potentialPositions, int terminalValue) {
        WSNProblemGenerator wsnProblemGenerator = WSNProblemGenerator.builder()
                .communicatingRange(Constants.DEFAULT_COMMUNICATION_RANGE)
                .sensingRange(Constants.DEFAULT_SENSING_RANGE)
                .m(Constants.DEFAULT_M)
                .k(Constants.DEFAULT_K)
                .terminationValue(terminalValue)
                .mutationRate(Constants.DEFAULT_MUTATION_RATE)
                .build();

        return wsnProblemGenerator.generateProblemInstance(targets, potentialPositions);
    }

    public static void GABenchmarkTest(String filePath, String gaType, MutationOperator mutationOperator, TerminalState terminalState, int repeat) {
        OptimizationProblem optimizationProblem = WSNProblemGenerator.generateProblemInstanceFromJson(filePath);
        WSN wsn = (WSN) optimizationProblem.model();

        double sum = 0.0;
        GA ga = buildGA(optimizationProblem, wsn, gaType, mutationOperator, terminalState);
        for (int i = 0; i < repeat; i++) {
            Solution solution = ga.perform();
            sum += solution.objectiveValue();
        }

        double average = sum / repeat;
        System.out.println(gaType + " -- Fitness Value (repeated: " + repeat + "): " + average);
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\doguk\\Desktop\\targus\\src\\main\\resources\\json\\big_instance.json";
        int terminalValue = 20;
        int repeat = 5;
        GABenchmarkTest(filePath, "Standard", new OneBitMutation(), new TimeBasedTerminal(terminalValue), repeat);
        GABenchmarkTest(filePath, "Improved", new KBitMutation(), new TimeBasedTerminal(terminalValue), repeat);
    }
}
