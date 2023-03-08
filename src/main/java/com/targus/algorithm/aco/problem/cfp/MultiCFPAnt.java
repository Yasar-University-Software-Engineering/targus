package com.targus.algorithm.aco.problem.cfp;

import core.SimpleOptimizationProblem;
import core.algorithm.SimpleSolution;
import core.algorithm.aco.BaseAnt;
import core.base.Solution;
import core.problems.coalitionFormation.MCFPCostObjective;
import core.problems.coalitionFormation.MCFPModel;
import core.problems.coalitionFormation.MultiCFPGenerator;
import core.problems.coalitionFormation.MultiCoalition;
import core.representation.IntegerAssignment;
import core.utils.random.RandUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class MultiCFPAnt extends BaseAnt {

    Random rng = new SecureRandom();

    MultiCoalition currentAssignment;



    @Override
    protected void generateSolution() {
        solution = new SimpleSolution(currentAssignment,problem.objectiveValues(currentAssignment));
    }

    @Override
    protected void next() {

        MCFPModel mcfp = (MCFPModel) problem.model();
        MCFPPheromoneMatrix pheromoneMatrix = (MCFPPheromoneMatrix) pheromoneTrails;
        int idleAgents[]= idleAgents();
        double idlePheromones[]= IntStream.of(idleAgents).mapToDouble(x->pheromoneMatrix.get(x,0)).toArray();

        int agent = idleAgents[RandUtils.rouletteSelectInverse(idlePheromones)];

        double taskPheromones[]= IntStream.range(1,mcfp.getTaskCount()+1).mapToDouble(x->pheromoneMatrix.get(agent,x)).toArray();
        /*todo:*/
        int task = RandUtils.rouletteSelect(taskPheromones)+1;

        currentAssignment.reassign(agent,task);


    }


    @Override
    protected boolean solutionConstructed() {

        MCFPModel mcp = (MCFPModel) problem.model();
        boolean res = idleAgents().length ==0 || mcp.isFeasible(currentAssignment);

        return res;
    }

    private int[] idleAgents() {
        int assignment[] = currentAssignment.getCoalitionAssignment().getValues();
        return IntStream.range(0,assignment.length).filter(x->assignment[x]==0).toArray();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void reset() {
        MCFPModel mcp = (MCFPModel) problem.model();

        currentAssignment = new MultiCoalition(new IntegerAssignment(new int[mcp.getAgentCount()]),mcp.getAgents(),mcp.getTaskCount());

    }

    public static void main(String[] args) {

        MCFPModel mcfp = MultiCFPGenerator.generateMultiCFProblem(3,5,3, RandUtils.getDefaultRNG());

        MCFRandomSG sg = new MCFRandomSG();
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(mcfp);
        problem.addObjective(new MCFPCostObjective());
        List<Solution>  solutions = sg.generate(problem,5);

        for (Solution s:solutions)
        {
            System.out.println(s);
        }
    }
}
