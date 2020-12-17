package agh.cs.lab.runner;

import agh.cs.lab.element.EntityFactoryFacade;
import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalFactory;
import agh.cs.lab.element.animal.GenesFactory;
import agh.cs.lab.element.plant.PlantFactory;
import agh.cs.lab.engine.SimulationEngine;
import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.tracker.AnimalTracker;
import agh.cs.lab.statistics.SimulationStatisticsManager;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.view.AnimalDetailsController;
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

    private final MapController mapView;
    private final SimulationStatisticsController statisticsView;
    private final AnimalDetailsController animalDetailsView;
    private final SimulationEngine engine;
    private final SimulationStatisticsManager statistics;

    private Timeline animation;
    private AnimalTracker animalTracker;

    private SimulationRunner(MapController mapView, SimulationStatisticsController statisticsView,
                             AnimalDetailsController animalDetailsView, SimulationEngine engine,
                             SimulationStatisticsManager statistics) {
        this.mapView = mapView;
        this.statisticsView = statisticsView;
        this.animalDetailsView = animalDetailsView;
        this.engine = engine;
        this.statistics = statistics;
    }

    public void pause() {
        if (!animation.getStatus().equals(Animation.Status.RUNNING)) {
            throw new SimulationRunnerException("Cannot stop a simulation that is not running");
        }
        animation.pause();
    }

    public void resume() {
        if (animation.getStatus().equals(Animation.Status.RUNNING)) {
            throw new SimulationRunnerException("Cannot resume a running simulation");
        }
        animation.play();
    }

    public void setSpeed(double speed) {
        if (animation != null && animation.getStatus().equals(Animation.Status.RUNNING)) {
            throw new SimulationRunnerException("Cannot change speed of a running simulation");
        }
        animation = new Timeline(
                new KeyFrame(Duration.seconds(MAX_SPEED + MIN_SPEED - speed), event -> firstPart()),
                new KeyFrame(Duration.seconds((MAX_SPEED + MIN_SPEED - speed) * 2), event -> secondPart()),
                new KeyFrame(Duration.seconds((MAX_SPEED + MIN_SPEED - speed) * 3), event -> thirdPart()),
                new KeyFrame(Duration.seconds((MAX_SPEED + MIN_SPEED - speed) * 4), event -> fourthPart())
        );
        animation.setDelay(Duration.seconds((MAX_SPEED + MIN_SPEED - speed) * 5));
        animation.setCycleCount(Timeline.INDEFINITE);
    }

    public void init() {
        mapView.onClick(this::tryTrackAnimal);
        animalDetailsView.onCheck(this::trackerAnimalSnapshot);
        animalDetailsView.onFinish(this::finishTracking);
    }

    private void trackerAnimalSnapshot(int epoch) {
        var snapshot = animalTracker.getSnapshot(epoch);
        animalDetailsView.update(snapshot.getChildren(), snapshot.getDescendants());
    }

    private void finishTracking() {
        animalTracker = null;
        animalDetailsView.reset();
    }

    public void tryTrackAnimal(Vector2d position) {
        if (animation.getStatus().equals(Animation.Status.RUNNING)) {
            return;
        }

        engine.getAnimalAt(position).ifPresent(animal -> {
            animalTracker = new AnimalTracker(
                    animal, () -> animalDetailsView.onAnimalDeath(engine.getCurrentEpoch())
            );
            animalDetailsView.reset();
            animalDetailsView.makeActive(animal.getGenes());
        });
    }

    public static SimulationRunner prepare(SimulationSettings settings, MapController mapView,
                                           SimulationStatisticsController statisticsView, AnimalDetailsController animalDetailsView,
                                           Rand rand) {
        var statistics = new SimulationStatisticsManager();
        var engine = buildSimulation(settings, statistics, rand);
        prepareView(mapView, statisticsView, engine, statistics);

        var runner = new SimulationRunner(mapView, statisticsView, animalDetailsView, engine, statistics);
        runner.setSpeed(DEFAULT_SPEED);

        return runner;
    }

    public void afterPreparation() {
        mapView.redrawFields(engine.getMapBorders(), engine.getMapJungleBorders());
        mapView.redrawAnimals(engine.getAnimalsWithTopEnergy());
        mapView.drawPlants(engine.getPlants());
    }

    private void firstPart() {
        engine.turnAnimals();

        mapView.redrawAnimals(engine.getAnimalsWithTopEnergy());
        mapView.drawPlants(engine.getPlants());
        statisticsView.setLivingAnimals(statistics.getLivingAnimals());
        statisticsView.setMostCommonGenes(statistics.getMostCommonGenes(
                SimulationStatisticsController.MOST_COMMON_GENES_MAX
        ));
        statisticsView.setAverageEnergy(statistics.getAverageEnergy());
        statisticsView.setAverageLifeTime(statistics.getAverageLifeTime());
    }

    private void secondPart() {
        var deadAnimals = engine.checkAnimalsEnergy();
        if (animalTracker != null) {
            deadAnimals.forEach(animal -> {
                if (animalTracker.notifyAnimalDeath(animal)) {
                    animalDetailsView.onAnimalDeath(engine.getCurrentEpoch());
                }
            });
        }
        engine.moveAnimals();

        mapView.redrawAnimals(engine.getAnimalsWithTopEnergy());
        mapView.drawPlants(engine.getPlants());
    }

    private void thirdPart() {
        engine.feedAnimals();
        var childrenAndParents = engine.procreate();
        if (animalTracker != null) {
            childrenAndParents.forEach(trio -> animalTracker.notifyAnimalBorn(trio.first, trio.second, trio.third));
        }

        mapView.redrawAnimals(engine.getAnimalsWithTopEnergy());
        mapView.drawPlants(engine.getPlants());
        statisticsView.setLivingAnimals(statistics.getLivingAnimals());
        statisticsView.setLivingPlants(statistics.getLivingPlants());
        statisticsView.setMostCommonGenes(statistics.getMostCommonGenes(
                SimulationStatisticsController.MOST_COMMON_GENES_MAX
        ));
        statisticsView.setAverageEnergy(statistics.getAverageEnergy());
        statisticsView.setAverageChildren(statistics.getAverageChildren());
    }

    private void fourthPart() {
        engine.addPlants();

        mapView.redrawAnimals(engine.getAnimalsWithTopEnergy());
        mapView.drawPlants(engine.getPlants());
        statisticsView.setLivingPlants(statistics.getLivingPlants());
        engine.nextEpoch();
        if (animalTracker != null) {
            animalTracker.nextEpoch();
        }
    }

    private static SimulationEngine buildSimulation(SimulationSettings settings, SimulationStatisticsManager statistics,
                                                    Rand rand) {
        var map = WorldMap.create(settings.getMapWidth(), settings.getMapHeight(), settings.getJungleRatio());
        var genesFactory = new GenesFactory(rand);
        var animalFactory = new AnimalFactory(settings.getStartEnergy(), map, statistics, rand, genesFactory);
        var plantFactory = new PlantFactory(map, statistics);
        var entityFactory = new EntityFactoryFacade(animalFactory, plantFactory);

        var engine = new SimulationEngine(settings, map, entityFactory, rand);
        engine.init(START_ANIMALS_NUMBER);
        return engine;
    }

    private static void prepareView(MapController mapView, SimulationStatisticsController statisticsView,
                                    SimulationEngine engine, SimulationStatisticsManager statistics) {
        mapView.redrawAnimals(engine.getAnimalsWithTopEnergy());
        mapView.drawPlants(engine.getPlants());

        statisticsView.setLivingAnimals(statistics.getLivingAnimals());
        statisticsView.setLivingPlants(statistics.getLivingPlants());
        statisticsView.setMostCommonGenes(
                statistics.getMostCommonGenes(SimulationStatisticsController.MOST_COMMON_GENES_MAX)
        );
        statisticsView.setAverageEnergy(statistics.getAverageEnergy());
        statisticsView.setAverageLifeTime(statistics.getAverageLifeTime());
        statisticsView.setAverageChildren(statistics.getAverageChildren());
    }
}
