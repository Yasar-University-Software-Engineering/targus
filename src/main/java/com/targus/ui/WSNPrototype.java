package com.targus.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targus.utils.Constants;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WSNPrototype {
    private int paneWidth;
    private int paneHeight;
    private final ArrayList<Point2D> potentialPositions = new ArrayList<Point2D>();

    public ArrayList<Point2D> getPotentialPositions() {
        return potentialPositions;
    }

    private final ArrayList<Point2D> targets = new ArrayList<Point2D>();

    public ArrayList<Point2D> getTargets() {
        return targets;
    }

    private final IntegerProperty mProperty = new SimpleIntegerProperty(1);

    public IntegerProperty getMProperty() {
        return mProperty;
    }

    private final IntegerProperty kProperty = new SimpleIntegerProperty(1);

    public IntegerProperty getKProperty() {
        return kProperty;
    }

    private final DoubleProperty communicationRangeProperty = new SimpleDoubleProperty(100);

    public DoubleProperty getCommunicationRangeProperty() {
        return communicationRangeProperty;
    }

    private final DoubleProperty sensingRangeProperty = new SimpleDoubleProperty(50);

    public DoubleProperty getSensingRangeProperty() {
        return sensingRangeProperty;
    }

    private final IntegerProperty generationCountProperty = new SimpleIntegerProperty(1000);

    public IntegerProperty getGenerationCountProperty() {
        return generationCountProperty;
    }

    private final DoubleProperty mutationRateProperty = new SimpleDoubleProperty(0.3);

    public DoubleProperty getMutationRateProperty() {
        return mutationRateProperty;
    }

    public int getPaneWidth() {
        return paneWidth;
    }

    public int getPaneHeight() {
        return paneHeight;
    }

    public void setPaneWidth(int paneWidth) {
        this.paneWidth = paneWidth;
    }

    public void setPaneHeight(int paneHeight) {
        this.paneHeight = paneHeight;
    }

    public void exportToFile() {
        try {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File("."));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File f = fc.showSaveDialog(null);

            if (f == null) {
                return;
            }

            String src = f.getAbsolutePath();

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(src));
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> problemInfo = new HashMap<>();
            problemInfo.put(Constants.DIMENSIONS, Arrays.asList(paneWidth, paneHeight));

            List<double[]> targetList = new ArrayList<>();
            for (Point2D target : targets) {
                double[] coords = new double[2];
                coords[0] = target.getX();
                coords[1] = target.getY();
                targetList.add(coords);
            }
            problemInfo.put(Constants.TARGETS, targetList);

            List<double[]> potentialPositionList = new ArrayList<>();
            for (Point2D potentialPosition : potentialPositions) {
                double[] coords = new double[2];
                coords[0] = potentialPosition.getX();
                coords[1] = potentialPosition.getY();
                potentialPositionList.add(coords);
            }
            problemInfo.put(Constants.POTENTIAL_POSITIONS, potentialPositionList);

            problemInfo.put(Constants.COMMUNICATION_RADIUS, communicationRangeProperty.get());
            problemInfo.put(Constants.SENSING_RADIUS, sensingRangeProperty.get());
            problemInfo.put(Constants.M, mProperty.get());
            problemInfo.put(Constants.K, kProperty.get());
            problemInfo.put(Constants.GENERATION_COUNT, generationCountProperty.get());
            problemInfo.put(Constants.MUTATION_RATE, mutationRateProperty.get());

            writer.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(problemInfo));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("."));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File f = fc.showOpenDialog(null);

        if (f == null) {
            return;
        }

        String src = f.getAbsolutePath();

        Point2D dimensions;
        Point2D[] targetArray;
        Point2D[] potentialPositionArray;

        try {
            Reader reader = Files.newBufferedReader(Paths.get(src));
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

            mProperty.set(parser.path(Constants.M).asInt());
            kProperty.set(parser.path(Constants.K).asInt());
            communicationRangeProperty.set(Double.parseDouble(parser.path(Constants.COMMUNICATION_RADIUS).asText()));
            sensingRangeProperty.set(Double.parseDouble(parser.path(Constants.SENSING_RADIUS).asText()));
            generationCountProperty.set(parser.path(Constants.GENERATION_COUNT).asInt());
            mutationRateProperty.set(Double.parseDouble(parser.path(Constants.MUTATION_RATE).asText()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        paneWidth = (int) dimensions.getX();
        paneHeight = (int) dimensions.getY();

        Collections.addAll(targets, targetArray);
        Collections.addAll(potentialPositions, potentialPositionArray);
    }
}