package com.targus.algorithm.ga;

import com.targus.problem.BitStringSolution;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.*;

public class SimplePopulation implements Population {

    private int populationSize;
    private List<Solution> individuals;
    private Random random;
    private static final int POPULATION_SIZE = 100;

    public SimplePopulation(int populationSize) {
        this.populationSize = populationSize;
        individuals = new ArrayList<>();
        random = new SecureRandom();
    }

    @Override
    public void init(OptimizationProblem problem) {
        WSN model = (WSN) problem.model();
        int solutionSize = model.getSolutionSize();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            BitString random = generateRandomIndividual(solutionSize);
            individuals.add(new BitStringSolution(random, problem.objectiveValue(random)));
        }
    }

    private BitString generateRandomIndividual(int individualSize) {
        BitSet bitSet = new BitSet(individualSize);
        for (int i = 0; i < individualSize; i++) {
            bitSet.set(i, random.nextBoolean());
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
    public void remove(int i) {
        individuals.remove(i);
    }

    public void remove(Solution s) {
        individuals.remove(s);
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
        return Collections.max(individuals, Comparator.comparingDouble(Solution::objectiveValue));
    }

    @Override
    public List<Solution> getIndividuals() {
        return individuals;
    }

    @Override
    public void clear() {
        individuals.clear();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("*Population*\n");
        for ( Solution s : individuals) {
            res.append(s).append("\n");
        }
        return res.toString();
    }
}
