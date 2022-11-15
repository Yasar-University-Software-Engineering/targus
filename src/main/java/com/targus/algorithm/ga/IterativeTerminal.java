package com.targus.algorithm.ga;

import com.targus.algorithm.TerminalState;

public class IterativeTerminal implements TerminalState {

    private int startIndex;
    private int endIndex;

    public IterativeTerminal() {
        startIndex = 0;
        endIndex = 1000;
    }

    @Override
    public boolean isTerminal() {
        return startIndex >= endIndex;
    }

    @Override
    public void nextState() {
        startIndex++;
    }
}
