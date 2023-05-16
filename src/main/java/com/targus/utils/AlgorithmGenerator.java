package com.targus.utils;

import com.targus.algorithm.ga.*;
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

    public GA generateAlgorithm(String algorithmType,
                                String mutationType,
                                double mutationRate,
                                String terminationType,
                                int terminationValue) throws Exception {

        // TODO: we should replace this line with -> SingleObjectiveOA algorithm
        //  I was going to do that but the progress bar fails. Since it takes time
        //  to refactor this, I will leave it for later
        GA ga;

        switch (algorithmType) {
            case Constants.STANDARD_GA ->
                    ga = buildStandardGA(mutationType, mutationRate, terminationType, terminationValue);
            case Constants.IMPROVED_GA ->
                    ga = buildImprovedGA(mutationType, mutationRate, terminationType, terminationValue);
            case Constants.SIMULATED_ANNEALING -> ga = buildSimulatedAnnealing();
            case Constants.GREEDY_ALGORITHM -> ga = buildGreedyAlgorithm();
            default -> {
                throw new Exception("No such algorithm available");
            }
        }

        return ga;
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

    private GA buildGreedyAlgorithm() {
        return null;
    }

    private GA buildSimulatedAnnealing() {
        return null;
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
