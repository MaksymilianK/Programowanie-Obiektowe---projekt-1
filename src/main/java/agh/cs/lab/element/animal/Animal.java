package agh.cs.lab.element.animal;

import agh.cs.lab.element.Entity;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.shared.Direction;

import java.util.HashSet;
import java.util.Set;

public class Animal extends Entity {

    private final Gene genes;
    private final Set<AnimalObserver> observers = new HashSet<>();

    private Vector2d position;
    private Direction orientation;
    private int energy;

    private Animal(int id, Vector2d position, Gene genes, Direction orientation, int energy) {
        super(id);
        this.genes = genes;
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
    }

    public void addObserver(AnimalObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(AnimalObserver observer) {
        observers.remove(observer);
    }

    public void kill() {
        observers.forEach(obs -> obs.onAnimalDead(this));
    }

    public void turn(Direction newOrientation) {
        var prevOrientation = orientation;
        orientation = newOrientation;

        observers.forEach(obs -> obs.onAnimalTurned(this, prevOrientation));
    }

    public void move(Vector2d newPosition) {
        var prevPosition = position;
        position = newPosition;

        observers.forEach(obs -> obs.onAnimalMoved(this, prevPosition));
    }

    public Gene getGenes() {
        return genes;
    }

    public Direction getOrientation() {
        return orientation;
    }

    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
        observers.forEach(obs -> obs.onEnergyChanged(this, this.energy - energy));
    }

    public void subtractEnergy(int energy) {
        this.energy -= energy;
        observers.forEach(obs -> obs.onEnergyChanged(this, this.energy + energy));
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public static Animal create(int id, Vector2d position, Gene genes, Direction orientation, int energy,
                                Set<AnimalObserver> observers) {
        var animal = new Animal(id, position, genes, orientation, energy);

        observers.forEach(obs -> {
            obs.onAnimalCreated(animal);
            animal.addObserver(obs);
        });

        return animal;
    }

    public static Animal giveBirth(int id, Vector2d position, Gene genes, Direction orientation, int energy,
                                   Animal parent1, Animal parent2, Set<AnimalObserver> observers) {
        var animal = new Animal(id, position, genes, orientation, energy);

        observers.forEach(obs -> {
            obs.onAnimalBorn(animal, parent1, parent2);
            animal.addObserver(obs);
        });

        return animal;
    }
}