package agh.cs.lab.element.animal;

import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Vector2d;

public interface AnimalObserver {

    void onAnimalCreated(Animal animal);
    void onAnimalBorn(Animal animal, Animal parent1, Animal parent2);
    void onAnimalDead(Animal animal);
    void onAnimalTurned(Animal animal, Direction prevOrientation);
    void onAnimalMoved(Animal animal, Vector2d prevPosition);
    void onEnergyChanged(Animal animal, int prevEnergy);
}
