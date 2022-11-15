package com.targus.algorithm.ga;

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
        // ga.setPopulation(new SimplePopulation());
    }

    public void buildCrossOverOperator() {
        ga.setCrossOverOperator(new OnePointCrossOver());
    }

    public void buildMutationOperator() {
        ga.setMutationOperator(new OneBitMutation());
    }

    public void buildTerminalState() {
        ga.setTerminalState(new IterativeTerminal());
    }

    public void buildSelectionPolicy() {
        ga.setSelectionPolicy(new ElitismSelection());
    }

    public void buildSurvivalPolicy() {
        ga.setSurvivalPolicy(new SimpleSurvival());
    }

}
