package com.targus.algorithm.aco;

import core.base.OptimizationProblem;
import core.base.Solution;

public abstract class BaseAnt implements Ant{


    protected OptimizationProblem problem;
    protected PheromoneTrails pheromoneTrails;
    protected Solution solution;


    @Override
    public void init(OptimizationProblem problem, PheromoneTrails pheromoneTrails) {

        this.problem = problem;
        this.pheromoneTrails = pheromoneTrails;

    }

    @Override
    public Solution constructSolution() {

        while (!solutionConstructed())
        {
            next();
        }
        //System.out.println("GS-B");
        generateSolution();
        //System.out.println("GS-A");
        return solution;
    }

    protected abstract void generateSolution() ;


    protected abstract void next();

    protected abstract boolean solutionConstructed();


}
