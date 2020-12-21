package agh.cs.lab.runner;

import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.view.ViewManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AppRunner {

    private final ViewManager viewManager;

    private SimulationRunner singleSimulation;
    private SimulationRunner firstSimulation;
    private SimulationRunner secondSimulation;
    private Timeline resizingTimeline;

    private AppRunner(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    private void onMenu() {
        if (resizingTimeline != null) {
            resizingTimeline.stop();
            resizingTimeline = null;
        }

        viewManager.showMenu();

        if (singleSimulation != null) {
            singleSimulation.finish();
            singleSimulation = null;
        } else if (firstSimulation != null) {
            firstSimulation.finish();
            secondSimulation.finish();
            firstSimulation = null;
            secondSimulation = null;
        }

        singleSimulation = null;
        firstSimulation = null;
        secondSimulation = null;
    }

    private void createSingleSimulation(SimulationSettings settings, Rand rand) {
        viewManager.showSingleMapApp();

        singleSimulation = SimulationRunner.builder()
                .mapController(viewManager.getSingleMapController().getMapController())
                .controlController(viewManager.getSingleMapController().getControlController())
                .statisticsController(viewManager.getSingleMapController().getStatisticsController())
                .animalDetailsController(viewManager.getSingleMapController().getAnimalDetailsController())
                .settings(settings)
                .rand(rand)
                .build();

        resizeSingleMap();
    }

    private void createDoubleSimulation(SimulationSettings settings, Rand rand) {
        viewManager.showDoubleMapApp();

        firstSimulation = SimulationRunner.builder()
                .mapController(viewManager.getDoubleMapController().getFirstMapController())
                .controlController(viewManager.getDoubleMapController().getFirstControlController())
                .statisticsController(viewManager.getDoubleMapController().getFirstStatisticsController())
                .animalDetailsController(viewManager.getDoubleMapController().getFirstAnimalDetailsController())
                .settings(settings)
                .rand(rand)
                .build();

        secondSimulation = SimulationRunner.builder()
                .mapController(viewManager.getDoubleMapController().getSecondMapController())
                .controlController(viewManager.getDoubleMapController().getSecondControlController())
                .statisticsController(viewManager.getDoubleMapController().getSecondStatisticsController())
                .animalDetailsController(viewManager.getDoubleMapController().getSecondAnimalDetailsController())
                .settings(settings)
                .rand(rand)
                .build();

        resizeDoubleMap();
    }

    private void resizeSingleMap() {
        resizingTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    viewManager.getSingleMapController().setMapSize();
                    singleSimulation.draw();
                    resizingTimeline = null;
                })
        );
        resizingTimeline.play();
    }

    private void resizeDoubleMap() {
        resizingTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> {
                    viewManager.getDoubleMapController().setMapsSize();
                    firstSimulation.draw();
                    secondSimulation.draw();
                    resizingTimeline = null;
                })
        );
        resizingTimeline.play();
    }

    private void prepareEventHandlers(Rand rand) {
        viewManager.getMenu().onSingleSimulationChoice(settings -> createSingleSimulation(settings, rand));
        viewManager.getMenu().onDoubleSimulationChoice(settings -> createDoubleSimulation(settings, rand));
        viewManager.getSingleMapController().onMenu(this::onMenu);
        viewManager.getDoubleMapController().onMenu(this::onMenu);
    }

    public static AppRunner create(ViewManager viewManager, Rand rand) {
        var runner = new AppRunner(viewManager);
        runner.prepareEventHandlers(rand);
        return runner;
    }
}
