package com.targus.experiment;

import com.targus.problem.wsn.WSN;
import com.targus.utils.Constants;
import javafx.geometry.Point2D;

public class JsonProblem {

    private final WSN wsn;
    private final Point2D dimensions;
    private final int terminationValue;
    private int communicationRange = Constants.DEFAULT_COMMUNICATION_RANGE;
    private int sensingRange = Constants.DEFAULT_SENSING_RANGE;

    public JsonProblem(WSN wsn, Point2D dimensions, int terminationValue) {
        this.wsn = wsn;
        this.dimensions = dimensions;
        this.terminationValue = terminationValue;
    }

    public JsonProblem(WSN wsn, Point2D dimensions, int terminationValue, int communicationRange, int sensingRange) {
        this(wsn, dimensions, terminationValue);
        this.communicationRange = communicationRange;
        this.sensingRange = sensingRange;
    }

    public Point2D[] getTargets() {
        return wsn.getTargets();
    }

    public Point2D[] getPotentialPositions() {
        return wsn.getPotentialPositions();
    }

    public int getK() {
        return wsn.getK();
    }

    public int getM() {
        return wsn.getM();
    }

    public Point2D getDimensions() {
        return dimensions;
    }

    public int getCommunicationRange() {
        return communicationRange;
    }

    public void setCommunicationRange(int communicationRange) {
        this.communicationRange = communicationRange;
    }

    public int getSensingRange() {
        return sensingRange;
    }

    public void setSensingRange(int sensingRange) {
        this.sensingRange = sensingRange;
    }

    public int getTerminationValue() {
        return terminationValue;
    }

    public double getMutationRate() {
        return wsn.getMutationRate();
    }
}
