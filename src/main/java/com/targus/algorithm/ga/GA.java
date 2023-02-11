package com.targus.algorithm.ga;

import com.targus.algorithm.base.SingleObjectiveOA;
import com.targus.base.OptimizationProblem;

import java.lang.reflect.Field;
import java.util.*;

public abstract class GA implements SingleObjectiveOA {
    protected OptimizationProblem problem;
    protected Population population;
    protected SelectionPolicy selectionPolicy;
    protected SurvivalPolicy survivalPolicy;
    protected CrossOverOperator crossOverOperator;
    protected MutationOperator mutationOperator;
    protected TerminalState terminalState;


    public GA(OptimizationProblem problem) {
        this.problem = problem;
    }

    public TerminalState getTerminalState() {
        return terminalState;
    }

    protected boolean notRunnable() {
        Field[] fields = this.getClass().getDeclaredFields();

        return Arrays.stream(fields).anyMatch(Objects::isNull);
    }

    public void setProblem(OptimizationProblem problem) {
        this.problem = problem;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public void setSurvivalPolicy(SurvivalPolicy survivalPolicy) {
        this.survivalPolicy = survivalPolicy;
    }

    public void setCrossOverOperator(CrossOverOperator crossOverOperator) {
        this.crossOverOperator = crossOverOperator;
    }

    public void setMutationOperator(MutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    public void setTerminalState(TerminalState terminalState) {
        this.terminalState = terminalState;
    }

}
