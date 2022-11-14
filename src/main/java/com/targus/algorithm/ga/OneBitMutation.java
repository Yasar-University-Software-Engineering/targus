package com.targus.algorithm.ga;

import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
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
        for (Solution s : solutions) {
            if (random.nextDouble() < GA.MUTATION_PROBABILITY) {
                BitSet individual = (BitSet) s.getRepresentation();
                individual.flip(random.nextInt(individual.size()));
                newSolutions.add((Solution) individual);
            }
            else {
                newSolutions.add(s);
            }
        }
        return newSolutions;
    }
}
