package com.targus;

import com.targus.base.Solution;
import com.targus.ui.Mediator;
import com.targus.ui.controllers.ObjectiveValueDisplayController;

public class BestSolutionUpdater {

    private static ObjectiveValueDisplayController objectiveValueDisplayController;
    private static Mediator mediator;

    public static void setObjectiveValueDisplayController(ObjectiveValueDisplayController objectiveValueDisplayController) {
        BestSolutionUpdater.objectiveValueDisplayController = objectiveValueDisplayController;
    }

    public static void setMediator(Mediator mediator) {
        BestSolutionUpdater.mediator = mediator;
    }

    public static void update(Solution oldSolution, Solution newSolution) {
        objectiveValueDisplayController.display(newSolution);
        mediator.displaySolution(oldSolution, newSolution);

    }
}
