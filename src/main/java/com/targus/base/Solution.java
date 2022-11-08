package com.targus.base;

public interface Solution {

    Solution clone();
    Representation getRepresentation();
    double[] objectiveValues();
    double objectiveValue(int index);
    double objectiveValue();
}
