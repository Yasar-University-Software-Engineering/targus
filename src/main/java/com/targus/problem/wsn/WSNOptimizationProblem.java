package com.targus.problem.wsn;

import com.targus.base.OptimizationProblem;
import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import com.targus.problem.ObjectiveFunction;
import com.targus.problem.ObjectiveType;


public class WSNOptimizationProblem implements OptimizationProblem {
    private ProblemModel model;
    private ObjectiveFunction objectiveFunction;

    public WSNOptimizationProblem(ProblemModel model, ObjectiveFunction objectiveFunction) {
        this.model = model;
        this.objectiveFunction = objectiveFunction;
    }

    @Override
    public ProblemModel model() {
        return model;
    }

    public ObjectiveFunction getObjectiveFunction() {
        return objectiveFunction;
    }

    @Override
    public double objectiveValue(Representation r) {
        return objectiveFunction.value(model, r);
    }

    @Override
    public ObjectiveType objectiveType() {
        return objectiveFunction.type();
    }
}
