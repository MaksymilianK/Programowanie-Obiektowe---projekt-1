package agh.cs.lab.runner;

import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.shared.Pair;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.view.SimulationControlController;
import agh.cs.lab.view.ViewManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

public class AppRunner {

    private final Rand rand = new Rand();
    private final ViewManager viewManager;

    private SimulationRunner singleSimulation;
    private SimulationRunner firstSimulation;
    private SimulationRunner secondSimulation;

    public AppRunner(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void run() {
        viewManager.getMenu().onSingleSimulationChoice(this::prepareSingleSimulation);
        viewManager.getMenu().onDoubleSimulationChoice(this::prepareDoubleSimulation);

        viewManager.getSingleMapController().getControlController().onResume(this::resumeSingleSimulation);
        viewManager.getSingleMapController().getControlController().onPause(this::pauseSingleSimulation);
        viewManager.getSingleMapController().getControlController().onFinish(this::finishSingleSimulation);
        viewManager.getSingleMapController().getControlController().onMostCommonGenes(this::drawMostCommonGenesSingle);
        viewManager.getSingleMapController().getControlController().onStatistics(this::writeStatistics);

        viewManager.getDoubleMapController().getFirstControlController().onResume(this::resumeFirstSimulation);
        viewManager.getDoubleMapController().getFirstControlController().onPause(this::pauseFirstSimulation);
        viewManager.getDoubleMapController().getFirstControlController().onFinish(this::finishFirstSimulation);
        viewManager.getDoubleMapController().getFirstControlController().onMostCommonGenes(this::drawMostCommonGenesFirst);
        viewManager.getDoubleMapController().getFirstControlController().onStatistics(this::writeStatistics);

        viewManager.getDoubleMapController().getSecondControlController().onResume(this::resumeSecondSimulation);
        viewManager.getDoubleMapController().getSecondControlController().onPause(this::pauseSecondSimulation);
        viewManager.getDoubleMapController().getSecondControlController().onFinish(this::finishSecondSimulation);
        viewManager.getDoubleMapController().getSecondControlController().onMostCommonGenes(this::drawMostCommonGenesSecond);
        viewManager.getDoubleMapController().getSecondControlController().onStatistics(this::writeStatistics);

        viewManager.showMenu();
        viewManager.show();
    }

    private void drawMostCommonGenesSingle() {
        singleSimulation.drawMostCommonGenes();
    }

    private void drawMostCommonGenesFirst() {
        firstSimulation.drawMostCommonGenes();

    }

    private void drawMostCommonGenesSecond() {
        secondSimulation.drawMostCommonGenes();
    }

    private void prepareSingleSimulation(SimulationSettings settings) {
        singleSimulation = createSingleSimulationRunner(settings);
        viewManager.showSingleMapApp();

        Platform.runLater(() -> {
            viewManager.getSingleMapController().setMapSize();
            singleSimulation.afterPreparation();
        });
    }

    private void prepareDoubleSimulation(SimulationSettings settings) {
        viewManager.showDoubleMapApp();

        var runners = createDoubleSimulationRunners(settings);
        firstSimulation = runners.first;
        secondSimulation = runners.second;

        var a = new Timeline(
                new KeyFrame(Duration.seconds(1), action -> {
                    viewManager.getDoubleMapController().setMapsSize();
                    firstSimulation.afterPreparation();
                    secondSimulation.afterPreparation();
                })
        );
        a.play();
    }

    private void resumeSingleSimulation(double speed) {
        resumeSimulation(singleSimulation, viewManager.getSingleMapController().getControlController(), speed);
    }

    private void resumeFirstSimulation(double speed) {
        resumeSimulation(firstSimulation, viewManager.getDoubleMapController().getFirstControlController(), speed);
    }

    private void resumeSecondSimulation(double speed) {
        resumeSimulation(secondSimulation, viewManager.getDoubleMapController().getSecondControlController(), speed);
    }

    private void pauseSingleSimulation() {
        pauseSimulation(singleSimulation, viewManager.getSingleMapController().getControlController());
    }

    private void pauseFirstSimulation() {
        pauseSimulation(firstSimulation, viewManager.getDoubleMapController().getFirstControlController());
    }

    private void pauseSecondSimulation() {
        pauseSimulation(secondSimulation, viewManager.getDoubleMapController().getSecondControlController());
    }

    private void finishSingleSimulation() {
        singleSimulation = null;
        finishSimulation();
    }

    private void finishFirstSimulation() {
        firstSimulation = null;
        finishSimulation();
    }

    private void finishSecondSimulation() {
        secondSimulation = null;
        finishSimulation();
    }

    private void resumeSimulation(SimulationRunner runner, SimulationControlController control, double speed) {
        runner.setSpeed(speed);
        runner.resume();
        control.setRunning(true);
    }

    private void pauseSimulation(SimulationRunner runner, SimulationControlController control) {
        control.setRunning(false);
        runner.pause();
    }

    private void finishSimulation() {
        viewManager.showMenu();
    }

    private SimulationRunner createSingleSimulationRunner(SimulationSettings settings) {
        var runner = SimulationRunner.prepare(
                settings,
                viewManager.getSingleMapController().getMapController(),
                viewManager.getSingleMapController().getStatisticsController(),
                viewManager.getSingleMapController().getAnimalDetailsController(),
                rand
        );
        runner.init();
        return runner;
    }

    private Pair<SimulationRunner, SimulationRunner> createDoubleSimulationRunners(SimulationSettings settings) {
        var first = SimulationRunner.prepare(
                settings,
                viewManager.getDoubleMapController().getFirstMapController(),
                viewManager.getDoubleMapController().getFirstStatisticsController(),
                viewManager.getDoubleMapController().getFirstAnimalDetailsController(),
                rand
        );
        var second = SimulationRunner.prepare(
                settings,
                viewManager.getDoubleMapController().getSecondMapController(),
                viewManager.getDoubleMapController().getSecondStatisticsController(),
                viewManager.getDoubleMapController().getSecondAnimalDetailsController(),
                rand
        );
        first.init();
        second.init();
        return new Pair<>(first, second);
    }

    private void writeStatistics() {
        //todo
    }
}
