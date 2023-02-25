package com.targus.algorithm.ga;

import com.targus.problem.BitStringSolution;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.ObjectiveType;
import com.targus.problem.wsn.WSN;
import com.targus.represent.BitString;

import java.security.SecureRandom;
import java.util.*;

public class SimplePopulation implements Population {

    private OptimizationProblem problem;
    private final int populationSize;
    private List<Solution> individuals;
    private final Random random;

    public SimplePopulation(OptimizationProblem problem, int populationSize) {
        this.problem = problem;
        this.populationSize = populationSize;
        individuals = new ArrayList<>();
        random = new SecureRandom();
    }

    @Override
    public void init(OptimizationProblem problem) {
        WSN model = (WSN) problem.model();
        int solutionSize = model.getSolutionSize();
        individuals.addAll(BitStringSolution.generate(problem, solutionSize, populationSize));
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

    public void remove(Solution s) {
        individuals.remove(s);
    }

    @Override
    public Solution getBestIndividual() {
        if (individuals.isEmpty()) {
            return null;
        }

        return problem.objectiveType() == ObjectiveType.Maximization
                ? Collections.max(individuals, Comparator.comparingDouble(Solution::objectiveValue))
                : Collections.min(individuals, Comparator.comparingDouble(Solution::objectiveValue));
    }

    @Override
    public Solution getWorstIndividual() {
        if (individuals.isEmpty()) {
            return null;
        }

        return problem.objectiveType() == ObjectiveType.Maximization
                ? Collections.min(individuals, Comparator.comparingDouble(Solution::objectiveValue))
                : Collections.max(individuals, Comparator.comparingDouble(Solution::objectiveValue));
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
