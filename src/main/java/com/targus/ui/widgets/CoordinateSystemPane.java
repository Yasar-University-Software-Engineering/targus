package com.targus.ui.widgets;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.Text;

public class CoordinateSystemPane extends Pane {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final double AXIS_WIDTH = 2.0;
    private static final Color AXIS_COLOR = Color.BLACK;

    private Line xAxis;
    private Line yAxis;

    private static final double UNIT_SIZE = 50.0;
    private static final double LINE_WIDTH = 2.0;

    public CoordinateSystemPane() {
//        // Add x-axis
//        Line xAxis = new Line(-1000, 0, 1000, 0);
//        xAxis.setStroke(Color.BLACK);
//        xAxis.setStrokeWidth(LINE_WIDTH);
//        this.getChildren().add(xAxis);
//
//        // Add y-axis
//        Line yAxis = new Line(0, -1000, 0, 1000);
//        yAxis.setStroke(Color.BLACK);
//        yAxis.setStrokeWidth(LINE_WIDTH);
//        this.getChildren().add(yAxis);
//
//        // Add x-axis labels
//        for (double x = -1000; x <= 1000; x += UNIT_SIZE) {
//            if (x != 0) {
//                Text label = new Text(String.valueOf((int) x));
//                label.setLayoutX(x);
//                label.setLayoutY(-LINE_WIDTH - 5);
//                this.getChildren().add(label);
//            }
//        }
//
//        // Add y-axis labels
//        for (double y = -1000; y <= 1000; y += UNIT_SIZE) {
//            if (y != 0) {
//                Text label = new Text(String.valueOf((int) y));
//                label.setLayoutX(-LINE_WIDTH - 20);
//                label.setLayoutY(y);
//                this.getChildren().add(label);
//            }
//        }
//
//        setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public void setPrefSize(double width, double height) {
        super.setPrefSize(width, height);
        xAxis.setEndX(width);
        yAxis.setEndY(height);
    }
}
