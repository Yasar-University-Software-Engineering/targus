package com.targus.experiment;

import com.targus.algorithm.ga.*;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.wsn.WSNProblemGenerator;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNSolutionImprover;
import com.targus.represent.BitString;

import java.security.InvalidParameterException;

public class GAExperiment extends Experiment{

    TerminalState terminalCondition;
    String pathForResults;

    public GAExperiment(TerminalState terminalCondition, String pathForResults) {
        this.terminalCondition = terminalCondition;
        this.pathForResults = pathForResults;
    }

    public GA buildGA(OptimizationProblem optimizationProblem, WSN wsn, String gaType, int pc, double impRate, double immRate) {
        GA ga = null;
        switch (gaType) {
            case "Standard" -> ga = StandardGA.builder(optimizationProblem)
                    .setCrossOverOperator(new OnePointCrossOver())
                    .setMutationOperator(new OneBitMutation())
                    .setSurvivalPolicy(new RouletteWheelSurvival())
                    .setSelectionPolicy(new InverseRouletteWheelSelection())
                    .setTerminalState(terminalCondition)
                    .setPopulation(new SimplePopulation(optimizationProblem, pc))
                    .build();
            case "Improved" -> ga = ImprovedGA.builder(optimizationProblem)
                    .setSolutionImprover(new WSNSolutionImprover(wsn, impRate))
                    .setMigrationCount(immRate)
                    .setCrossOverOperator(new OnePointCrossOver())
                    .setMutationOperator(new KBitMutation())
                    .setSurvivalPolicy(new RouletteWheelSurvival())
                    .setSelectionPolicy(new InverseRouletteWheelSelection())
                    .setTerminalState(terminalCondition)
                    .setPopulation(new SimplePopulation(optimizationProblem, pc))
                    .build();
            default -> System.out.println("Invalid class name. Make sure there is no typo in the class name");
        }

        if (ga == null)
            throw new InvalidParameterException("Type " + gaType + " could not found.");

        return ga;
    }

    /**
     * Executes a GA benchmark test on a WSN optimization problem, calculates the average fitness value and number of
     * activated sensors, and writes the results to an output file.
     *
     * @param filePath       a {@code String} representing the path to the JSON file containing the WSN optimization problem
     * @param gaType         a {@code String} representing the type of GA to be used for the benchmark test
     * @param outputFileName a {@code String} representing the name of the output file where the results will be written
     * @param repeat         an integer specifying the number of times the GA should be executed for the benchmark test
     * <p>
     * Note: This method reads the WSN optimization problem from the specified JSON file, builds the GA based on the
     *       provided {@code gaType}, and executes the GA {@code repeat} times. The average fitness value and number of
     *       activated sensors across all executions are calculated and written to the output file.
     */
    public void GABenchmarkTest(String filePath, String gaType, String outputFileName, int repeat, int pc, double impRate, double immRate) {
        OptimizationProblem optimizationProblem = WSNProblemGenerator.generateProblemInstanceFromJson(filePath);
        WSN wsn = (WSN) optimizationProblem.model();

        double sum = 0.0;
        double sensorSum = 0;
        GA ga = buildGA(optimizationProblem, wsn, gaType, pc, impRate, immRate);
        for (int i = 0; i < repeat; i++) {
            Solution solution = ga.perform();
            BitString bitString = (BitString) solution.getRepresentation();
            sensorSum += bitString.ones().size();
            sum += solution.objectiveValue();
        }

        double average = sum / repeat;
        int sensorAverage = (int) (sensorSum / repeat);
        FileOperations.writeToFile(pathForResults + outputFileName, average + "," + sensorAverage, true);
        System.out.println(gaType + " -- Fitness Value (repeated: " + repeat + "): " + average);
    }

}
