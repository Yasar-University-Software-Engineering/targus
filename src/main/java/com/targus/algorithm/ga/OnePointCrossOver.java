package com.targus.algorithm.ga;

import com.targus.problem.BitStringSolution;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.*;

public class OnePointCrossOver implements CrossOverOperator{

    private final Random random;

    public OnePointCrossOver() {
        random = new SecureRandom();
    }

    @Override
    public List<Solution> apply(OptimizationProblem problem, List<Solution> solutions) {
        List<Solution> result = new ArrayList<>();
        WSN model = (WSN) problem.model();
        int solutionSize = model.getSolutionSize();
        int parentSize = solutions.size() % 2 == 1 ? solutions.size() - 1 : solutions.size();
        for (int i = 0; i < parentSize; i+=2) {
            Solution pOne = solutions.get(i).clone();
            Solution pTwo = solutions.get(i+1).clone();
            BitString parentOne = (BitString) pOne.getRepresentation();
            BitString parentTwo = (BitString) pTwo.getRepresentation();

            int crossOverPoint = random.nextInt(solutionSize);
            BitString childOne = generateChild(parentOne, parentTwo, crossOverPoint, solutionSize);
            BitString childTwo = generateChild(parentTwo, parentOne, crossOverPoint, solutionSize);

            result.add(new BitStringSolution(childOne, problem.objectiveValue(childOne)));
            result.add(new BitStringSolution(childTwo, problem.objectiveValue(childTwo)));
        }

        return result;
    }

    private BitString generateChild(BitString parentOne, BitString parentTwo, int crossOverPoint, int solutionSize) {
        BitSet child = new BitSet(solutionSize);
        for (int j = 0; j < crossOverPoint; j++) {
            child.set(j, parentTwo.get(j));
        }
        for (int j = crossOverPoint; j < solutionSize ; j++) {
            child.set(j, parentOne.get(j));
        }
        return new BitString(child, solutionSize);
    }

}
