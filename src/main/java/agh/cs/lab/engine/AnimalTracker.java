package agh.cs.lab.engine;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalObserver;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Vector2d;

import java.util.HashSet;
import java.util.Set;

public class AnimalTracker implements AnimalObserver {

    private final Animal trackedAnimal;
    private final Set<Animal> livingDescendants = new HashSet<>();
    private final int epochBorn;

    private int childrenCounter = 0;
    private int descendantsCounter = 0;
    private int epochDied = -1;

    public AnimalTracker(Animal trackedAnimal, int currentEpoch) {
        this.trackedAnimal = trackedAnimal;
        this.epochBorn = currentEpoch - trackedAnimal.getLengthOfLife();
    }

    @Override
    public void onAnimalCreated(Animal animal) {
        //do nothing
    }

    @Override
    public void onAnimalBorn(Animal animal, Animal parent1, Animal parent2) {
        if (isDescendant(parent1, parent2)) {
            livingDescendants.add(animal);
            descendantsCounter++;
        }

        if (isChild(parent1, parent2)) {
            childrenCounter++;
        }
    }

    @Override
    public void onAnimalDead(Animal animal) {
        livingDescendants.remove(animal);
    }

    @Override
    public void onAnimalTurned(Animal animal, Direction prevOrientation) {
        //do nothing
    }

    @Override
    public void onAnimalMoved(Animal animal, Vector2d prevPosition) {
        //do nothing
    }

    @Override
    public void onEnergyChanged(Animal animal, int prevEnergy) {
        //do nothing
    }

    public int getChildren() {
        return childrenCounter;
    }

    public int getDescendants() {
        return descendantsCounter;
    }

    public int getEpochDied() {
        return epochDied;
    }

    private boolean isChild(Animal parent1, Animal parent2) {
        return trackedAnimal.equals(parent1) || trackedAnimal.equals(parent2);
    }

    private boolean isDescendant(Animal parent1, Animal parent2) {
        return isChild(parent1, parent2) || livingDescendants.contains(parent1) || livingDescendants.contains(parent2);
    }
}
