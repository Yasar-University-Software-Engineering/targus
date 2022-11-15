package com.targus.algorithm.ga;

import com.targus.problem.BitStringSolution;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.*;

public class SimplePopulation implements Population {

    private final int POPULATION_SIZE = 50;  // I do not think I should decide the population size
    private List<Solution> individuals;  // keeps the solution sorted
    private Random random;

    public SimplePopulation() {
        individuals = new ArrayList<>();
        random = new SecureRandom();
    }

    @Override
    public void init(OptimizationProblem problem) {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            BitString random = generateRandomIndividual();
            individuals.add(new BitStringSolution(random, problem.objectiveValue(random)));
        }
    }

    private BitString generateRandomIndividual() {
        BitSet bitSet = new BitSet(POPULATION_SIZE);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            // set random bits
            bitSet.set(random.nextInt(2));
        }
        return new BitString(bitSet);
    }

    @Override
    public void add(OptimizationProblem problem, Solution solution) {
        individuals.add(solution);
    }

    @Override
    public void addAll(OptimizationProblem problem, List<Solution> solutions) {
        for (Solution s : solutions) {
            add(problem, s);
        }
    }

    @Override
    public void sortIndividuals() {
        individuals.sort(Comparator.comparingDouble(Solution::objectiveValue));
    }

    @Override
    public Solution getBestIndividual() {
        if (individuals.isEmpty()) {
            return null;
        }
        return individuals.get(0);  // return 0th element since the list will always be sorted
    }

    @Override
    public List<Solution> getIndividuals() {
        return individuals;
    }

    @Override
    public void clear() {
        individuals.clear();
    }
}
