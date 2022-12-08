package com.targus.problem.wsn;

import com.targus.represent.BitString;
import javafx.geometry.Point2D;

import java.util.BitSet;

public class Test {

    public static void main(String[] args) {
        WSNMinimumSensorObjective wsnMinimumSensorObjective = new WSNMinimumSensorObjective();

        Point2D[] targets = { new Point2D(40, 5),
        new Point2D(25, 10),
        new Point2D(5, 40),
        new Point2D(50, 40),
        new Point2D(75, 45),
        new Point2D(20, 65),
        new Point2D(95, 60),
        new Point2D(45, 80),
        new Point2D(80, 85),
        new Point2D(35, 85),
        new Point2D(70, 90)};

        Point2D[] potentialPositions = { new Point2D(30, 15),
                new Point2D(55, 15),
                new Point2D(80, 15),
                new Point2D(30, 40),
                new Point2D(55, 40),
                new Point2D(80, 40),
                new Point2D(30, 65),
                new Point2D(55, 65),
                new Point2D(80, 65)};

        int m = 3;
        int k = 1;

        int commRange = 10;
        int sensRange = 10;

        int generationCount = 100;
        double mutationRate = 0.03;

        WSN wsn = new WSN(
                targets,
                potentialPositions,
                m,
                k,
                commRange,
                sensRange,
                generationCount,
                mutationRate
        );

        BitString bitString = new BitString(new BitSet(9));
        bitString.getBitSet().set(0, 9);
        bitString.getBitSet().flip(0, 9);

        bitString.getBitSet().set(1);
        bitString.getBitSet().set(2);
        bitString.getBitSet().set(3);
        bitString.getBitSet().set(7);
        bitString.getBitSet().set(8);

        System.out.println("The fitness value is: " + wsnMinimumSensorObjective.value(wsn, bitString));
    }
}
