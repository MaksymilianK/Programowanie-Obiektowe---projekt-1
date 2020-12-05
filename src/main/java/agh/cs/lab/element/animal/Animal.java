package agh.cs.lab.element.animal;

import agh.cs.lab.element.Entity;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.shared.Direction;

import java.util.Set;

public class Animal extends Entity {

    private final Gene genes;
    private final Set<AnimalObserver> observers;

    private Vector2d position;
    private Direction orientation;
    private int energy;

    private Animal(int id, Set<AnimalObserver> observers, Vector2d position, Gene genes, Direction orientation,
                   int energy) {
        super(id);
        this.genes = genes;
        this.observers = observers;
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
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

    public static Animal create(int id, Set<AnimalObserver> observers, Vector2d position, Gene genes,
                                Direction orientation, int energy) {
        var animal = new Animal(id, observers, position, genes, orientation, energy);
        observers.forEach(obs -> obs.onAnimalCreated(animal));
        return animal;
    }

    public static Animal giveBirth(int id, Set<AnimalObserver> observers, Vector2d position, Gene genes,
                                   Direction orientation, int energy, Animal parent1, Animal parent2) {
        var animal = new Animal(id, observers, position, genes, orientation, energy);
        observers.forEach(obs -> obs.onAnimalBorn(animal, parent1, parent2));
        return animal;
    }
}