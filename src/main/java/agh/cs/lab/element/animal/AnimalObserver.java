package agh.cs.lab.element.animal;

import agh.cs.lab.shared.MapDirection;
import agh.cs.lab.shared.Vector2d;

public interface AnimalObserver {

    void onAnimalBorn(Animal animal);
    void onAnimalDead(Animal animal);
    void onAnimalTurned(Animal animal, MapDirection prevOrientation);
    void onAnimalMoved(Animal animal, Vector2d prevPosition);
    void onEnergyChanged(Animal animal, int prevEnergy);
}
