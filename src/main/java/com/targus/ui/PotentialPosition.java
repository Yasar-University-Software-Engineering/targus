package com.targus.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class PotentialPosition extends Circle {

    public PotentialPosition(double layoutX, double layoutY) {

        setLayoutX(layoutX);
        setLayoutY(layoutY);
        setRadius(4.0);
        setFill(Color.WHITE);
        setStroke(Color.RED);
        setStrokeType(StrokeType.INSIDE);

    }

}