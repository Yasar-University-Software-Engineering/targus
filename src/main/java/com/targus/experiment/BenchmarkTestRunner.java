package com.targus.experiment;

import com.targus.algorithm.ga.TimeBasedTerminal;
import com.targus.algorithm.sa.*;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.wsn.WSNProblemGenerator;
import com.targus.represent.BitString;
import com.targus.utils.Constants;

import java.util.*;

public class BenchmarkTestRunner {

    private SA buildSA(OptimizationProblem problem) {
        return new SA(problem, new RandomSolutionGenerator(), new LinearCooling(100, 0, 0.1), new SimpleNF(), new BoltzmanAF(), 100, new TimeBasedTerminal(15));
    }

    private void runSA(int repeat) {
        List<String> jsonFiles = FileOperations.getJsonFiles(Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES + "reference/");
        for (String file : jsonFiles) {
            double sum = 0.0;
            int sensorCount = 0;
            for (int i = 0; i < repeat; i++) {
                OptimizationProblem optimizationProblem = WSNProblemGenerator.generateProblemInstanceFromJson(file);
                SA sa = buildSA(optimizationProblem);
                Solution solution = sa.perform();
                sum += solution.objectiveValue();
                BitString bitString = (BitString) solution.getRepresentation();
                sensorCount += bitString.ones().size();
            }
            System.out.println("SA -- Fitness Value (repeated: " + repeat + "): " + sum / repeat + " SC: " + sensorCount / repeat);
            FileOperations.writeToFile(Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES + "results/sa.txt", sum / repeat + "," + sensorCount / repeat, true);
        }
    }

    public static void main(String[] args) {
        BenchmarkTestRunner runner = new BenchmarkTestRunner();
        GAExperiment gaExperiment = new GAExperiment(new TimeBasedTerminal(15), "./src/main/resources/json/ga");
        // init 5 times
        gaExperiment.initProblemInstances(new int[]{1, 2, 3}, new int[]{1, 2, 3}, new int[]{100, 200, 300});
        int repeat = 10;
//        runner.runSA(repeat);
        List<String> jsonFiles = FileOperations.getJsonFiles(Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES + "reference/");
        for (String file : jsonFiles) {
            gaExperiment.Run(file, "Standard", "", repeat, 50, 0.8, 0.4);
        }

        for (String file : jsonFiles) {
            gaExperiment.Run(file, "Improved", "", repeat, 50, 0.8, 0.4);        }

    }
}
