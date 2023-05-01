package com.targus.experiment;

import com.targus.utils.Constants;

import java.util.*;

public class GAParameterTuning {

    private double improvementRate = Constants.DEFAULT_IMPROVE_PROBABILITY;
    private double immigrationRate = Constants.DEFAULT_IMMIGRATION_RATE;
    private int populationCount = Constants.DEFAULT_POPULATION_COUNT;

    private String pathForJsonFiles = Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES + "reference/";
    private String pathForResults;
    private GAExperiment gaExperiment;

    public GAParameterTuning(GAExperiment gaExperiment) {
        this.pathForResults = Constants.DEFAULT_BASE_PATH_FOR_JSON_FILES + "param_tuning";
        this.gaExperiment = gaExperiment;
    }

    public GAParameterTuning(double GAImprovementRate, double GAImmigrationRate, int GAPopulationCount) {
        this.improvementRate = GAImprovementRate;
        this.immigrationRate = GAImmigrationRate;
        this.populationCount = GAPopulationCount;
    }

    public void parameterTuningTests(String path) {
        List<String> jsonFiles = FileOperations.getJsonFiles(path);
        int[] populationSizes = { 30, 60, 100 };
        double[] improvementRates = { 0.2, 0.4, 0.6, 0.8 };
        double[] immigrantCounts = { 0.1, 0.2, 0.4, 0.5 };
        int repeat = 3;
        for (Integer populationSize : populationSizes) {
            for (String filePath : jsonFiles) {
                resetGAParameters();
                setPopulationCount(populationSize);
                gaExperiment.Run(filePath, "Improved", pathForResults, repeat, populationCount, improvementRate, immigrationRate);
            }
        }
        for (Double improvementRate : improvementRates) {
            for (String filePath : jsonFiles) {
                resetGAParameters();
                setImprovementRate(improvementRate);
                gaExperiment.Run(filePath, "Improved", pathForResults, repeat, populationCount, improvementRate, immigrationRate);
            }
        }

        for (Double immigrantCount : immigrantCounts) {
            for (String filePath : jsonFiles) {
                resetGAParameters();
                setImmigrationRate(immigrantCount);
                gaExperiment.Run(filePath, "Improved", pathForResults, repeat, populationCount, improvementRate, immigrationRate);
            }
        }
    }

    private void resetGAParameters() {
        setImprovementRate(Constants.DEFAULT_IMPROVE_PROBABILITY);
        setImmigrationRate(Constants.DEFAULT_IMMIGRATION_RATE);
        setPopulationCount(Constants.DEFAULT_POPULATION_COUNT);
    }

    public void setImprovementRate(double improvementRate) {
        this.improvementRate = improvementRate;
    }

    public void setImmigrationRate(double immigrationRate) {
        this.immigrationRate = immigrationRate;
    }

    public void setPopulationCount(int populationCount) {
        this.populationCount = populationCount;
    }

}
