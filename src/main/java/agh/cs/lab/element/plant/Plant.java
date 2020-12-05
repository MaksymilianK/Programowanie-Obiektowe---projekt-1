package agh.cs.lab.element.plant;

import agh.cs.lab.element.Entity;
import agh.cs.lab.shared.Vector2d;

import java.util.Set;

public class Plant extends Entity {

    private final Vector2d position;
    private final Set<PlantObserver> observers;

    Plant(int id, Vector2d position, Set<PlantObserver> observers) {
        super(id);
        this.position = position;
        this.observers = observers;

        observers.forEach(obs -> obs.onPlantCreated(this));
    }

    public void eat() {
        observers.forEach(obs -> obs.onPlantEaten(this));
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }
}
