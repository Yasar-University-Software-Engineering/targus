package com.targus.algorithm;

import com.targus.base.Representation;
import com.targus.base.Solution;

public class BitStringSolution implements Solution {

    Representation representation;
    double objectiveValue;

    public BitStringSolution(Representation representation, double objectiveValue) {
        this.representation = representation;
        this.objectiveValue = objectiveValue;
    }

    public void setObjectiveValue(double objectiveValue) {
        this.objectiveValue = objectiveValue;
    }

    @Override
    public Solution clone() {
        return new BitStringSolution(representation.clone(), objectiveValue);
    }

    @Override
    public Representation getRepresentation() {
        return representation;
    }

    @Override
    public double objectiveValue() {
        return objectiveValue;
    }

    // will be deleted
    @Override
    public double[] objectiveValues() {
        return new double[0];
    }

    // will be deleted
    @Override
    public double objectiveValue(int index) {
        return 0;
    }
}
