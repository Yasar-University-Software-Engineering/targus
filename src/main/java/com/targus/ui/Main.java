package com.targus.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/targus/main.fxml"));
        BorderPane root = loader.load();

//        // Set style for left border
//        root.getLeft().setStyle("-fx-border-color: grey;");
//        root.getLeft().setStyle("-fx-border-width: 0px 2px 0px 0px;");
//        root.getLeft().setStyle("-fx-border-style: solid;");
//
//        root.getCenter().setStyle("-fx-background-color: #f0f0f0;");
//        root.getCenter().setStyle("-fx-border-width: 0px 2px 0px 0px;");

        Main.primaryStage.setTitle("Targus");
        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add("/css/styles.css");

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
