package com.targus.ui.widgets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class PotentialPosition extends Circle {
    public PotentialPosition(double centerX, double centerY) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(4.0);
        setFill(Color.WHITE);
        setStroke(Color.RED);
        setStrokeType(StrokeType.INSIDE);
        setViewOrder(2);
    }
}