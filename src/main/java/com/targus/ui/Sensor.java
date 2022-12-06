package com.targus.ui;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sensor extends Circle {

    private static double communicationRangeRadius;
    private static double sensingRangeRadius;

    private Circle communicationRangeCircle;


    public Sensor(double layoutX, double layoutY) {


        setLayoutX(layoutX);
        setLayoutY(layoutY);
        setRadius(9);
        setFill(Color.GREEN);
//çemberler gelmiyor
        Circle communicationRangeCircle = new Circle();
        communicationRangeCircle.setLayoutX(layoutX);
        communicationRangeCircle.setLayoutX(layoutX);
        communicationRangeCircle.setRadius(communicationRangeRadius);
        communicationRangeCircle.setFill(Color.TRANSPARENT);
        communicationRangeCircle.setStroke(Color.GREEN);

        Circle sensingRangeCircle = new Circle();
        sensingRangeCircle.setLayoutX(layoutX);
        sensingRangeCircle.setLayoutX(layoutX);
        sensingRangeCircle.setRadius(sensingRangeRadius);
      /*  sensingRangeCircle.setFill(Color.TRANSPARENT);
        sensingRangeCircle.setStroke(Color.BLUE);*/
        sensingRangeCircle.setFill(new Color(0,0,0,0));

    }

    public double getSensingRangeRadius() {
        return sensingRangeRadius;
    }

    public double getCommunicationRangeRadius() {
        return communicationRangeRadius;
    }

    public static void setRadii(int communicationRangeRadius, int sensingRangeRadius){
        Sensor.communicationRangeRadius = communicationRangeRadius;
        Sensor.sensingRangeRadius = sensingRangeRadius;
    }
}
