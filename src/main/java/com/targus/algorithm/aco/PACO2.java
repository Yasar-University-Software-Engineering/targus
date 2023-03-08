package com.targus.algorithm.aco;

import core.SimpleOptimizationProblem;
import core.algorithm.AbstractMetaheuristic;
import core.algorithm.aco.problem.cfp.MCFPPheromoneMatrix;
import core.algorithm.aco.problem.cfp.MultiCFPAnt;
import core.algorithm.aco.problem.tsp.TSP;
import core.algorithm.aco.problem.tsp.TSPAnt;
import core.algorithm.aco.problem.tsp.TSPMinimumDistanceObjective;
import core.algorithm.aco.problem.tsp.TSPPheromoneMatrix;
import core.algorithm.aco.problem.tsp.tsplib.datamodel.tour.Tour;
import core.algorithm.aco.problem.tsp.tsplib.datamodel.tsp.Tsp;
import core.algorithm.aco.problem.tsp.tsplib.parser.TspLibParser;
import core.algorithm.aco.problem.wsn.WSNAnt;
import core.algorithm.aco.problem.wsn.WSNQPheromoneMatrix;
import core.algorithm.base.SingleObjectiveOA;
import core.algorithm.localsearch.IterationBasedTC;
import core.algorithm.localsearch.TerminalCondition;
import core.base.OptimizationProblem;
import core.base.Solution;
import core.problems.coalitionFormation.MCFPCostObjective;
import core.problems.coalitionFormation.MCFPModel;
import core.problems.coalitionFormation.MultiCFPGenerator;
import core.problems.wsn.WSNOptimizationProblem;
import core.problems.wsn.WSNProblemGenerator;
import core.representation.Permutation;
import core.utils.random.RandUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class PACO2  extends AbstractMetaheuristic {

    private static final int THREAD_COUNT = 10;
    private static final int TOUR_COUNT = 100;
    PheromoneTrails pheromoneTrails;
    List<Ant> colony;
    TerminalCondition terminalCondition;

    ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
    private long solutionCounter;


    public PACO2(PheromoneTrails pheromoneTrails, List<Ant> colony,TerminalCondition terminalCondition) {
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
        solutionCounter=0;
    }

    int antJob(OptimizationProblem problem,Ant ant, TerminalCondition terminalCondition)
    {
        long id = Thread.currentThread().getId();
        long iteration=0;
        while (!terminalCondition.isSatisfied(problem,this)){

            ant.reset();

            Solution s = ant.constructSolution();

            incrementSolutionCount();
            updateBest(problem,s);

            pheromoneTrails.update(problem,ant);
            if (solutionCounter%1000==0)
                System.out.println("SC:"+solutionCounter);
        }
        System.out.println("Done: " + Thread.currentThread().getId());
        return 0;
    }

    private synchronized void incrementSolutionCount() {
        solutionCounter++;
    }

    @Override
    protected void _perform(OptimizationProblem problem) {
        if (colony.size() > 1)
            multiThreadedPerform(problem);
        else antJob(problem,colony.get(0),terminalCondition);
        System.out.println(bestSolution);
    }

    private void multiThreadedPerform(OptimizationProblem problem) {
        List<Callable<Integer>> todo = new ArrayList<Callable<Integer>>(colony.size());

        for (Ant a: colony) {
            todo.add(()-> antJob(problem,a,terminalCondition.clone()));
        }

        try {
            pool.invokeAll(todo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "PACO-2";
    }

    public static void main(String[] args) {
        demoWSN();
    }

    private static void demoCFP() {
        MCFPModel mcfp = MultiCFPGenerator.generateMultiCFProblem(20,40,5, RandUtils.getDefaultRNG());
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(mcfp);
        problem.addObjective(new MCFPCostObjective());

        List<Ant> colony = new ArrayList<>();
        colony.add(new MultiCFPAnt());

        SingleObjectiveOA alg = new PACO2(new MCFPPheromoneMatrix(10,colony.size(),0.1),colony,new IterationBasedTC(1000000));

        alg.perform(problem);
    }

    private static void demoTSP() {
        double distances[][] = {
                {0,2,3,4,9,5,4},
                {2,0,5,7,1,2,7},
                {3,5,0,1,3,6,1},
                {4,7,1,0,4,8,2},
                {9,1,3,4,0,4,9},
                {5,2,6,8,4,0,3},
                {4,7,1,2,9,3,0}
        };

        Tsp tsp1 = TspLibParser.parseTsp("./data/tsplib/att48.tsp");
        TSP tsp  = TSP.fromTsp(tsp1);

        Tour tour = TspLibParser.parseTour("./data/tsplib/att48.opt.tour");
        System.out.println("OPTIMAL: "+ Arrays.toString(tour.getTour().get(0)));

        int nodes[] = IntStream.of(tour.getTour().get(0)).map(x->x-1).toArray();
        Permutation p = new Permutation(nodes);


        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(tsp);
        problem.addObjective(new TSPMinimumDistanceObjective());

        System.out.println("Cost: "+problem.objectiveValue(p));

        List<Ant> colony = new ArrayList<>();
        colony.add(new TSPAnt());
        colony.add(new TSPAnt());
        colony.add(new TSPAnt());
        colony.add(new TSPAnt());
        colony.add(new TSPAnt());
        colony.add(new TSPAnt());
        colony.add(new TSPAnt());
        colony.add(new TSPAnt());


        SingleObjectiveOA alg = new PACO2(new TSPPheromoneMatrix(10,colony.size(),0.1),colony,new IterationBasedTC(1000000));

        alg.perform(problem);
    }

    private static void demoWSN() {
        WSNOptimizationProblem problem = WSNProblemGenerator.generateProblemInstanceFromJson("./data/wsn/big_instance.json"); // todo: call Generator here


        List<Ant> colony = new ArrayList<>();
        colony.add(new WSNAnt());
       /* colony.add(new WSNAnt());
        colony.add(new WSNAnt());
        colony.add(new WSNAnt());*/


        SingleObjectiveOA alg = new PACO2(new WSNQPheromoneMatrix(10,colony.size(),0.1),colony,new IterationBasedTC(1000000));

        alg.perform(problem);
    }
}
