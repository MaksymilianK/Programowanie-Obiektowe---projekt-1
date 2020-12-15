package agh.cs.lab.statistics;

import agh.cs.lab.element.animal.Animal;

import java.util.HashMap;
import java.util.Map;

public class LivingAnimalsChildrenCounter {

    private final Map<Animal, Integer> childrenCounter = new HashMap<>();

    private int totalChildren = 0;

    public void addAnimal(Animal animal) {
        childrenCounter.put(animal, 0);
    }

    public void addChild(Animal child, Animal parent1, Animal parent2) {
        childrenCounter.put(child, 0);

        addChildToParent(parent1);
        addChildToParent(parent2);
    }

    public void addChildToParent(Animal parent) {
        if (childrenCounter.containsKey(parent)) {
            childrenCounter.put(parent, childrenCounter.get(parent) + 1);
        }

        totalChildren++;
    }

    public void removeAnimal(Animal animal) {
        totalChildren -= childrenCounter.remove(animal);
    }

    public float getAverageChildren() {
        return (float) childrenCounter.size() / (float) totalChildren;
    }
}
