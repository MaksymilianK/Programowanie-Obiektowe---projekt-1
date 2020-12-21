package agh.cs.lab.view;

import agh.cs.lab.runner.SimulationRunner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

import java.util.function.Consumer;

public class SimulationControlController implements Controller {

    @FXML
    private Slider speed;

    @FXML
    private Button resumeButton;

    @FXML
    private Button pauseButton;

    public void setRunning(boolean isRunning) {
        speed.setDisable(isRunning);
        resumeButton.setDisable(isRunning);
        pauseButton.setDisable(!isRunning);
    }

    public void onResume(Consumer<Double> onResume) {
        resumeButton.setOnAction(event -> onResume.accept(speed.getValue()));
    }

    public void onPause(Runnable onPause) {
        pauseButton.setOnAction(event -> onPause.run());
    }

    @Override
    public void reset() {
        speed.setValue(SimulationRunner.DEFAULT_SPEED);
        setRunning(false);
    }

    @Override
    public void init() {
        speed.setMin(SimulationRunner.MIN_SPEED);
        speed.setMax(SimulationRunner.MAX_SPEED);
        reset();
    }
}
