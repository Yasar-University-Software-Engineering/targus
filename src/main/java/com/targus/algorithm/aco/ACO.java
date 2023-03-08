package com.targus.algorithm.aco;

import core.SimpleOptimizationProblem;
import core.algorithm.AbstractMetaheuristic;
import core.algorithm.aco.problem.tsp.TSP;
import core.algorithm.aco.problem.tsp.TSPAnt;
import core.algorithm.aco.problem.tsp.TSPMinimumDistanceObjective;
import core.algorithm.aco.problem.tsp.TSPPheromoneMatrix;
import core.algorithm.aco.problem.tsp.tsplib.datamodel.tsp.Tsp;
import core.algorithm.aco.problem.tsp.tsplib.parser.TspLibParser;
import core.algorithm.base.SingleObjectiveOA;
import core.algorithm.localsearch.IterationBasedTC;
import core.algorithm.localsearch.TerminalCondition;
import core.base.OptimizationProblem;
import core.base.Solution;

import java.util.ArrayList;
import java.util.List;

public class ACO  extends AbstractMetaheuristic {

    PheromoneTrails pheromoneTrails;
    List<Ant> colony;
    TerminalCondition terminalCondition;

    public ACO(PheromoneTrails pheromoneTrails, List<Ant> colony,TerminalCondition terminalCondition) {
        super(null);

        this.pheromoneTrails = pheromoneTrails;
        this.colony  = colony;
        this.terminalCondition = terminalCondition;
    }


    @Override
    protected void init(OptimizationProblem problem) {
        pheromoneTrails.init(problem);
        for (Ant a:colony)
            a.init(problem, pheromoneTrails);

        terminalCondition.init();
    }

    @Override
    protected void _perform(OptimizationProblem problem) {
        while (!terminalCondition.isSatisfied(problem,this))
        {
            for (Ant a:colony)
            {
                a.reset();
                Solution s = a.constructSolution();

                updateBest(problem,s);
            }

            pheromoneTrails.update(problem,colony);
            System.out.println(bestSolution);
        }
    }

    @Override
    public String getName() {
        return "ACO";
    }

    public static void main(String[] args) {
        double distances[][] = {
                {0,2,3,4,9,5,4},
                {2,0,5,7,1,2,7},
                {3,5,0,1,3,6,1},
                {4,7,1,0,4,8,2},
                {9,1,3,4,0,4,9},
                {5,2,6,8,4,0,3},
                {4,7,1,2,9,3,0}
        };

        //TSP tsp = new TSP(7, distances);

        Tsp tsp1 = TspLibParser.parseTsp("./data/tsplib/att532.tsp");
        TSP tsp  = TSP.fromTsp(tsp1);

        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(tsp);
        problem.addObjective(new TSPMinimumDistanceObjective());

        List<Ant> colony = new ArrayList<>();
        colony.add(new TSPAnt());

         SingleObjectiveOA alg = new ACO(new TSPPheromoneMatrix(10,colony.size(),0.1),colony,new IterationBasedTC(1000));



        alg.perform(problem);

    }
}
