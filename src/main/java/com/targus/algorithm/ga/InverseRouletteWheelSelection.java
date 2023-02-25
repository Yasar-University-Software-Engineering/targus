package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSNMinimumSensorObjective;
import com.targus.utils.Constants;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class InverseRouletteWheelSelection implements SelectionPolicy {

    @Override
    public List<Solution> apply(OptimizationProblem problem, List<Solution> solutions) {
        double maxPenalty = WSNMinimumSensorObjective.WEIGHT_SENSOR
                + WSNMinimumSensorObjective.WEIGHT_K_COV
                + WSNMinimumSensorObjective.WEIGHT_M_COMM;
        double sum = 0;
        double probabilitiesSum = 0;
        for (Solution s : solutions) {
            sum += (maxPenalty - s.objectiveValue());
        }
        double[] probabilitiesArray = new double[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
            double prob = probabilitiesSum + ((maxPenalty - solutions.get(i).objectiveValue()) / sum);
            probabilitiesArray[i] = prob;
            probabilitiesSum = prob;
        }

        Random random = new SecureRandom();
        List<Solution> selected = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SELECTION_COUNT; i++) {
            double prob = random.nextDouble();
            int index = Arrays.binarySearch(probabilitiesArray, prob);
            if (index < 0) {
                index = Math.abs(index + 1);
            }
            selected.add(solutions.get(index));
        }

        return selected;
    }
}
