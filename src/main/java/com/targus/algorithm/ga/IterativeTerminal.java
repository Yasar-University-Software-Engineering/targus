package com.targus.algorithm.ga;

// TODO: change integers to long
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
    public long getCurrentState() {
        return startIndex;
    }

    @Override
    public long getFinishState() {
        return endIndex;
    }
}
