package com.targus.algorithm.sa;

import com.targus.base.OptimizationProblem;

public class GeometricCooling extends ClassicalCoolingSchedule {

    double delta;

    public GeometricCooling(double tMax, double tMin, double delta) {
        super(tMax,tMin);
        this.delta= delta;
    }

    @Override
    public void updateTemp(OptimizationProblem problem) {
        currentTemperature *= delta;
    }
}
