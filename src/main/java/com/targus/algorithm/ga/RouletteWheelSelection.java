package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RouletteWheelSelection implements SelectionPolicy {

    @Override
    public List<Solution> apply(OptimizationProblem problem, List<Solution> solutions) {
        double sum = 0;
        double probabilitiesSum = 0;
        for (Solution s : solutions) {
            sum += s.objectiveValue();
        }
        double[] probabilitiesArray = new double[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
            double prob = probabilitiesSum + (solutions.get(i).objectiveValue() / sum);
            probabilitiesArray[i] = prob;
            probabilitiesSum = prob;
        }

        Random random = new SecureRandom();
        List<Solution> selected = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
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
