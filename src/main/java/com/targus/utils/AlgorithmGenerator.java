package com.targus.utils;

import com.targus.algorithm.GreedySelectionAlgorithm;
import com.targus.algorithm.base.SingleObjectiveOA;
import com.targus.algorithm.ga.*;
import com.targus.algorithm.sa.*;
import com.targus.problem.wsn.WSN;
import com.targus.problem.wsn.WSNOptimizationProblem;
import com.targus.problem.wsn.WSNSolutionImprover;

public class AlgorithmGenerator {
    private final WSNOptimizationProblem wsnOptimizationProblem;
    private final WSN wsn;

    public AlgorithmGenerator(WSNOptimizationProblem wsnOptimizationProblem) {
        this.wsnOptimizationProblem = wsnOptimizationProblem;
        wsn = (WSN) wsnOptimizationProblem.model();
    }

    public SingleObjectiveOA generateAlgorithm(String algorithmType,
                                String mutationType,
                                double mutationRate,
                                String terminationType,
                                int terminationValue) throws Exception {

        SingleObjectiveOA algorithm;

        switch (algorithmType) {
            case Constants.STANDARD_GA ->
                    algorithm = buildStandardGA(mutationType, mutationRate, terminationType, terminationValue);
            case Constants.IMPROVED_GA ->
                    algorithm = buildImprovedGA(mutationType, mutationRate, terminationType, terminationValue);
            case Constants.SIMULATED_ANNEALING ->
                    algorithm = buildSimulatedAnnealing(terminationType, terminationValue);
            case Constants.GREEDY_ALGORITHM -> algorithm = buildGreedyAlgorithm(terminationType, terminationValue);
            default -> {
                throw new Exception("No such algorithm available");
            }
        }

        return algorithm;
    }

    private GA buildStandardGA(String mutationType, double mutationRate, String terminationType, int terminationValue) {
        MutationOperator mutationOperator = buildMutationOperator(mutationType, mutationRate);
        TerminalState terminalState = buildTerminalState(terminationType, terminationValue);

        return StandardGA
                .builder(wsnOptimizationProblem)
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(mutationOperator)
                .setTerminalState(terminalState)
                .build();
    }

    // TODO: replace wsn.getGenerationCount() with time
    public GA buildImprovedGA(String mutationType, double mutationRate, String terminationType, int terminationValue) {
        MutationOperator mutationOperator = buildMutationOperator(mutationType, mutationRate);
        TerminalState terminalState = buildTerminalState(terminationType, terminationValue);

        return ImprovedGA
                .builder(wsnOptimizationProblem)
                .setSolutionImprover(new WSNSolutionImprover(wsn, Constants.DEFAULT_IMPROVE_PROBABILITY))
                .setTerminalState(terminalState)
                .setCrossOverOperator(new OnePointCrossOver())
                .setMutationOperator(mutationOperator)
                .build();
    }

    // TODO: change the terminal state according to the user input
    private SingleObjectiveOA buildGreedyAlgorithm(String terminationType, int terminationValue) {
        TerminalState terminalState = buildTerminalState(terminationType, terminationValue);
        return new GreedySelectionAlgorithm(wsnOptimizationProblem, terminalState);
    }

    // TODO: change the terminalstate according to the user input
    private SingleObjectiveOA buildSimulatedAnnealing(String terminationType, int terminationValue) {
        TerminalState terminalState = buildTerminalState(terminationType, terminationValue);
        return new SA(wsnOptimizationProblem, new RandomSolutionGenerator(), new LinearCooling(100, 0, 0.1), new SimpleNF(), new BoltzmanAF(), 100, terminalState);
    }

    private static TerminalState buildTerminalState(String terminationType, int terminationValue) {
        TerminalState terminalState;

        if (terminationType.equals(Constants.TIME_BASED)) {
            terminalState = new TimeBasedTerminal(terminationValue);
        } else if (terminationType.equals(Constants.ITERATION_BASED)) {
            terminalState = new IterationBasedTerminal(terminationValue);
        } else {
            terminalState = null;
        }

        return terminalState;
    }

    private static MutationOperator buildMutationOperator(String mutationType, Double mutationRate) {
        MutationOperator mutationOperator;

        if (mutationType.equals(Constants.ONE_BIT_MUTATION)) {
            mutationOperator = new OneBitMutation(mutationRate);
        } else if (mutationType.equals(Constants.K_BIT_MUTATION)) {
            mutationOperator = new KBitMutation(mutationRate);
        } else {
            mutationOperator = null;
        }

        return mutationOperator;
    }

}
