package com.targus.experiment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targus.algorithm.ga.TerminalState;
import com.targus.base.OptimizationProblem;
import com.targus.base.Solution;
import com.targus.problem.wsn.WSN;
import com.targus.utils.Constants;
import javafx.geometry.Point2D;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public static WSN readFromJson(String filePath) {
        Point2D dimensions;
        Point2D[] targetArray;
        Point2D[] potentialPositionArray;

        try {
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parser = objectMapper.readTree(reader);

            JsonNode dimensionsNode = parser.path(Constants.DIMENSIONS);
            dimensions = new Point2D(dimensionsNode.get(0).asDouble(), dimensionsNode.get(1).asDouble());

            JsonNode targetsNode = parser.path(Constants.TARGETS);
            targetArray = new Point2D[targetsNode.size()];
            for (int i = 0; i < targetsNode.size(); i++) {
                JsonNode node = targetsNode.get(i);
                Point2D target = new Point2D(node.get(0).asDouble(), node.get(1).asDouble());
                targetArray[i] = target;
            }

            JsonNode potentialPositionsNode = parser.path(Constants.POTENTIAL_POSITIONS);
            potentialPositionArray = new Point2D[potentialPositionsNode.size()];
            for (int i = 0; i < potentialPositionsNode.size(); i++) {
                JsonNode node = potentialPositionsNode.get(i);
                Point2D potentialPos = new Point2D(node.get(0).asDouble(), node.get(1).asDouble());
                potentialPositionArray[i] = potentialPos;
            }

            int m = parser.path(Constants.M).asInt();
            int k = parser.path(Constants.K).asInt();
            double communicationRadius = Double.parseDouble(parser.path(Constants.COMMUNICATION_RADIUS).asText());
            double sensingRadius = Double.parseDouble(parser.path(Constants.SENSING_RADIUS).asText());
            int terminationValue = parser.path(Constants.GENERATION_COUNT).asInt();
            double mutationRate = Double.parseDouble(parser.path(Constants.MUTATION_RATE).asText());

            return new WSN(targetArray, potentialPositionArray, m, k, communicationRadius, sensingRadius, terminationValue, mutationRate);
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
