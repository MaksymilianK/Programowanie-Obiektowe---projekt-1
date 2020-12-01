package agh.cs.lab.view;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalObserver;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.element.plant.PlantObserver;
import agh.cs.lab.shared.MapDirection;
import agh.cs.lab.shared.Vector2d;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class WorldMapView implements AnimalObserver, PlantObserver {

    @FXML
    private Canvas map;

    public Canvas draw() {
        var ctx = map.getGraphicsContext2D();
        map.setWidth(100);
        map.setHeight(100);
        ctx.drawImage(new Image("animal.png"), 0, 0, 96, 96);
        System.out.println(map.toString());
        return map;
    }

    @Override
    public void onAnimalBorn(Animal animal) {

    }

    @Override
    public void onAnimalDead(Animal animal) {

    }

    @Override
    public void onAnimalTurned(Animal animal, MapDirection prevOrientation) {

    }

    @Override
    public void onAnimalMoved(Animal animal, Vector2d prevPosition) {

    }

    @Override
    public void onEnergyChanged(Animal animal, int prevEnergy) {

    }

    @Override
    public void onPlantSet(Plant plant) {

    }

    @Override
    public void onPlantEaten(Plant plant) {

    }
}
