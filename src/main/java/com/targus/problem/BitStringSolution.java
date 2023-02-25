package com.targus.problem;

import com.targus.base.OptimizationProblem;
import com.targus.base.Representation;
import com.targus.base.Solution;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class BitStringSolution implements Solution {

    Representation representation;
    double objectiveValue;

    public BitStringSolution(Representation representation, double objectiveValue) {
        this.representation = representation;
        this.objectiveValue = objectiveValue;
    }

    public void setObjectiveValue(double objectiveValue) {
        this.objectiveValue = objectiveValue;
    }

    @Override
    public Solution clone() {
        return new BitStringSolution(representation.clone(), objectiveValue);
    }

    @Override
    public Representation getRepresentation() {
        return representation;
    }

    @Override
    public double objectiveValue() {
        return objectiveValue;
    }

    public static Solution generate(OptimizationProblem problem, int solutionSize) {
        Random random = new SecureRandom();
        BitSet bitSet = new BitSet(solutionSize);
        for (int i = 0; i < solutionSize; i++) {
            bitSet.set(i, random.nextBoolean());
        }
        BitString bitString = new BitString(bitSet);
        return new BitStringSolution(bitString, problem.objectiveValue(bitString));
    }

    public static List<Solution> generate(OptimizationProblem problem, int solutionSize, int count) {
        List<Solution> solutions = new ArrayList<>();
        for (int c = 0; c < count; c++) {
            solutions.add(generate(problem, solutionSize));
        }
        return solutions;
    }

    public String toString() {
        return representation.toString() + " " + objectiveValue;
    }
}
