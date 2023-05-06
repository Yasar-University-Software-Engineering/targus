package com.targus.ui.widgets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Target extends Circle {
    public Target(double centerX, double centerY) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(2.0);
        setFill(Color.RED);
        setViewOrder(1);
    }
}