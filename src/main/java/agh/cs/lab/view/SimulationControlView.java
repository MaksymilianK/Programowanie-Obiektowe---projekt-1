package agh.cs.lab.view;

import agh.cs.lab.runner.SimulationRunner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

import java.util.function.Consumer;

public class SimulationControlView implements View {

    @FXML
    private Slider speed;

    @FXML
    private Button resumeButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button finishButton;

    @FXML
    private Button statisticsButton;

    private Consumer<Double> onResume;
    private Runnable onPause;
    private Runnable onFinish;
    private Runnable onStatistics;

    public void setRunning(boolean isRunning) {
        speed.setDisable(isRunning);
        resumeButton.setDisable(isRunning);
        pauseButton.setDisable(!isRunning);
        finishButton.setDisable(isRunning);
        statisticsButton.setDisable(isRunning);
    }

    public void onResume(Consumer<Double> onResume) {
        this.onResume = onResume;
    }

    public void onPause(Runnable onPause) {
        this.onPause = onPause;
    }

    public void onFinish(Runnable onFinish) {
        this.onFinish = onFinish;
    }

    public void onStatistics(Runnable onStatistics) {
        this.onStatistics = onStatistics;
    }

    @Override
    public void reset() {
        speed.setValue(SimulationRunner.DEFAULT_SPEED);
        setRunning(false);
    }

    @FXML
    private void initialize() {
        speed.setMin(SimulationRunner.MIN_SPEED);
        speed.setMin(SimulationRunner.MAX_SPEED);
        reset();

        resumeButton.setOnAction(event -> onResume.accept(speed.getValue()));
        pauseButton.setOnAction(event -> onPause.run());
        finishButton.setOnAction(event -> onFinish.run());
        statisticsButton.setOnAction(event -> onStatistics.run());
    }
}
