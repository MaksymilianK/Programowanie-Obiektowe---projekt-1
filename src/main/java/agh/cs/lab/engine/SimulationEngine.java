package agh.cs.lab.engine;

import agh.cs.lab.element.EntityFactoryFacade;
import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Vector2d;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SimulationEngine {

    private final SimulationSettings settings;
    private final WorldMap map;
    private final EntityFactoryFacade entityFactory;
    private final Random rand = new Random();
    private final Set<Animal> animals = new HashSet<>();

    public SimulationEngine(SimulationSettings settings, WorldMap map, EntityFactoryFacade entityFactory) {
        this.settings = settings;
        this.map = map;
        this.entityFactory = entityFactory;
    }

    public void checkAnimalsEnergy() {
        animals.stream()
                .filter(animal -> animal.getEnergy() < 1)
                .forEach(this::killAnimal);
    }

    public void turnAnimals() {
        animals.forEach(animal -> animal.turn(getNewOrientation(animal)));
    }

    public void moveAnimals() {
        animals.forEach(animal -> animal.move(map.getNewPosition(animal.getPosition(), animal.getOrientation())));
    }

    public void feedAnimals() {
        map.getAnimalsToFeed().forEach(field -> {
            field.first.eat();
            field.second.forEach(animal -> {
                animal.addEnergy(settings.getPlantEnergy() / animals.size());
            });
        });
    }

    public void procreate() {
        map.getAnimalsToProcreate(settings.getProcreationEnergy()).forEach(animals -> {
            Animal parent1 = animals.get(0);
            Animal parent2 = animals.get(rand.nextInt(animals.size() - 1) + 1);

            Vector2d childPosition;
            var adjacentFields = map.getEmptyAdjacentFields(parent1.getPosition());

            if (adjacentFields.isEmpty()) {
                childPosition = parent1.getPosition().add(Direction.values()[rand.nextInt(8)].toUnitVector());
            } else {
                childPosition = adjacentFields.get(rand.nextInt(adjacentFields.size()));
            }

            entityFactory.createAnimal(childPosition, parent1, parent2);
        });
    }

    public void addPlants() {
        trySetPlant(map.getEmptyFieldsInsideJungle());
        trySetPlant(map.getEmptyFieldsOutsideJungle());
    }

    private void trySetPlant(List<Vector2d> fields) {
        if (!fields.isEmpty()) {
            int index = rand.nextInt(fields.size());
            entityFactory.createPlant(fields.get(index));
        }
    }

    private void killAnimal(Animal animal) {
        animal.kill();
        animals.remove(animal);
    }

    private Direction getNewOrientation(Animal animal) {
        int turn = rand.nextInt(8);
        var newOrientation = animal.getOrientation();

        for (int i = 0; i < turn; i++) {
            newOrientation = newOrientation.next();
        }
        return newOrientation;
    }
}
