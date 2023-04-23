package com.targus.problem.wsn;

import com.targus.utils.Constants;
import javafx.geometry.Point2D;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WSNMinimumSensorObjectiveTest {

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0",
            "1, 0, 1",
            "10, 0, 10",
            "0, 50, 0",
            "1, 50, 0",
            "10, 50, 7"
    })
    void kCovPenSumTest(int k, double sensingRange, int expected) {
        var wsnMinimumSensorObjective = new WSNMinimumSensorObjective();

        final int m = Constants.DEFAULT_M;
        final double communicationRange = Constants.DEFAULT_COMMUNICATION_RANGE;

        Point2D[] targets = {new Point2D(15, 30)};
        Point2D[] potentialPositions = {
                new Point2D(15, 40),
                new Point2D(15, 50),
                new Point2D(15, 60)};

        var wsn = new WSN(
                targets,
                potentialPositions,
                m,
                k,
                communicationRange,
                sensingRange,
                Constants.DEFAULT_ITERATION_COUNT,
                Constants.DEFAULT_MUTATION_RATE
        );

        HashSet<Integer> indexes = new HashSet<>(Arrays.asList(0, 1, 2));

        assertEquals(expected, wsnMinimumSensorObjective.kCovPenSum(wsn, indexes));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0",
            "1, 0, 3",
            "10, 0, 30",
            "0, 50, 0",
            "1, 50, 0",
            "10, 50, 24"
    })
    void mConnPenSumTest(int m, double communicationRange, int expected) {
        var wsnMinimumSensorObjective = new WSNMinimumSensorObjective();

        final int k = Constants.DEFAULT_K;
        final double sensingRange = Constants.DEFAULT_SENSING_RANGE;

        Point2D[] targets = {new Point2D(15, 30)};
        Point2D[] potentialPositions = {
                new Point2D(15, 40),
                new Point2D(15, 50),
                new Point2D(15, 60)};

        var wsn = new WSN(
                targets,
                potentialPositions,
                m,
                k,
                communicationRange,
                sensingRange,
                Constants.DEFAULT_ITERATION_COUNT,
                Constants.DEFAULT_MUTATION_RATE
        );

        HashSet<Integer> indexes = new HashSet<>(Arrays.asList(0, 1, 2));

        assertEquals(expected, wsnMinimumSensorObjective.mConnPenSum(wsn, indexes));
    }
}