package com.targus.algorithm.ga;

public interface TerminalState {

    boolean isTerminal();

    void nextState();

    int getCurrentState();
    int getFinishState();
}
