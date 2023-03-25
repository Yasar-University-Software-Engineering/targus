package com.targus.ui.widgets;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class CoordinateSystemPane extends Pane {

    private final int tickSize = 50;

    public CoordinateSystemPane() {
        setPrefSize(500, 500);
        setMaxSize(500, 500);
        setMinSize(500, 500);
        drawAxes();
        drawTicks();
    }

    private void drawAxes() {
        // create x and y axis lines
        Line xAxis = new Line(0, getHeight() / 2, getWidth(), getHeight() / 2);
        Line yAxis = new Line(getWidth() / 2, 0, getWidth() / 2, getHeight());

        // set stroke colors
        xAxis.setStroke(Color.BLACK);
        yAxis.setStroke(Color.BLACK);

        // add lines to pane
        getChildren().addAll(xAxis, yAxis);

        // add axis labels
        Text xAxisLabel = new Text(getWidth() - 20, getHeight() / 2 + 20, "X");
        Text yAxisLabel = new Text(getWidth() / 2 + 20, 20, "Y");
        getChildren().addAll(xAxisLabel, yAxisLabel);
    }

    private void drawTicks() {
        double width = getWidth();
        double height = getHeight();
        // create vertical ticks
        for (double x = 0; x < width; x += tickSize) {
            Line tick = new Line(x, height / 2 - 5, x, height / 2 + 5);
            tick.setStroke(Color.BLACK);
            getChildren().add(tick);

            Text tickLabel = new Text(x - 5, height / 2 + 20, String.valueOf(x));
            getChildren().add(tickLabel);
        }
        // create horizontal ticks
        for (double y = 0; y < height; y += tickSize) {
            Line tick = new Line(width / 2 - 5, y, width / 2 + 5, y);
            tick.setStroke(Color.BLACK);
            getChildren().add(tick);

            Text tickLabel = new Text(width / 2 + 10, y + 5, String.valueOf(y));
            getChildren().add(tickLabel);
        }
    }
}
