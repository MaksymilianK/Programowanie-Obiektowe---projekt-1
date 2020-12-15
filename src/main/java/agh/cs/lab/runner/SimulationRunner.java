package agh.cs.lab.runner;

import agh.cs.lab.element.EntityFactoryFacade;
import agh.cs.lab.element.animal.AnimalFactory;
import agh.cs.lab.element.animal.GenesFactory;
import agh.cs.lab.element.plant.PlantFactory;
import agh.cs.lab.engine.SimulationEngine;
import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.statistics.SimulationStatistics;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.view.AnimalDetailsView;
import agh.cs.lab.view.SimulationStatisticsView;
import agh.cs.lab.view.WorldMapView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SimulationRunner {

    private static final double SPEED_CONST = 1.0d / 200.0d;

    public static final double DEFAULT_SPEED = 50.0d * SPEED_CONST;
    public static final double MIN_SPEED = 1.0d * SPEED_CONST;
    public static final double MAX_SPEED = 100.0d * SPEED_CONST;

    private final WorldMapView mapView;
    private final SimulationStatisticsView statisticsView;
    private final AnimalDetailsView animalDetailsView;
    private final SimulationEngine engine;
    private final SimulationStatistics statistics;

    private Timeline animation;

    private SimulationRunner(WorldMapView mapView, SimulationStatisticsView statisticsView,
                             AnimalDetailsView animalDetailsView, SimulationEngine engine,
                             SimulationStatistics statistics) {
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
        if (animation.getStatus().equals(Animation.Status.RUNNING)) {
            throw new SimulationRunnerException("Cannot change speed of a running simulation");
        }
        animation = new Timeline(
                new KeyFrame(Duration.seconds(speed), event -> firstPart()),
                new KeyFrame(Duration.seconds(speed * 2), event -> secondPart()),
                new KeyFrame(Duration.seconds(speed * 3), event -> thirdPart()),
                new KeyFrame(Duration.seconds(speed * 4), event -> fourthPart())
        );
        animation.setCycleCount(Timeline.INDEFINITE);
    }

    public static SimulationRunner prepare(SimulationSettings settings, WorldMapView mapView,
                                           SimulationStatisticsView statisticsView, AnimalDetailsView animalDetailsView,
                                           Rand rand) {
        var statistics = new SimulationStatistics();
        var engine = buildSimulation(settings, statistics, rand);
        prepareView(mapView, statisticsView, engine, statistics);

        var runner = new SimulationRunner(mapView, statisticsView, animalDetailsView, engine, statistics);
        runner.setSpeed(DEFAULT_SPEED);
        return runner;
    }

    private void firstPart() {
        engine.newEpoch();
        engine.checkAnimalsEnergy();
        engine.turnAnimals();

        mapView.drawAnimals(engine.getAnimalsWithTopEnergy());
        statisticsView.setLivingAnimals(statistics.getLivingAnimals());
        statisticsView.setMostCommonGenes(statistics.getMostCommonGenes());
        statisticsView.setAverageEnergy(statistics.getAverageEnergy());
        statisticsView.setAverageLifeTime(statistics.getAverageLifeTime());
    }

    private void secondPart() {
        engine.moveAnimals();

        mapView.drawAnimals(engine.getAnimalsWithTopEnergy());
        mapView.drawPlants(engine.getPlants());
    }

    private void thirdPart() {
        engine.feedAnimals();
        engine.procreate();

        mapView.drawAnimals(engine.getAnimalsWithTopEnergy());
        statisticsView.setLivingAnimals(statistics.getLivingAnimals());
        statisticsView.setLivingPlants(statistics.getLivingPlants());
        statisticsView.setMostCommonGenes(statistics.getMostCommonGenes());
        statisticsView.setAverageEnergy(statistics.getAverageEnergy());
        statisticsView.setAverageChildren(statistics.getAverageChildren());
    }

    private void fourthPart() {
        var newPlants = engine.addPlants();

        mapView.drawPlants(newPlants);
        statisticsView.setLivingPlants(statistics.getLivingPlants());
    }

    private static SimulationEngine buildSimulation(SimulationSettings settings, SimulationStatistics statistics,
                                                    Rand rand) {
        var map = WorldMap.create(settings.getMapWidth(), settings.getMapHeight(), settings.getJungleRatio());
        var genesFactory = new GenesFactory(rand);
        var animalFactory = new AnimalFactory(settings.getStartEnergy(), map, statistics, rand, genesFactory);
        var plantFactory = new PlantFactory(map, statistics);
        var entityFactory = new EntityFactoryFacade(animalFactory, plantFactory);

        return new SimulationEngine(settings, map, entityFactory, rand);
    }

    private static void prepareView(WorldMapView mapView, SimulationStatisticsView statisticsView,
                                    SimulationEngine engine, SimulationStatistics statistics) {
        mapView.drawAnimals(engine.getAnimalsWithTopEnergy());
        mapView.drawPlants(engine.getPlants());

        statisticsView.setLivingAnimals(statistics.getLivingAnimals());
        statisticsView.setLivingPlants(statistics.getLivingPlants());
        statisticsView.setMostCommonGenes(statistics.getMostCommonGenes());
        statisticsView.setAverageEnergy(statistics.getAverageEnergy());
        statisticsView.setAverageLifeTime(statistics.getAverageLifeTime());
        statisticsView.setAverageChildren(statistics.getAverageChildren());
    }
}
