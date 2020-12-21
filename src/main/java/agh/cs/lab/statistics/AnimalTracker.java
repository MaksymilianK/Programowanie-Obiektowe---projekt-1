package agh.cs.lab.statistics;

import agh.cs.lab.element.animal.Animal;

import java.util.HashSet;
import java.util.Set;

public class AnimalTracker {

    private final Animal trackedAnimal;
    private final Set<Animal> livingDescendants = new HashSet<>();

    private int finishAfter;
    private Runnable onFinish;
    private int epochsPassed = 0;
    private int children = 0;
    private int descendants = 0;
    private boolean isDead = false;

    public AnimalTracker(Animal trackedAnimal) {
        this.trackedAnimal = trackedAnimal;
    }

    public void start(int finishAfter, Runnable onFinish) {
        if (isTracking()) {
            throw new SimulationStatisticsException("A track method of animal tracker can be called only once");
        }

        this.finishAfter = finishAfter;
        this.onFinish = onFinish;
    }

    public void stop() {
        onFinish = null;
    }

    public void reset() {
        stop();
        epochsPassed = 0;
        children = 0;
        descendants = 0;
    }

    public boolean isTracking() {
        return onFinish != null;
    }

    public int getChildren() {
        return children;
    }

    public int getDescendants() {
        return descendants;
    }

    public void nextEpoch() {
        if (!isTracking()) {
            return;
        }

        if (++epochsPassed == finishAfter) {
            onFinish.run();
        }
    }

    public void onAnimalBorn(Animal animal, Animal parent1, Animal parent2) {
        if (isDescendant(parent1, parent2)) {
            livingDescendants.add(animal);
            descendants++;

            if (isChild(parent1, parent2)) {
                children++;
            }
        }
    }

    public boolean onAnimalDead(Animal animal) {
        if (animal == trackedAnimal) {
            isDead = true;
            return true;
        } else {
            livingDescendants.remove(animal);
            return false;
        }
    }

    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    public boolean isDead() {
        return isDead;
    }

    private boolean isChild(Animal parent1, Animal parent2) {
        return trackedAnimal.equals(parent1) || trackedAnimal.equals(parent2);
    }

    private boolean isDescendant(Animal parent1, Animal parent2) {
        return isChild(parent1, parent2) || livingDescendants.contains(parent1) || livingDescendants.contains(parent2);
    }
}
