package agh.cs.lab.view;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class AppView {

    public WorldMapView set() throws IOException {
        var loader = new FXMLLoader(getClass().getResource("/world-map.fxml"));
        var canvas = loader.load();
        return loader.getController();
    }
}
