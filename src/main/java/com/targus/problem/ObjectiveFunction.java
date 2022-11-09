package com.targus.problem;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;

public interface ObjectiveFunction {
    double value(ProblemModel model, Representation r);
    ObjectiveType type();
}