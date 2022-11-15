package com.targus.algorithm.ga;

import com.targus.algorithm.BitStringSolution;
import com.targus.base.OptimizationProblem;
import com.targus.base.Representation;
import com.targus.base.Solution;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.*;

public class SimplePopulation implements Population {

    private final int POPULATION_SIZE = 50;  // I do not think I should decide the population size
    private TreeMap<Double, Solution> individuals;  // keeps the solution sorted
    private Random random;

    public SimplePopulation() {
        individuals = new TreeMap<>();
        random = new SecureRandom();
    }

    @Override
    public void init(OptimizationProblem problem) {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            BitString random = generateRandomIndividual();
            BitStringSolution randomSolution = new BitStringSolution(random, problem.objectiveValue(random));
            individuals.put(randomSolution.objectiveValue(), randomSolution);
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
        individuals.put(problem.objectiveValue((Representation) solution), solution);
    }

    @Override
    public void addAll(OptimizationProblem problem, List<Solution> solutions) {
        for (Solution s : solutions) {
            add(problem, s);
        }
    }

    @Override
    public List<Solution> getIndividuals() {
        return new ArrayList<>(individuals.values());
    }

    @Override
    public int getPopulationSize() {
        return individuals.size();
    }

    @Override
    public void clear() {
        individuals.clear();
    }
}
