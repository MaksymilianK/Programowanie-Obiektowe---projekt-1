package agh.cs.lab;

import agh.cs.lab.element.EntityFactoryFacade;
import agh.cs.lab.element.animal.AnimalFactory;
import agh.cs.lab.element.animal.GenesFactory;
import agh.cs.lab.element.plant.PlantFactory;
import agh.cs.lab.engine.SimulationEngine;
import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.engine.SimulationStatistics;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.view.AppView;
import agh.cs.lab.view.WorldMapView;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Paths;

public class World extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        var appView = new AppView();

        var view = appView.set();

        var rand = new Rand();
        var mapper = new ObjectMapper();
        var settings = mapper.readValue(getClass().getResource("/settings.json"), SimulationSettings.class);
        var map = WorldMap.create(settings.getMapWidth(), settings.getMapHeight(), settings.getJungleRatio());
        var statistics = new SimulationStatistics();
        var genesFactory = new GenesFactory(rand);
        var animalFactory = new AnimalFactory(settings.getStartEnergy(), map, statistics, rand, genesFactory);
        var plantFactory = new PlantFactory(map, statistics);
        var entityFactoryFacade = new EntityFactoryFacade(animalFactory, plantFactory);
        var engine = new SimulationEngine(settings, map, entityFactoryFacade, rand);

        var pane = new ScrollPane();

        var scene = new Scene(pane);

        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        var m = view.draw(map.getMapBorders(), map.getJungleBorders(),
                primaryStage.getWidth(), primaryStage.getHeight());
        pane.setContent(m);

        engine.init(10);

        view.drawAnimals(map.getAnimalsWithTopEnergy());
        view.drawPlants(map.getPlants());

        var turn = new Timeline(new KeyFrame(Duration.seconds(0.04), (ActionEvent event1) -> {
            engine.newEpoch();
            engine.checkAnimalsEnergy();
            engine.turnAnimals();
            view.clearEntities();
            view.drawAnimals(map.getAnimalsWithTopEnergy());
            view.drawPlants(map.getPlants());
        }));
        turn.setCycleCount(Timeline.INDEFINITE);

        var move = new Timeline(new KeyFrame(Duration.seconds(0.04), (ActionEvent event1) -> {
            engine.moveAnimals();
            view.clearEntities();
            view.drawAnimals(map.getAnimalsWithTopEnergy());
            view.drawPlants(map.getPlants());
        }));
        move.setCycleCount(Timeline.INDEFINITE);

        var feedAndProcreate = new Timeline(new KeyFrame(Duration.seconds(0.04), (ActionEvent event1) -> {
            engine.feedAnimals();
            engine.procreate();
            view.clearEntities();
            view.drawAnimals(map.getAnimalsWithTopEnergy());
            view.drawPlants(map.getPlants());
        }));
        feedAndProcreate.setCycleCount(Timeline.INDEFINITE);

        var addPlants = new Timeline(new KeyFrame(Duration.seconds(0.04), (ActionEvent event1) -> {
            engine.addPlants();
            view.clearEntities();
            view.drawAnimals(map.getAnimalsWithTopEnergy());
            view.drawPlants(map.getPlants());
        }));
        addPlants.setCycleCount(Timeline.INDEFINITE);

        turn.play();

        new Timeline(new KeyFrame(Duration.seconds(0.01), (ActionEvent event1) -> {
            move.play();
        })).play();

        new Timeline(new KeyFrame(Duration.seconds(0.02), (ActionEvent event1) -> {
            feedAndProcreate.play();
        })).play();

        new Timeline(new KeyFrame(Duration.seconds(0.03), (ActionEvent event1) -> {
            addPlants.play();
        })).play();
    }

    public static void main(String[] args) {
        launch();
    }
}