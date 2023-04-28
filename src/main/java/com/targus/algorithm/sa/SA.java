package com.targus.algorithm.sa;

import com.targus.algorithm.base.AbstractSMetaheuristic;
import com.targus.algorithm.base.SolutionGenerator;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.ObjectiveType;

import java.security.SecureRandom;
import java.util.Random;

public class SA  extends AbstractSMetaheuristic {

    private OptimizationProblem optimizationProblem;
    private CoolingSchedule coolingSchedule;
    private NeighboringFunction neighboring;
    private AcceptanceFunction acceptanceFunction;
    private Random rng = new SecureRandom();
    private int equilibriumIterationCount;

    public SA(OptimizationProblem optimizationProblem, SolutionGenerator generator, CoolingSchedule coolingSchedule, NeighboringFunction neighboring, AcceptanceFunction acceptanceFunction, int equilibriumIterationCount) {
        super(generator);
        this.optimizationProblem = optimizationProblem;
        this.coolingSchedule = coolingSchedule;
        this.neighboring = neighboring;
        this.acceptanceFunction = acceptanceFunction;
        this.equilibriumIterationCount = equilibriumIterationCount;
    }

    @Override
    public Solution perform() {
        return perform(optimizationProblem);
    }

    @Override
    protected void _perform(OptimizationProblem problem) {
        while (!coolingSchedule.cooledDown())
        {
            optimizeAtThisTemp(problem);
            coolingSchedule.updateTemp(problem);
        }
    }

    private void optimizeAtThisTemp(OptimizationProblem problem) {
        for (int i = 0; i < equilibriumIterationCount; i++) {
            Solution neighbor = neighboring.apply(problem, getCurrentSolution());
            double deltaF = neighbor.objectiveValue() - getCurrentSolution().objectiveValue();

            if (problem.objectiveType().betterThan(neighbor.objectiveValue(), getCurrentSolution().objectiveValue()))
                setCurrentSolution(problem, neighbor);
            else{
                deltaF = problem.objectiveType() == ObjectiveType.Maximization ? -deltaF : deltaF;
                double acceptanceRatio = acceptanceFunction.ratio(deltaF, coolingSchedule.temperature());
                if ( rng.nextDouble() < acceptanceRatio)
                    setCurrentSolution(problem, neighbor);
            }
        }
    }

    @Override
    protected void _init(OptimizationProblem problem) {
        coolingSchedule.init(problem);
    }

    @Override
    public String getName() {
        return "Simulated Annealing";
    }
}

