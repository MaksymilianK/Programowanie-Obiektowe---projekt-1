package agh.cs.lab.runner;

import agh.cs.lab.element.EntityFactoryFacade;
import agh.cs.lab.element.animal.AnimalFactory;
import agh.cs.lab.element.animal.GeneFactory;
import agh.cs.lab.element.plant.PlantFactory;
import agh.cs.lab.engine.SimulationEngine;
import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.statistics.AnimalTracker;
import agh.cs.lab.statistics.SimulationStatisticsManager;
import agh.cs.lab.engine.map.WorldMap;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.statistics.StatisticsSaver;
import agh.cs.lab.view.AnimalTrackerController;
import agh.cs.lab.view.SimulationControlController;
import agh.cs.lab.view.SimulationStatisticsController;
import agh.cs.lab.view.MapController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SimulationRunner {

    private static final double SPEED_CONST = 1.0d / 200.0d;

    public static final double DEFAULT_SPEED = 50.0d * SPEED_CONST;
    public static final double MIN_SPEED = 1.0d * SPEED_CONST;
    public static final double MAX_SPEED = 100.0d * SPEED_CONST;
    public static final int START_ANIMALS_NUMBER = 5;

    private final MapController mapController;
    private final SimulationControlController controlController;
    private final SimulationStatisticsController statisticsController;
    private final AnimalTrackerController animalDetailsController;
    private final SimulationEngine engine;
    private final SimulationStatisticsManager statistics;

    private Timeline timeline;
    private AnimalTracker animalTracker;

    private SimulationRunner(MapController mapView, SimulationControlController controlController,
                             SimulationStatisticsController statisticsController,
                             AnimalTrackerController animalDetailsController, SimulationEngine engine,
                             SimulationStatisticsManager statistics) {
        this.mapController = mapView;
        this.controlController = controlController;
        this.statisticsController = statisticsController;
        this.animalDetailsController = animalDetailsController;
        this.engine = engine;
        this.statistics = statistics;
    }

    public void finish() {
        timeline.stop();
        timeline = null;
    }

    public void draw() {
        //Must wait a fraction of second, because resizing does not occur immediately after showing a scene
        (new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    mapController.redrawFields(
                            engine.getMapBorders(), engine.getMapJungleBorders(), engine.getSettings().getStartEnergy()
                    );
                    mapController.redrawAnimals(engine.getAnimalsWithTopEnergy());
                    mapController.drawPlants(engine.getPlants());
                })
        )).play();
    }

    private void trySelectAnimal(Vector2d position) {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        engine.getHealthiestAnimalAt(position).ifPresent(animal -> {
            if (animalTracker != null) {
                if (!animalTracker.isDead()) {
                    mapController.redrawAnimal(animalTracker.getTrackedAnimal());
                }

                animalTracker.stop();
                animalDetailsController.reset();
            }

            animalTracker = new AnimalTracker(animal);
            animalDetailsController.notifySelected(animal.getGene());
            mapController.drawTrackedAnimal(animal);
        });
    }

    private void startTracking(Integer finishAfter) {
        animalTracker.start(finishAfter, this::finishTracking);
        animalDetailsController.notifyTrackingStarted();
    }

    private void finishTracking() {
        pause();
        animalDetailsController.notifyTrackingFinished(animalTracker.getChildren(), animalTracker.getDescendants());
        animalTracker.reset();
    }

    private void interruptTracking() {
        if (!animalTracker.isDead()) {
            mapController.redrawAnimal(
                    engine.getHealthiestAnimalAt(animalTracker.getTrackedAnimal().getPosition()).get()
            );
        }

        animalTracker.stop();;
        animalTracker = null;
        animalDetailsController.notifyTrackingInterrupted();
    }

    private void showMostCommonGenes() {
        mapController.redrawAnimalsWithMostCommonGenes(engine.getAnimalsWithGenes(statistics.getMostCommonGenes()));
    }

    private void pause() {
        controlController.setRunning(false);
        statisticsController.setRunning(false);
        animalDetailsController.setRunning(false);
        timeline.pause();
    }

    private void resume(double speed) {
        setSpeed(speed);
        controlController.setRunning(true);
        statisticsController.setRunning(true);
        animalDetailsController.setRunning(true);
        timeline.play();
    }

    private void setSpeed(double speed) {
        if (timeline != null && timeline.getStatus().equals(Animation.Status.RUNNING)) {
            throw new SimulationRunnerException("Cannot change speed while a simulation is running");
        }
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(MAX_SPEED + MIN_SPEED - speed), event -> firstPart()),
                new KeyFrame(Duration.seconds((MAX_SPEED + MIN_SPEED - speed) * 2), event -> secondPart()),
                new KeyFrame(Duration.seconds((MAX_SPEED + MIN_SPEED - speed) * 3), event -> thirdPart()),
                new KeyFrame(Duration.seconds((MAX_SPEED + MIN_SPEED - speed) * 4), event -> fourthPart())
        );
        timeline.setDelay(Duration.seconds((MAX_SPEED + MIN_SPEED - speed) * 5));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void saveStatistics() {
        StatisticsSaver.saveTotal(engine.getCurrentEpoch(), statistics.getSnapshot());
    }

    private void firstPart() {
        var deadAnimals = engine.removeDeadAnimals();
        engine.turnAnimals();

        if (animalTracker != null) {
            deadAnimals.forEach(dead -> {
                if (animalTracker.onAnimalDead(dead)) {
                    animalDetailsController.notifyDead(engine.getCurrentEpoch());
                }
            });
        }

        drawAnimals();

        statisticsController.setAverageEnergy(statistics.getAverageEnergy());
        statisticsController.setAverageChildren(statistics.getAverageChildren());
        statisticsController.setLivingAnimals(statistics.getLivingAnimals());
        statisticsController.setAverageLifeTime(statistics.getAverageLifeTime());
        statisticsController.setMostCommonGenes(statistics.getMostCommonGenes(
                SimulationStatisticsController.MOST_COMMON_GENES_MAX
        ));
    }

    private void secondPart() {
        engine.moveAnimals();

        drawAnimals();

        statisticsController.setAverageEnergy(statistics.getAverageEnergy());
    }

    private void thirdPart() {
        var eatenPlants = engine.feedAnimals();

        drawAnimals();
        mapController.clearPlants(eatenPlants);

        statisticsController.setAverageEnergy(statistics.getAverageEnergy());
        statisticsController.setLivingPlants(statistics.getLivingPlants());
    }

    private void fourthPart() {
        var animals = engine.procreate();
        var newPlants = engine.addPlants();
        engine.nextEpoch();
        statistics.nextEpoch();

        if (animalTracker != null) {
            animalTracker.nextEpoch();

            animals.forEach(childAndParents ->
                    animalTracker.onAnimalBorn(childAndParents.first, childAndParents.second, childAndParents.third)
            );
        }

        drawAnimals();
        mapController.drawPlants(newPlants);
        statisticsController.setCurrentEpoch(engine.getCurrentEpoch());
        statisticsController.setLivingPlants(statistics.getLivingPlants());
    }

    private void drawAnimals() {
        var animals = engine.getAnimalsWithTopEnergy();
        if (animalTracker != null && !animalTracker.isDead()) {
            animals.remove(engine.getHealthiestAnimalAt(animalTracker.getTrackedAnimal().getPosition()).get());
            mapController.redrawAnimals(animals);
            mapController.drawTrackedAnimal(animalTracker.getTrackedAnimal());
        } else {
            mapController.redrawAnimals(animals);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private MapController mapController;
        private SimulationControlController controlController;
        private SimulationStatisticsController statisticsController;
        private AnimalTrackerController animalDetailsController;
        private SimulationSettings settings;
        private Rand rand;

        Builder() {}

        public Builder mapController(MapController mapController) {
            this.mapController = mapController;
            return this;
        }

        public Builder controlController(SimulationControlController controlController) {
            this.controlController = controlController;
            return this;
        }

        public Builder statisticsController(SimulationStatisticsController statisticsController) {
            this.statisticsController = statisticsController;
            return this;
        }

        public Builder animalDetailsController(AnimalTrackerController animalDetailsController) {
            this.animalDetailsController = animalDetailsController;
            return this;
        }

        public Builder settings(SimulationSettings settings) {
            this.settings = settings;
            return this;
        }

        public Builder rand(Rand rand) {
            this.rand = rand;
            return this;
        }

        public SimulationRunner build() {
            if (mapController == null || controlController == null || statisticsController == null
                    || animalDetailsController == null || settings == null || rand == null) {
                throw new SimulationRunnerException("Cannot build a simulation runner - some of fields are missing");
            }

            var statistics = new SimulationStatisticsManager();
            var engine = buildSimulation(settings, statistics, rand);
            prepareView(statistics);

            var runner = new SimulationRunner(mapController, controlController, statisticsController,
                    animalDetailsController, engine, statistics);

            runner.setSpeed(DEFAULT_SPEED);
            prepareEventHandlers(runner);

            return runner;
        }

        private SimulationEngine buildSimulation(SimulationSettings settings, SimulationStatisticsManager statistics,
                                                        Rand rand) {
            var map = WorldMap.create(settings.getMapWidth(), settings.getMapHeight(), settings.getJungleRatio());
            var genesFactory = new GeneFactory(rand);
            var animalFactory = new AnimalFactory(settings.getStartEnergy(), map, statistics, rand, genesFactory);
            var plantFactory = new PlantFactory(map, statistics);
            var entityFactory = new EntityFactoryFacade(animalFactory, plantFactory);

            return SimulationEngine.create(settings, map, entityFactory, rand, START_ANIMALS_NUMBER);
        }

        private void prepareView(SimulationStatisticsManager statistics) {
            statisticsController.setLivingAnimals(statistics.getLivingAnimals());
            statisticsController.setLivingPlants(statistics.getLivingPlants());
            statisticsController.setMostCommonGenes(
                    statistics.getMostCommonGenes(SimulationStatisticsController.MOST_COMMON_GENES_MAX)
            );
            statisticsController.setAverageEnergy(statistics.getAverageEnergy());
            statisticsController.setAverageLifeTime(statistics.getAverageLifeTime());
            statisticsController.setAverageChildren(statistics.getAverageChildren());
        }

        private void prepareEventHandlers(SimulationRunner runner) {
            mapController.onClick(runner::trySelectAnimal);
            controlController.onResume(runner::resume);
            controlController.onPause(runner::pause);
            statisticsController.onMostCommonGenes(runner::showMostCommonGenes);
            statisticsController.onSave(runner::saveStatistics);
            animalDetailsController.onStart(runner::startTracking);
            animalDetailsController.onInterrupt(runner::interruptTracking);
        }
    }
}
