package agh.cs.lab;

import agh.cs.lab.view.AppView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class World extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        var appView = new AppView();

        var mapView = appView.set();
        var pane = new ScrollPane(mapView.draw());

        var scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}