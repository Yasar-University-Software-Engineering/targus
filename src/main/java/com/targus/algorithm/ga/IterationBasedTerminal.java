package com.targus.algorithm.ga;

public class IterationBasedTerminal implements TerminalState {

    private long startIndex;
    private final long endIndex;

    public IterationBasedTerminal(long endIndex) {
        init();
        this.endIndex = endIndex;
    }

    private void init() {
        this.startIndex = 0;
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
    public void reset() {
        init();
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
