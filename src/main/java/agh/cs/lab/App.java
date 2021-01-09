package agh.cs.lab;
// resources nie powinny być w src
import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.runner.AppRunner;
import agh.cs.lab.shared.Rand;
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

        AppRunner.create(
                ViewManager.load(stage, defaultSettings),
                new Rand()
        );
    }

    public static void main(String[] args) {
        launch();
    }
}