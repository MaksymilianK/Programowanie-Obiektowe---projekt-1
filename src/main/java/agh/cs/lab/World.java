package agh.cs.lab;

import agh.cs.lab.map.WorldMap;
import agh.cs.lab.view.AppView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class World extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        var worldMap = WorldMap.create(300, 300, 0.3f);

        var appView = new AppView();

        var mapView = appView.set();
        var pane = new ScrollPane();

        var scene = new Scene(pane);

        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        pane.setContent(mapView.draw(worldMap.getMapBorders(), worldMap.getJungleBorders(),
                primaryStage.getWidth(), primaryStage.getHeight()));
    }

    public static void main(String[] args) {
        launch();
    }
}