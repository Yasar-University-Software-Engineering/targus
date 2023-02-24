package com.targus.utils;

import javafx.geometry.Point2D;

public class Constants {

    public static final String DIMENSIONS = "dimensions";
    public static final String TARGETS = "targets";
    public static final String POTENTIAL_POSITIONS = "potentialPositions";
    public static final String COMMUNICATION_RADIUS = "communicationRadius";
    public static final String SENSING_RADIUS = "sensingRadius";
    public static final String M = "m";
    public static final String K = "k";
    public static final String MUTATION_RATE = "mutationRate";
    public static final String GENERATION_COUNT = "generationCount";
    public static final String STANDARD_GA_EXPERIMENT_FILE_NAME = "standard_ga_benchmark_tests.txt";
    public static final String IMPROVED_GA_EXPERIMENT_FILE_NAME = "improved_ga_benchmark_tests.txt";
    public static final String GREEDY_SELECTION_ALGORITHM_EXPERIMENT_FILE_NAME = "greedy_selection_algorithm_benchmark_tests.txt";
    public static final int DEFAULT_POPULATION_COUNT = 60;
    public static final int DEFAULT_COMMUNICATION_RANGE = 100;
    public static final int DEFAULT_SENSING_RANGE = 50;
    public static final double DEFAULT_MUTATION_RATE = 0.03;
    public static final int DEFAULT_M = 1;
    public static final int DEFAULT_K = 2;
    public static final int DEFAULT_ITERATION_COUNT = 10000;
    public static final int DEFAULT_TIME_IN_SECONDS = 10;
    public static final int DEFAULT_GRID_SIZE = 25;
    public static final Point2D DEFAULT_GRID_PADDING = new Point2D(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE);
    public static final double DEFAULT_IMPROVE_PROBABILITY = 0.2;
    public static final int DEFAULT_IMMIGRANT_COUNT = (int) (DEFAULT_POPULATION_COUNT * 0.4);
    public static final int DEFAULT_IMMIGRATION_PERIOD = 100;
    public static final int DEFAULT_SELECTION_COUNT = 8;

}
