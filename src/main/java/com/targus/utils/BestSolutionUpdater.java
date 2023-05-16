package com.targus.utils;

import com.targus.base.Solution;
import com.targus.ui.controllers.ObjectiveValueDisplayController;

public class BestSolutionUpdater {
    static ObjectiveValueDisplayController objectiveValueDisplayController;

    public static void setObjectiveValueDisplayController(ObjectiveValueDisplayController objectiveValueDisplayController) {
        BestSolutionUpdater.objectiveValueDisplayController = objectiveValueDisplayController;
    }

    public static void update(long currentState, Solution solution) {
        objectiveValueDisplayController.updateCurrentSolution(currentState, solution);
    }


}
