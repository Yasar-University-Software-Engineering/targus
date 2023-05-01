package com.targus.experiment;

import com.targus.base.OptimizationProblem;
import com.targus.experiment.wsn.WSNProblemGenerator;
import com.targus.problem.wsn.WSN;
import com.targus.utils.Constants;
import javafx.geometry.Point2D;

import java.util.*;

public abstract class Experiment {

    /**
     * Creates problem instances for WSN problem instances based on the combinations
     * of input parameters and saves each instance as a JSON file.
     *
     * @param mValues       an array of integers representing the m values, where m is the number of sensors in the WSN
     * @param kValues       an array of integers representing the k values, where k is the number of relay nodes in the WSN
     * @param targetCounts  an array of integers representing the number of target points in the WSN
     * <p>
     * Note: This method generates a mapping of file paths to {@code JsonProblem} instances based on the input parameters
     *       and writes each {@code JsonProblem} instance to a JSON file at the specified file path.
     */
    public void createProblemInstances(int[] mValues, int [] kValues, int[] targetCounts) {
        Map<String, JsonProblem> map = generateFileToProblemMap(mValues, kValues, targetCounts);
        for (Map.Entry<String, JsonProblem> entry : map.entrySet()) {
            String filePath = entry.getKey();
            JsonProblem jsonProblem = entry.getValue();
            FileOperations.writeToJson(jsonProblem, filePath, false);
        }
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

    /**
     *
     * Generates a mapping of file paths to {@code JsonProblem} instances based on the combinations of input parameters.
     * Each {@code JsonProblem} instance represents an optimization problem for a WSN.
     * @param mValues an array of integers representing the m values, where m is the number of sensors in the WSN
     * @param kValues an array of integers representing the k values, where k is the number of relay nodes in the WSN
     * @param targetCounts an array of integers representing the number of target points in the WSN
     * @return a {@code Map<String, JsonProblem>} where the key is a file path and the value is a {@code JsonProblem}
     * instance representing an optimization problem for the given parameters; the map is generated based on the
     * combination of input parameters (mValues, kValues, and targetCounts)
     *
     */
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
            String filePath = Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES + "reference/" + fileName;
            if(FileOperations.doesFileExist(filePath)) {
                System.out.println(filePath + " already exists");
                i++;
                continue;
            }

            OptimizationProblem optimizationProblem = buildProblemInstance(m, k,
                    WSNProblemGenerator.generateRandomPoint2D(dimension, targetCount),
                    WSNProblemGenerator.generateGrid(dimension, Constants.DEFAULT_GRID_PADDING, Constants.DEFAULT_GRID_SIZE), 15);
            WSN wsn = (WSN) optimizationProblem.model();
            JsonProblem jsonProblem = new JsonProblem(wsn, dimension, 15);
            fileToProblemMap.put(filePath, jsonProblem);
            i++;
        }
        return fileToProblemMap;
    }

    public OptimizationProblem buildDefaultProblemInstance(Point2D[] targets, Point2D[] potentialPositions, int terminationValue) {
        WSNProblemGenerator wsnProblemGenerator = WSNProblemGenerator.builder()
                .communicatingRange(Constants.DEFAULT_COMMUNICATION_RANGE)
                .sensingRange(Constants.DEFAULT_SENSING_RANGE)
                .m(Constants.DEFAULT_M)
                .k(Constants.DEFAULT_K)
                .terminationValue(terminationValue)
                .mutationRate(Constants.DEFAULT_MUTATION_RATE)
                .build();

        return wsnProblemGenerator.generateProblemInstance(targets, potentialPositions);
    }


    public OptimizationProblem buildProblemInstance(int m, int k, Point2D[] targets, Point2D[] potentialPositions, int terminationValue) {
        WSNProblemGenerator wsnProblemGenerator = WSNProblemGenerator.builder()
                .communicatingRange(Constants.DEFAULT_COMMUNICATION_RANGE)
                .sensingRange(Constants.DEFAULT_SENSING_RANGE)
                .m(m)
                .k(k)
                .terminationValue(terminationValue)
                .mutationRate(Constants.DEFAULT_MUTATION_RATE)
                .build();

        return wsnProblemGenerator.generateProblemInstance(targets, potentialPositions);
    }

    public void initProblemInstances(int[] mValues, int [] kValues, int[] targetCounts) {
        Map<String, JsonProblem> map = generateFileToProblemMap(mValues, kValues, targetCounts);
        for (Map.Entry<String, JsonProblem> entry : map.entrySet()) {
            String filePath = entry.getKey();
            JsonProblem jsonProblem = entry.getValue();
            FileOperations.writeToJson(jsonProblem, filePath, false);
        }
    }

}
