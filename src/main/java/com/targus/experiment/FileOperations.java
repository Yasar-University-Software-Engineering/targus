package com.targus.experiment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targus.problem.wsn.WSN;
import com.targus.utils.Constants;
import javafx.geometry.Point2D;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileOperations {

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

    public static boolean doesFileExist(String filePath) {
        File f = new File(filePath);
        return f.exists() && !f.isDirectory();
    }

    public static void writeToJson(JsonProblem jsonProblem, String filePath, boolean override) {
        if(doesFileExist(filePath) && !override) {
            System.out.println(filePath + " already exists. No changes made to the file.");
            System.out.println("\nIf you want to make changes to the file, you may provide 'true' for the override parameter\n");
            return;
        }

        BufferedWriter writer;
        try {
            writer = Files.newBufferedWriter(Paths.get(filePath));
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> problemInfo = new HashMap<>();
            problemInfo.put(Constants.DIMENSIONS, Arrays.asList(jsonProblem.getDimensions().getX(), jsonProblem.getDimensions().getY()));

            List<double[]> targetList = new ArrayList<>();
            for (Point2D target : jsonProblem.getTargets()) {
                double[] coords = new double[2];
                coords[0] = target.getX();
                coords[1] = target.getY();
                targetList.add(coords);
            }
            problemInfo.put(Constants.TARGETS, targetList);

            List<double[]> potentialPositionList = new ArrayList<>();
            for (Point2D potentialPosition : jsonProblem.getPotentialPositions()) {
                double[] coords = new double[2];
                coords[0] = potentialPosition.getX();
                coords[1] = potentialPosition.getY();
                potentialPositionList.add(coords);
            }
            problemInfo.put(Constants.POTENTIAL_POSITIONS, potentialPositionList);

            problemInfo.put(Constants.COMMUNICATION_RADIUS, jsonProblem.getCommunicationRange());
            problemInfo.put(Constants.SENSING_RADIUS, jsonProblem.getSensingRange());
            problemInfo.put(Constants.M, jsonProblem.getM());
            problemInfo.put(Constants.K, jsonProblem.getK());
            problemInfo.put(Constants.GENERATION_COUNT, jsonProblem.getTerminationValue());
            problemInfo.put(Constants.MUTATION_RATE, jsonProblem.getMutationRate());

            writer.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(problemInfo));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong when writing to json file. " + e);
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

}
