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
        // ga.setPopulation(new Population);
    }

    public void buildCrossOverOperator() {
        ga.setCrossOverOperator(new OnePointCrossOver());
    }

    public void buildMutationOperator() {
        ga.setMutationOperator(new OneBitMutation());
    }

    public void buildTerminalState() {
        // ga.setTerminalState(new TerminalState());
    }

    public void buildSelectionPolicy() {
        // ga.setSelectionPolicy(new SelectionPolicy());
    }

    public void buildSurvivalPolicy() {
        // ga.setSurvivalPolicy(new SurvivalPolicy());
    }

}
