package com.targus.experiment;

import com.targus.algorithm.ga.*;
import com.targus.algorithm.sa.*;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.experiment.wsn.WSNProblemGenerator;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNSolutionImprover;
import com.targus.represent.BitString;
import com.targus.utils.Constants;
import javafx.geometry.Point2D;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.*;

public class BenchmarkTestRunner {

    private int seconds;
    private double GAImprovementRate = Constants.DEFAULT_IMPROVE_PROBABILITY;
    private double GAImmigrationRate = Constants.DEFAULT_IMMIGRATION_RATE;
    private int GAPopulationCount = Constants.DEFAULT_POPULATION_COUNT;
    private String basePath = Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES;
    private String pathForJsonFiles = basePath + "reference/";
    private String pathForResults = basePath + "reference_results/";

    public BenchmarkTestRunner(int seconds) {
        this.seconds = seconds;
    }

    public BenchmarkTestRunner(int seconds, double GAImprovementRate, double GAImmigrationRate, int GAPopulationCount) {
        this(seconds);
        this.GAImprovementRate = GAImprovementRate;
        this.GAImmigrationRate = GAImmigrationRate;
        this.GAPopulationCount = GAPopulationCount;
    }

    private GA buildGA(OptimizationProblem optimizationProblem, WSN wsn, String gaType) {
        GA ga = null;
        switch (gaType) {
            case "Standard":
                ga = StandardGA.builder(optimizationProblem)
                        .setCrossOverOperator(new OnePointCrossOver())
                        .setMutationOperator(new OneBitMutation())
                        .setSurvivalPolicy(new RouletteWheelSurvival())
                        .setSelectionPolicy(new InverseRouletteWheelSelection())
                        .setTerminalState(new TimeBasedTerminal(seconds))
                        .setPopulation(new SimplePopulation(optimizationProblem, Constants.DEFAULT_POPULATION_COUNT))
                        .build();
                break;
            case "Improved":
                ga = ImprovedGA.builder(optimizationProblem)
                        .setSolutionImprover(new WSNSolutionImprover(wsn, GAImprovementRate))
                        .setMigrationCount(GAImmigrationRate)
                        .setCrossOverOperator(new OnePointCrossOver())
                        .setMutationOperator(new KBitMutation())
                        .setSurvivalPolicy(new RouletteWheelSurvival())
                        .setSelectionPolicy(new InverseRouletteWheelSelection())
                        .setTerminalState(new TimeBasedTerminal(seconds))
                        .setPopulation(new SimplePopulation(optimizationProblem, GAPopulationCount))
                        .build();
                break;
            default:
                System.out.println("Invalid class name. Make sure there is no typo in the class name");
                break;
        }

        if (ga == null)
            throw new InvalidParameterException("Type " + gaType + " could not found.");

        return ga;
    }

    private OptimizationProblem buildDefaultProblemInstance(Point2D[] targets, Point2D[] potentialPositions) {
        WSNProblemGenerator wsnProblemGenerator = WSNProblemGenerator.builder()
                .communicatingRange(Constants.DEFAULT_COMMUNICATION_RANGE)
                .sensingRange(Constants.DEFAULT_SENSING_RANGE)
                .m(Constants.DEFAULT_M)
                .k(Constants.DEFAULT_K)
                .terminationValue(seconds)
                .mutationRate(Constants.DEFAULT_MUTATION_RATE)
                .build();

        return wsnProblemGenerator.generateProblemInstance(targets, potentialPositions);
    }

    private OptimizationProblem buildProblemInstance(int m, int k, Point2D[] targets, Point2D[] potentialPositions) {
        WSNProblemGenerator wsnProblemGenerator = WSNProblemGenerator.builder()
                .communicatingRange(Constants.DEFAULT_COMMUNICATION_RANGE)
                .sensingRange(Constants.DEFAULT_SENSING_RANGE)
                .m(m)
                .k(k)
                .terminationValue(seconds)
                .mutationRate(Constants.DEFAULT_MUTATION_RATE)
                .build();

        return wsnProblemGenerator.generateProblemInstance(targets, potentialPositions);
    }

    public void GABenchmarkTest(String filePath, String gaType, int repeat) {
        OptimizationProblem optimizationProblem = WSNProblemGenerator.generateProblemInstanceFromJson(filePath);
        WSN wsn = (WSN) optimizationProblem.model();

        double sum = 0.0;
        double sensorSum = 0;
        GA ga = buildGA(optimizationProblem, wsn, gaType);
        for (int i = 0; i < repeat; i++) {
            Solution solution = ga.perform();
            BitString bitString = (BitString) solution.getRepresentation();
            sensorSum += bitString.ones().size();
            sum += solution.objectiveValue();
        }

        double average = sum / repeat;
        int sensorAverage = (int) (sensorSum / repeat);
        String fileName = "pc_" + GAPopulationCount + "_imprate_" + GAImprovementRate + "_immrate_" + GAImmigrationRate;
        FileOperations.writeToFile(pathForResults + fileName, average + "," + sensorAverage, true);
        System.out.println(gaType + " -- Fitness Value (repeated: " + repeat + "): " + average);
    }

    /**
     * Generates combinations of the given arrays
     *
     * @param mValues m values
     * @param kValues k values
     * @param targetCounts target count values
     * @return 2D array containing an array of length 3. The order goes like this:
     * mValue (index -> 0), kValue (index -> 1), targetCountValue (index -> 2)
     */
    private int[][] generateCombinations(int[] mValues, int [] kValues, int[] targetCounts) {
        int[][] combinations = new int[mValues.length * kValues.length * targetCounts.length][3];

        int index = 0;
        for (Integer mValue : mValues) {
            for (Integer kValue : kValues) {
                for (Integer targetCount : targetCounts) {
                    int[] temp = {mValue, kValue, targetCount};
                    combinations[index++] = temp;
                }
            }
        }
        return combinations;
    }

    private Map<String, JsonProblem> generateFileToProblemMap(int[] mValues, int [] kValues, int[] targetCounts) {
        Map<String, JsonProblem> fileToProblemMap =  new HashMap<>();
        int i = 0;
        int[][] parameters = generateCombinations(mValues, kValues, targetCounts);
        int size = parameters.length;
        while (i < size) {
            int m = parameters[i][0];
            int k = parameters[i][1];
            int targetCount = parameters[i][2];
            Point2D dimension = new Point2D(600, 600);
            String fileName = "m_" + m + "_k_" + k + "_tc_" + targetCount + "_dim_" + (int) dimension.getX() + "_" + (int) dimension.getY() + ".json";
            String filePath = pathForJsonFiles + fileName;
            if(FileOperations.doesFileExist(filePath)) {
                System.out.println(filePath + " already exists");
                i++;
                continue;
            }

            OptimizationProblem optimizationProblem = buildProblemInstance(m, k,
                    WSNProblemGenerator.generateRandomPoint2D(dimension, targetCount),
                    WSNProblemGenerator.generateGrid(dimension, Constants.DEFAULT_GRID_PADDING, Constants.DEFAULT_GRID_SIZE));
            WSN wsn = (WSN) optimizationProblem.model();
            JsonProblem jsonProblem = new JsonProblem(wsn, dimension, seconds);
            fileToProblemMap.put(filePath, jsonProblem);
            i++;
        }
        return fileToProblemMap;
    }

    public void initProblemInstances(int[] mValues, int [] kValues, int[] targetCounts) {
        Map<String, JsonProblem> map = generateFileToProblemMap(mValues, kValues, targetCounts);
        for (Map.Entry<String, JsonProblem> entry : map.entrySet()) {
            String filePath = entry.getKey();
            JsonProblem jsonProblem = entry.getValue();
            FileOperations.writeToJson(jsonProblem, filePath, false);
        }
    }

    private List<String> getJsonFiles() {
        File folder = new File(pathForJsonFiles);
        File[] listOfFiles = folder.listFiles();

        List<String> files = new ArrayList<>();
        for (File file : Objects.requireNonNull(listOfFiles)) {
            if (file.isFile()) {
                files.add(pathForJsonFiles + file.getName());
            }
        }
        return files;
    }

    public void runTests() {
        List<String> jsonFiles = getJsonFiles();
        int[] populationSizes = { 30, 60, 100 };
        double[] improvementRates = { 0.2, 0.4, 0.6, 0.8 };
        double[] immigrantCounts = { 0.1, 0.2, 0.4, 0.5 };
        int repeat = 3;
        for (Integer populationSize : populationSizes) {
            for (String filePath : jsonFiles) {
                resetGAParameters();
                setGAPopulationCount(populationSize);
                GABenchmarkTest(filePath, "Improved", repeat);
            }
        }
        for (Double improvementRate : improvementRates) {
            for (String filePath : jsonFiles) {
                resetGAParameters();
                setGAImprovementRate(improvementRate);
                GABenchmarkTest(filePath, "Improved", repeat);
            }
        }

        for (Double immigrantCount : immigrantCounts) {
            for (String filePath : jsonFiles) {
                resetGAParameters();
                setGAImmigrationRate(immigrantCount);
                GABenchmarkTest(filePath, "Improved", repeat);
            }
        }
    }

    private void resetGAParameters() {
        setGAImprovementRate(Constants.DEFAULT_IMPROVE_PROBABILITY);
        setGAImmigrationRate(Constants.DEFAULT_IMMIGRATION_RATE);
        setGAPopulationCount(Constants.DEFAULT_POPULATION_COUNT);
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setPathForJsonFiles(String pathForJsonFiles) {
        this.pathForJsonFiles = pathForJsonFiles;
    }

    public void setPathForResults(String pathForResults) {
        this.pathForResults = pathForResults;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setGAImprovementRate(double GAImprovementRate) {
        this.GAImprovementRate = GAImprovementRate;
    }

    public void setGAImmigrationRate(double GAImmigrationRate) {
        this.GAImmigrationRate = GAImmigrationRate;
    }

    public void setGAPopulationCount(int GAPopulationCount) {
        this.GAPopulationCount = GAPopulationCount;
    }

    private SA buildSA(OptimizationProblem problem) {
        return new SA(problem, new RandomSolutionGenerator(), new LinearCooling(100, 0, 0.1), new SimpleNF(), new BoltzmanAF(), 100);
    }

    private void runSA() {
        List<String> jsonFiles = getJsonFiles();
        for (String filePath : jsonFiles) {
            OptimizationProblem optimizationProblem = WSNProblemGenerator.generateProblemInstanceFromJson(filePath);
            SA sa = buildSA(optimizationProblem);
            Solution solution = sa.perform();
            System.out.println(solution.objectiveValue());
        }
    }

    public static void main(String[] args) {
        BenchmarkTestRunner runner = new BenchmarkTestRunner(15);
        runner.initProblemInstances(new int[]{1}, new int[]{1}, new int[]{100});
//        runner.runTests();
        runner.runSA();
        runner.runTests();
    }
}
