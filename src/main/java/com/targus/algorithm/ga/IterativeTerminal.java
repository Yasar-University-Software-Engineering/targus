package com.targus.algorithm.ga;

public class IterativeTerminal implements TerminalState {

    private int startIndex;
    private int endIndex;

    public IterativeTerminal(int endIndex) {
        this.startIndex = 0;
        this.endIndex = endIndex;
    }

    @Override
    public boolean isTerminal() {
        return startIndex >= endIndex;
    }

    @Override
    public void nextState() {
        startIndex++;
    }

    @Override
    public int getCurrentState() {
        return startIndex;
    }

    @Override
    public int getFinishState() {
        return endIndex;
    }
}
