package com.targus.algorithm.ga;

import com.targus.problem.BitStringSolution;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.*;

public class SimplePopulation implements Population {

    private int populationSize;
    private List<Solution> individuals;
    private Random random;
    private static final int POPULATION_SIZE = 10;

    public SimplePopulation(int populationSize) {
        this.populationSize = populationSize;
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
        // TODO: replace populationSize with individualSize
        BitSet bitSet = new BitSet(populationSize);
        for (int i = 0; i < populationSize; i++) {
            // set random bits
            bitSet.set(i, random.nextBoolean());
        }
        return new BitString(bitSet);
    }

    // TODO: refactor below.
    /*
    *   Search for a better data structure to hold unique elements: Set, Map etc
    *   Get rid of adding duplicate elements if possible
    * */
    @Override
    public void add(OptimizationProblem problem, Solution solution) {
        boolean found = false;
        for (Solution s : individuals) {
            if (s.getRepresentation().equals(solution.getRepresentation()))
                found = true;
        }

        if (!found) {
            individuals.add(solution);
        }
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
        return individuals.get(individuals.size() - 1);  // return the last element since the list will always be sorted
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
