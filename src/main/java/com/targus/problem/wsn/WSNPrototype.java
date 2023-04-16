package com.targus.problem.wsn;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class WSNPrototype {
    private final IntegerProperty paneWidthProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty paneHeightProperty = new SimpleIntegerProperty(0);
    private final ArrayList<Point2D> targets = new ArrayList<>();
    private final ArrayList<Point2D> potentialPositions = new ArrayList<>();
    private final IntegerProperty mProperty = new SimpleIntegerProperty(1);
    private final IntegerProperty kProperty = new SimpleIntegerProperty(1);
    private final DoubleProperty communicationRangeProperty = new SimpleDoubleProperty(100);
    private final DoubleProperty sensingRangeProperty = new SimpleDoubleProperty(50);

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

    public IntegerProperty getPaneHeightProperty() {
        return paneHeightProperty;
    }

    public ArrayList<Point2D> getTargets() {
        return targets;
    }

    public ArrayList<Point2D> getPotentialPositions() {
        return potentialPositions;
    }

    public IntegerProperty getMProperty() {
        return mProperty;
    }

    public IntegerProperty getKProperty() {
        return kProperty;
    }

    public DoubleProperty getCommunicationRangeProperty() {
        return communicationRangeProperty;
    }

    public DoubleProperty getSensingRangeProperty() {
        return sensingRangeProperty;
    }
}