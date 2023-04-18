package com.targus.algorithm.sa;

import com.targus.algorithm.base.OptimizationAlgorithm;
import com.targus.base.OptimizationProblem;

public class TimeBasedTC implements TerminalCondition{
    long endTime;
    long timeLimit;

    public TimeBasedTC(long timeLimit) {
        this.timeLimit = timeLimit;
        init();
    }

    @Override
    public boolean isSatisfied(OptimizationProblem problem, OptimizationAlgorithm alg) {
        long currentTime = System.currentTimeMillis();
        return currentTime >= endTime;
    }

    @Override
    public void init() {
        endTime = System.currentTimeMillis()+timeLimit;
    }

    @Override
    public TerminalCondition clone() {
        return new TimeBasedTC(timeLimit);
    }
}

