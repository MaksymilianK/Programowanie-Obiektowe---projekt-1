package agh.cs.lab.runner;

import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.shared.Pair;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.view.SimulationControlView;
import agh.cs.lab.view.ViewManager;

public class AppRunner {

    private final Rand rand = new Rand();
    private final ViewManager viewManager;

    private SimulationRunner singleSimulation;
    private Pair<SimulationRunner, SimulationRunner> doubleSimulation;

    public AppRunner(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void run(ViewManager viewManager) {
        viewManager.getMenu().onSingleSimulationChoice(this::prepareSingleSimulation);
        viewManager.getMenu().onDoubleSimulationChoice(this::prepareDoubleSimulation);

        viewManager.getSingleMapView().getControlView().onResume(this::resumeSingleSimulation);
        viewManager.getSingleMapView().getControlView().onPause(this::pauseSingleSimulation);
        viewManager.getSingleMapView().getControlView().onFinish(this::finishSingleSimulation);
        viewManager.getSingleMapView().getControlView().onStatistics(this::writeStatistics);

        viewManager.getDoubleMapView().getFirstControlView().onResume(this::resumeFirstSimulation);
        viewManager.getDoubleMapView().getFirstControlView().onPause(this::pauseFirstSimulation);
        viewManager.getDoubleMapView().getFirstControlView().onFinish(this::finishFirstSimulation);
        viewManager.getDoubleMapView().getFirstControlView().onStatistics(this::writeStatistics);

        viewManager.getDoubleMapView().getSecondControlView().onResume(this::resumeSecondSimulation);
        viewManager.getDoubleMapView().getSecondControlView().onPause(this::pauseSecondSimulation);
        viewManager.getDoubleMapView().getSecondControlView().onFinish(this::finishSecondSimulation);
        viewManager.getDoubleMapView().getSecondControlView().onStatistics(this::writeStatistics);

        viewManager.showMenu();
    }

    private void prepareSingleSimulation(SimulationSettings settings) {
        viewManager.showSingleMapApp(settings);

        singleSimulation = createSimulationRunner(settings);
    }

    private void prepareDoubleSimulation(SimulationSettings settings) {
        viewManager.showDoubleMapApp(settings);

        doubleSimulation = new Pair<>(
                createSimulationRunner(settings),
                createSimulationRunner(settings)
        );
    }

    private void resumeSingleSimulation(double speed) {
        resumeSimulation(singleSimulation, viewManager.getSingleMapView().getControlView(), speed);
    }

    private void resumeFirstSimulation(double speed) {
        resumeSimulation(doubleSimulation.first, viewManager.getDoubleMapView().getFirstControlView(), speed);
    }

    private void resumeSecondSimulation(double speed) {
        resumeSimulation(doubleSimulation.second, viewManager.getDoubleMapView().getSecondControlView(), speed);
    }

    private void pauseSingleSimulation() {
        pauseSimulation(singleSimulation, viewManager.getSingleMapView().getControlView());
    }

    private void pauseFirstSimulation() {
        pauseSimulation(doubleSimulation.first, viewManager.getDoubleMapView().getFirstControlView());
    }

    private void pauseSecondSimulation() {
        pauseSimulation(doubleSimulation.second, viewManager.getDoubleMapView().getSecondControlView());
    }

    private void finishSingleSimulation() {
        finishSimulation(singleSimulation);
    }

    private void finishFirstSimulation() {
        finishSimulation(doubleSimulation.first);
    }

    private void finishSecondSimulation() {
        finishSimulation(doubleSimulation.second);
    }

    private void resumeSimulation(SimulationRunner runner, SimulationControlView control, double speed) {
        runner.setSpeed(speed);
        runner.resume();
        control.setRunning(true);
    }

    private void pauseSimulation(SimulationRunner runner, SimulationControlView control) {
        control.setRunning(false);
        runner.pause();
    }

    private void finishSimulation(SimulationRunner runner) {
        runner.pause();
        viewManager.showMenu();
    }

    private SimulationRunner createSimulationRunner(SimulationSettings settings) {
        return SimulationRunner.prepare(
                settings,
                viewManager.getDoubleMapView().getFirstMapView(),
                viewManager.getSingleMapView().getStatisticsView(),
                viewManager.getSingleMapView().getAnimalDetailsView(),
                rand
        );
    }

    private void writeStatistics() {
        //todo
    }
}
