package com.targus.algorithm.ga;

public interface TerminalState {

    boolean isTerminal();

    void nextState();
    void reset();

    long getCurrentState();
    long getFinishState();
}
