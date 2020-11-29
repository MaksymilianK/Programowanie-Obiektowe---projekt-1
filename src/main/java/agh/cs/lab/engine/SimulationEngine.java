package agh.cs.lab.engine;

import agh.cs.lab.Element.Animal;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.MapDirection;
import agh.cs.lab.shared.Vector2d;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SimulationEngine {

    private final int energyToAdd = 50;
    private final int minProcreationEnergy = 50;
    private final Random rand = new Random();
    private final WorldMap map;

    public SimulationEngine(WorldMap map) {
        this.map = map;
    }

    public void simulateEpoch() {
        var aliveAnimals = checkAnimalsEnergy(map.getAllAnimals());
        moveAnimals(aliveAnimals);
        feedAnimals();
        procreate();
        //addPlants();
        //subtractEnergy(aliveAnimals);
    }

    private Set<Animal> checkAnimalsEnergy(Set<Animal> animals) {
        var alive = new HashSet<Animal>();
        var dead = new HashSet<Animal>();

        animals.forEach(animal -> {
            if (animal.getEnergy() == 0) {
                dead.add(animal);
            } else {
                alive.add(animal);
            }
        });
        map.removeAnimals(dead);

        return alive;
    }

    private void moveAnimals(Set<Animal> alive) {
        alive.forEach(this::turnAnimal);
        map.moveAnimals(alive);
    }

    private void turnAnimal(Animal animal) {
        int turn = rand.nextInt(8);
        var newOrientation = animal.getOrientation();

        for (int i = 0; i < turn; i++) {
            newOrientation = newOrientation.next();
        }
        animal.setOrientation(newOrientation);
    }

    private void feedAnimals() {
        var plantsToRemove = new HashSet<Vector2d>();

        map.getAnimalsToFeed().forEach(animals -> {
            animals.forEach(animal -> {
                animal.addEnergy(energyToAdd / animals.size());
                plantsToRemove.add(animal.getPosition());
            });
        });
        map.removePlants(plantsToRemove);
    }

    private void procreate() {
        map.getAnimalsToProcreate(minProcreationEnergy).forEach(animals -> {
            Animal animal1;
            Animal animal2;

            if (animals.size() > 2) {
                int splitIndex = 0;
                for (var animal : animals) {
                    if (animal.getEnergy() < animals.get(0).getEnergy()) {
                        break;
                    }
                    splitIndex++;
                }
                animal1 = animals.get(rand.nextInt(splitIndex));
                animal2 = animals.get(rand.nextInt(animals.size() - splitIndex) + splitIndex);
            } else {
                animal1 = animals.get(0);
                animal2 = animals.get(1);
            }

            var adjacentFields = map.getEmptyAdjacentFields(animal1.getPosition());
            Vector2d childPosition;
            if (adjacentFields.isEmpty()) {
                childPosition = animal1.getPosition().add(MapDirection.values()[rand.nextInt(8)].toUnitVector());
            }
        });
    }
}
