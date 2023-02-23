package com.targus.experiment;

import com.targus.algorithm.ga.TerminalState;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.utils.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Experiment {

    public static void writeToFile(String fileName, String text, boolean append) {
        try(FileWriter fw = new FileWriter(fileName, append);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getBenchmarkTestInformation(Solution bestSolution, TerminalState terminalState) {
        return bestSolution.getRepresentation() + "\n" + bestSolution.objectiveValue() + "\n" + terminalState.getCurrentState() + "\n\n\n";
    }

    public static String getProblemInformation(OptimizationProblem problem) {
        WSN wsn = (WSN) problem.model();
        return String.format("K: %d M: %d Mutation Rate: %f Initial Population: %d Communication Range: %d Sensing Range: %d\n\n",
                wsn.getK(), wsn.getM(), wsn.getMutationRate(), Constants.DEFAULT_POPULATION_COUNT,
                Constants.DEFAULT_COMMUNICATION_RANGE, Constants.DEFAULT_SENSING_RANGE);
    }

}
