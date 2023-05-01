package com.targus.algorithm.sa;

public class BoltzmanAF implements AcceptanceFunction {

    @Override
    public double ratio(double deltaF, double temperature) {
        return Math.exp(-deltaF/temperature);
    }

}
