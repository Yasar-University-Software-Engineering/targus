package com.targus.algorithm.ga;

import com.targus.problem.wsn.WSN;

public class GABuilder {

    private GA ga;

    public GABuilder(GA ga) {
        this.ga = ga;
    }

    public GA build() {
        buildPopulation();
        buildSelectionPolicy();
        buildSurvivalPolicy();
        buildCrossOverOperator();
        buildMutationOperator();
        buildTerminalState();

        return ga;
    }

    public void buildPopulation() {
        WSN model = (WSN) ga.problem.model();
        // TODO: model.getPopulationSize() -> replace with solution size
        ga.setPopulation(new SimplePopulation(model.getPopulationSize()));
    }

    public void buildCrossOverOperator() {
        ga.setCrossOverOperator(new OnePointCrossOver());
    }

    public void buildMutationOperator() {
        ga.setMutationOperator(new OneBitMutation());
    }

    public void buildTerminalState() {
        WSN model = (WSN) ga.problem.model();
        ga.setTerminalState(new IterativeTerminal(model.getGenerationCount()));
    }

    public void buildSelectionPolicy() {
        ga.setSelectionPolicy(new ElitismSelection());
    }

    public void buildSurvivalPolicy() {
        ga.setSurvivalPolicy(new SimpleSurvival());
    }

}
