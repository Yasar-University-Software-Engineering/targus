package com.targus.utils;

import com.targus.algorithm.ga.TerminalState;
import javafx.concurrent.Task;

public class ProgressTask extends Task {

    TerminalState terminalState;

    public ProgressTask(TerminalState terminalState) {
        this.terminalState = terminalState;
    }

    @Override
    protected Object call() {
        long start = terminalState.getCurrentState();
        long end = terminalState.getFinishState();
        while (start < end) {
            updateValue("GA progress: " + start + "/" + end);
            updateProgress(start, end);
            start = terminalState.getCurrentState();
        }
        return null;
    }
}
