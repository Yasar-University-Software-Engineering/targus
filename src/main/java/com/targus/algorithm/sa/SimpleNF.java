package com.targus.algorithm.sa;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.BitStringSolution;
import com.targus.problem.wsn.WSN;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.Random;

public class SimpleNF implements NeighboringFunction {

    private final Random random;

    public SimpleNF() {
        random = new SecureRandom();
    }

    @Override
    public Solution apply(OptimizationProblem problem, Solution solution) {
        WSN model = (WSN) problem.model();
        double mutationProbability = model.getMutationRate();
        int solutionSize = model.getSolutionSize();
        if (random.nextDouble() < mutationProbability) {
            BitString individual = (BitString) solution.clone().getRepresentation();
            individual.flip(random.nextInt(solutionSize));
            return new BitStringSolution(individual, problem.objectiveValue(individual));
        }
        return solution;
    }
}
