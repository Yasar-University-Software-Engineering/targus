package com.targus.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/targus/main.fxml"));
        Parent root = loader.load();
        root.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        Main.primaryStage.setTitle("Targus");
        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add("/css/style.css");
        scene.getStylesheets().add("/css/button.css");
        Main.primaryStage.setScene(scene);
        Main.primaryStage.setMaximized(true);
        Main.primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
