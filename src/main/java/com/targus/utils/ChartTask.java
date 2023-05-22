package com.targus.utils;

import com.targus.algorithm.ga.GA;
import com.targus.algorithm.ga.TerminalState;
import com.targus.ui.Mediator;
import com.targus.ui.controllers.FitnessGraphController;
import javafx.concurrent.Task;

public class ChartTask extends Task {
    GA ga;
    TerminalState terminalState;
    Mediator mediator;

    public ChartTask(GA ga, Mediator mediator) {
        this.ga = ga;
        this.mediator = mediator;
        terminalState = ga.getTerminalState();
    }

    @Override
    protected Object call() {
        FitnessGraphController fitnessGraphController = mediator.getFitnessGraphController();
        long start = terminalState.getCurrentState();
        long end = terminalState.getFinishState();
        while (start < end) {
            if (ga.getBestSolution() != null) {
                long finalStart = start;
                fitnessGraphController.updateFitness(finalStart, ga.getBestSolution().objectiveValue());
            }
            start = terminalState.getCurrentState();
        }
        return null;
    }
}
