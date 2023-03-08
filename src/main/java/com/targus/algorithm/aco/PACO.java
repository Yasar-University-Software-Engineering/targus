package com.targus.algorithm.aco;

import core.algorithm.AbstractMetaheuristic;
import core.algorithm.localsearch.TerminalCondition;
import core.base.OptimizationProblem;
import core.base.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PACO  extends AbstractMetaheuristic {

    private static final int THREAD_COUNT = 10;
    PheromoneTrails pheromoneTrails;
    List<Ant> colony;
    TerminalCondition terminalCondition;

    ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

    public PACO() {
        super(null);
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
            List<Callable<Solution>> todo = new ArrayList<Callable<Solution>>(colony.size());

            for (Ant a: colony) {
                todo.add(a::constructSolution);
            }
            List<Future<Solution>> answers= null;
            try {
                 answers = pool.invokeAll(todo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < answers.size(); i++) {
                try {
                    if (answers.get(i).isDone()) {
                        Solution s = answers.get(i).get();
                        updateBest(problem, s);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            pheromoneTrails.update(problem,colony);
        }
    }

    @Override
    public String getName() {
        return "ACO";
    }
}
