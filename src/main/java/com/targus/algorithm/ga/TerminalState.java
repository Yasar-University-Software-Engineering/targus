package com.targus.algorithm.ga;

public interface TerminalState {

    boolean isTerminal();

    void nextState();

    long getCurrentState();
    long getFinishState();
}
