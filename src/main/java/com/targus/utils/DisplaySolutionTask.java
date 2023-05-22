package com.targus.utils;

import com.targus.algorithm.ga.GA;
import com.targus.algorithm.ga.TerminalState;
import com.targus.ui.Mediator;
import javafx.concurrent.Task;

public class DisplaySolutionTask extends Task {
    GA ga;
    TerminalState terminalState;
    Mediator mediator;

    public DisplaySolutionTask(GA ga, Mediator mediator, TerminalState terminalState) {
        this.ga = ga;
        this.mediator = mediator;
        this.terminalState = terminalState;
    }

    @Override
    protected Object call() {
        long start = terminalState.getCurrentState();
        long end = terminalState.getFinishState();
        while (start < end) {
            mediator.displaySolution(ga.getBestSolution());
            start = terminalState.getCurrentState();
        }
        return null;
    }
}
