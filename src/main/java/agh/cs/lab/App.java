package agh.cs.lab;

import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.runner.AppRunner;
import agh.cs.lab.view.ViewManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        var defaultSettings = (new ObjectMapper()).readValue(
               getClass().getResource("/settings.json"), SimulationSettings.class);

        var view = ViewManager.load(stage, defaultSettings);
        var runner = new AppRunner(view);
        runner.run();
    }

    public static void main(String[] args) {
        launch();
    }
}