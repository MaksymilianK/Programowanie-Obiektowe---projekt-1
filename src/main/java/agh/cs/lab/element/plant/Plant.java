package agh.cs.lab.element.plant;

import agh.cs.lab.element.Entity;
import agh.cs.lab.shared.Vector2d;

import java.util.HashSet;
import java.util.Set;

public class Plant extends Entity {

    private final Vector2d position;
    private final Set<PlantObserver> observers = new HashSet<>();

    private Plant(int id, Vector2d position) {
        super(id);
        this.position = position;
    }

    public void addObserver(PlantObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PlantObserver observer) {
        observers.remove(observer);
    }

    public void eat() {
        observers.forEach(obs -> obs.onPlantEaten(this));
    }

    public static Plant create(int id, Vector2d position, Set<PlantObserver> observers) {
        var plant = new Plant(id, position);

        observers.forEach(obs -> {
            obs.onPlantCreated(plant);
            plant.addObserver(obs);
        });

        return plant;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }
}
