package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import com.targus.problem.ObjectiveFunction;
import com.targus.problem.ObjectiveType;

public class WSNMinimumSensorObjective implements ObjectiveFunction {
    @Override
    public double value(ProblemModel model, Representation r) {
        return 0;
    }

    @Override
    public ObjectiveType type() {
        return null;
    }
}
