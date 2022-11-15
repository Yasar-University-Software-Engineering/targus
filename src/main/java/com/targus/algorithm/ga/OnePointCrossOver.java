package com.targus.algorithm.ga;

import com.targus.algorithm.BitStringSolution;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class OnePointCrossOver implements CrossOverOperator{

    private final Random random;

    public OnePointCrossOver() {
        random = new SecureRandom();
    }

    @Override
    public List<Solution> apply(OptimizationProblem problem, List<Solution> solutions) {
        List<Solution> children = new ArrayList<>();
        int parentSize = solutions.size() % 2 == 1 ? solutions.size() - 1 : solutions.size();
        for (int i = 0; i < parentSize; i+=2) {
            BitString parentOne = (BitString) solutions.get(i);
            BitString parentTwo = (BitString) solutions.get(i+1);

            int crossOverPoint = random.nextInt(parentOne.length());
            BitString childOne = generateChild(parentOne, parentTwo, crossOverPoint);
            BitString childTwo = generateChild(parentTwo, parentOne, crossOverPoint);

            children.add(new BitStringSolution(childOne, problem.objectiveValue(childOne)));
            children.add(new BitStringSolution(childTwo, problem.objectiveValue(childTwo)));
        }

        return children;
    }

    private BitString generateChild(BitString parentOne, BitString parentTwo, int crossOverPoint) {
        BitSet child = new BitSet(parentOne.length());
        for (int j = 0; j < crossOverPoint; j++) {
            child.set(j, parentTwo.get(j));
        }
        for (int j = crossOverPoint; j < parentOne.length() ; j++) {
            child.set(j, parentOne.get(j));
        }
        return new BitString(child);
    }

}
