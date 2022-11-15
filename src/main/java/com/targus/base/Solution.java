package com.targus.base;

public interface Solution {

    Solution clone();
    Representation getRepresentation();
    double objectiveValue();
}
