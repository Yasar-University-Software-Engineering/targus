package com.targus.algorithm.ga;

import com.targus.problem.BitStringSolution;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OneBitMutation implements MutationOperator{
    private final Random random;

    public OneBitMutation() {
        random = new SecureRandom();
    }

    @Override
    public List<Solution> apply(OptimizationProblem problem, List<Solution> solutions) {
        List<Solution> newSolutions = new ArrayList<>();
        WSN model = (WSN) problem.model();
        double mutationProbability = model.getMutationRate();
        int solutionSize = model.getSolutionSize();
        for (Solution s : solutions) {
            if (random.nextDouble() < mutationProbability) {
                BitString individual = (BitString) s.getRepresentation();
                individual.flip(random.nextInt(solutionSize));
                BitStringSolution mutatedSolution = new BitStringSolution(individual, problem.objectiveValue(individual));
                newSolutions.add(mutatedSolution);
            }
            else {
                newSolutions.add(s);
            }
        }
        return newSolutions;
    }
}
