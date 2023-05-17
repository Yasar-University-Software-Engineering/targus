package com.targus.ui;

import com.targus.ui.widgets.Sensor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/targus/main.fxml"));
        Parent root = loader.load();

        Main.primaryStage.setTitle("targus");
        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add("/css/styles.css");

        Main.primaryStage.setScene(scene);
        Main.primaryStage.setMaximized(true);
        Main.primaryStage.show();

        Image logoImage = new Image(Objects.requireNonNull(getClass().getResource("/icons/logo.png")).toExternalForm());
        primaryStage.getIcons().add(logoImage);
    }

    @Override
    public void stop() {
        Sensor.clearSensorArrayList();
    }

}
