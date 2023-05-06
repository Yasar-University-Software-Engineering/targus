package com.targus.problem.wsn;

import com.targus.ui.widgets.PotentialPosition;
import com.targus.ui.widgets.Target;
import com.targus.utils.Constants;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class WSNPrototype {
    private IntegerProperty paneWidthProperty = new SimpleIntegerProperty(Constants.DEFAULT_PANE_WIDTH);
    private IntegerProperty paneHeightProperty = new SimpleIntegerProperty(Constants.DEFAULT_PANE_HEIGHT);
    private IntegerProperty mProperty = new SimpleIntegerProperty(Constants.DEFAULT_M);
    private IntegerProperty kProperty = new SimpleIntegerProperty(Constants.DEFAULT_K);
    private DoubleProperty communicationRangeProperty = new SimpleDoubleProperty(Constants.DEFAULT_COMMUNICATION_RANGE);
    private DoubleProperty sensingRangeProperty = new SimpleDoubleProperty(Constants.DEFAULT_SENSING_RANGE);
    private ArrayList<Point2D> targets = new ArrayList<>();
    private ArrayList<Point2D> potentialPositions = new ArrayList<>();

    public WSNPrototype() {
    }

    public WSNPrototype(
            int paneWidth,
            int paneHeight,
            int m,
            int k,
            double communicationRange,
            double sensingRange) {
        this.paneWidthProperty.set(paneWidth);
        this.getPaneHeightProperty().set(paneHeight);
        this.mProperty.set(m);
        this.kProperty.set(k);
        this.communicationRangeProperty.set(communicationRange);
        this.sensingRangeProperty.set(sensingRange);
    }

    public IntegerProperty getPaneWidthProperty() {
        return paneWidthProperty;
    }

    public void setPaneWidthProperty(IntegerProperty paneWidthProperty) {
        this.paneWidthProperty = paneWidthProperty;
    }

    public int getPaneWidth() {
        return paneWidthProperty.get();
    }

    public void setPaneWidth(int paneWidth) {
        paneWidthProperty.set(paneWidth);
    }

    public IntegerProperty getPaneHeightProperty() {
        return paneHeightProperty;
    }

    public void setPaneHeightProperty(IntegerProperty paneHeightProperty) {
        this.paneHeightProperty = paneHeightProperty;
    }

    public int getPaneHeight() {
        return paneHeightProperty.get();
    }

    public void setPaneHeight(int paneHeight) {
        paneHeightProperty.set(paneHeight);
    }

    public IntegerProperty getMProperty() {
        return mProperty;
    }

    public void setMProperty(IntegerProperty mProperty) {
        this.mProperty = mProperty;
    }

    public int getM() {
        return mProperty.get();
    }

    public void setM(int m) {
        mProperty.set(m);
    }

    public IntegerProperty getKProperty() {
        return kProperty;
    }

    public void setKProperty(IntegerProperty kProperty) {
        this.kProperty = kProperty;
    }

    public int getK() {
        return kProperty.get();
    }

    public void setK(int k) {
        kProperty.set(k);
    }

    public DoubleProperty getCommunicationRangeProperty() {
        return communicationRangeProperty;
    }

    public void setCommunicationRangeProperty(DoubleProperty communicationRangeProperty) {
        this.communicationRangeProperty = communicationRangeProperty;
    }

    public double getCommunicationRange() {
        return communicationRangeProperty.get();
    }

    public void setCommunicationRange(double communicationRange) {
        communicationRangeProperty.set(communicationRange);
    }

    public DoubleProperty getSensingRangeProperty() {
        return sensingRangeProperty;
    }

    public void setSensingRangeProperty(DoubleProperty sensingRangeProperty) {
        this.sensingRangeProperty = sensingRangeProperty;
    }

    public double getSensingRange() {
        return sensingRangeProperty.get();
    }

    public void setSensingRange(double sensingRange) {
        sensingRangeProperty.set(sensingRange);
    }

    public ArrayList<Point2D> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<Point2D> targets) {
        this.targets = targets;
    }

    public ArrayList<Point2D> getPotentialPositions() {
        return potentialPositions;
    }
    public Point2D[] getPotentialPositionsAsArray() {
        return potentialPositions.toArray(new Point2D[0]);
    }

    public void setPotentialPositions(ArrayList<Point2D> potentialPositions) {
        this.potentialPositions = potentialPositions;
    }

    public Point2D getTargetByIndex(int index) {
        return targets.get(index);
    }

    public int getTargetsSize() {
        return targets.size();
    }

    public void addTarget(Target target) {
        this.targets.add(new Point2D(target.getCenterX(), target.getCenterY()));
    }

    public void addTargets(ArrayList<Point2D> targets) {
        this.targets.addAll(targets);
    }

    public void clearTargets() {
        this.targets.clear();
    }

    public Point2D getPotentialPositionByIndex(int index) {
        return potentialPositions.get(index);
    }

    public int getPotentialPositionsSize() {
        return potentialPositions.size();
    }

    public void addPotentialPosition(PotentialPosition potentialPosition) {
        this.potentialPositions.add(new Point2D(potentialPosition.getCenterX(), potentialPosition.getCenterY()));
    }

    public void addPotentialPositions(ArrayList<Point2D> potentialPositions) {
        this.potentialPositions.addAll(potentialPositions);
    }

    public void clearPotentialPositions() {
        this.potentialPositions.clear();
    }
}