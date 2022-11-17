package com.targus.problem;

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
}
