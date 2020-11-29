package agh.cs.lab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class World extends Application {

    @Override
    public void start(Stage primaryStage) throws InterruptedException {

        var canvas = new Canvas();
        var pane = new ScrollPane(canvas);

        var scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}