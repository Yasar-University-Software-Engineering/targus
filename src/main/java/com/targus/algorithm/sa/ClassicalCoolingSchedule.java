package com.targus.algorithm.sa;

import com.targus.base.OptimizationProblem;

public abstract class ClassicalCoolingSchedule implements CoolingSchedule{

    double tMax;
    double tMin;
    double currentTemperature;

    public ClassicalCoolingSchedule(double tMax, double tMin) {
        this.tMax = tMax;
        this.tMin = tMin;
    }

    @Override
    public double temperature() {
        return currentTemperature;
    }

    @Override
    public void init(OptimizationProblem problem) {
        currentTemperature = tMax;
    }

    @Override
    public boolean cooledDown() {
        return currentTemperature<= tMin;
    }
}
