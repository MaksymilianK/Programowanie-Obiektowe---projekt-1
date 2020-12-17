package agh.cs.lab.tracker;

import agh.cs.lab.element.animal.Animal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnimalTracker {

    private final Animal trackedAnimal;
    private final Set<Animal> livingDescendants = new HashSet<>();
    private final List<AnimalTrackerSnapshot> snapshots = new ArrayList<>();
    private final Runnable onAnimalDeath;

    private int childrenCounter = 0;
    private int descendantsCounter = 0;

    {
        snapshots.add(new AnimalTrackerSnapshot(0, 0));
    }

    public AnimalTracker(Animal trackedAnimal, Runnable onAnimalDeath) {
        this.trackedAnimal = trackedAnimal;
        this.onAnimalDeath = onAnimalDeath;
    }

    public void nextEpoch() {
        snapshots.add(new AnimalTrackerSnapshot(childrenCounter, descendantsCounter));
    }

    public void notifyAnimalBorn(Animal animal, Animal parent1, Animal parent2) {
        if (isDescendant(parent1, parent2)) {
            livingDescendants.add(animal);
            descendantsCounter++;
        }

        if (isChild(parent1, parent2)) {
            childrenCounter++;
        }
    }

    public boolean notifyAnimalDeath(Animal animal) {
        if (animal == trackedAnimal) {
            return true;
        } else {
            livingDescendants.remove(animal);
            return false;
        }
    }

    public AnimalTrackerSnapshot getSnapshot(int epoch) {
        if (epoch >= this.snapshots.size()) {
            return new AnimalTrackerSnapshot(-1, -1);
        }
        return snapshots.get(epoch);
    }

    private boolean isChild(Animal parent1, Animal parent2) {
        return trackedAnimal.equals(parent1) || trackedAnimal.equals(parent2);
    }

    private boolean isDescendant(Animal parent1, Animal parent2) {
        return isChild(parent1, parent2) || livingDescendants.contains(parent1) || livingDescendants.contains(parent2);
    }
}
