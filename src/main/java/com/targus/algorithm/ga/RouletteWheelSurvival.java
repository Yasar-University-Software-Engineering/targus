package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.utils.Constants;

import java.security.SecureRandom;
import java.util.*;

public class RouletteWheelSurvival implements SurvivalPolicy {

    @Override
    public void apply(OptimizationProblem problem, Population population) {
        List<Solution> solutions = population.getIndividuals();
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
        List<Solution> toRemove = new ArrayList<>();
        int removeCount = population.getIndividuals().size() - Constants.DEFAULT_POPULATION_COUNT;
        for (int i = 0; i < removeCount; i++) {
            double prob = random.nextDouble();
            int index = Arrays.binarySearch(probabilitiesArray, prob);
            if (index < 0) {
                index = Math.abs(index + 1);
            }
            toRemove.add(solutions.get(index));
        }

        for (Solution s : toRemove) {
            population.remove(s);
        }
    }
}
