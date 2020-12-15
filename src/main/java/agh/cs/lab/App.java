package agh.cs.lab;

import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.runner.SimulationRunner;
import agh.cs.lab.view.ViewManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        var defaultSettings = (new ObjectMapper()).readValue(
                Paths.get("settings.json").toFile(), SimulationSettings.class);

        var view = ViewManager.load(stage, defaultSettings);
        var singleMapView = view.getSingleMapView();
        var doubleMapView = view.getDoubleMapView();

        var singleRunner = new SimulationRunner(view.getSingleMapView().getMapView());

        var doubleRunner1 = new SimulationRunner(view.getDoubleMapView().getFirstMapView());
        var doubleRunner2 = new SimulationRunner(view.getDoubleMapView().getSecondMapView());

        view.onSingleMapStart(singleRunner::start);
        view.onSingleMapFinish(singleRunner::stop);
    }

    public static void main(String[] args) {
        launch();
    }
}