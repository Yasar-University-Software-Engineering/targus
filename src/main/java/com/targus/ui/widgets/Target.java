package com.targus.ui.widgets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Target extends Circle {
    public Target(double layoutX, double layoutY) {
        setLayoutX(layoutX);
        setLayoutY(layoutY);
        setRadius(2.0);
        setFill(Color.RED);
    }
}