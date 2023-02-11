package com.targus.experiment.wsn;

import javafx.geometry.Point2D;

import java.security.SecureRandom;
import java.util.Random;

public class WSNProblemGenerator {

    public static Point2D[] generateRandomPoint2DArray(Point2D dimensions, int arrayLength, boolean gridShaped) {
        Random random = new SecureRandom();
        Point2D[] point2DArray = new Point2D[arrayLength];
        int screenX = (int) dimensions.getX();
        int screenY = (int) dimensions.getY();

        if (gridShaped) {
            int stepX = screenX / arrayLength;
            int stepY = screenY / arrayLength;
            int index = 0;
            for (int i = 0; i < screenX; i += stepX) {
                for (int j = 0; j < screenY; j += stepY) {
                    if (index < point2DArray.length) {
                        int x = random.nextInt(i, i + stepX);
                        int y = random.nextInt(j, j + stepY);
                        point2DArray[index] = new Point2D(x, y);
                        index++;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < arrayLength; i++) {
                int x = random.nextInt(screenX);
                int y = random.nextInt(screenY);
                point2DArray[i] = new Point2D(x, y);
            }
        }
        return point2DArray;
    }
}
