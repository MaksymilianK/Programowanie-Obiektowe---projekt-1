package agh.cs.lab.engine;

import agh.cs.lab.element.Entity;
import agh.cs.lab.element.EntityFactoryFacade;
import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.statistics.AnimalTracker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimulationEngine {

    private final SimulationSettings settings;
    private final WorldMap map;
    private final EntityFactoryFacade entityFactory;
    private final Rand rand;
    private final Set<Animal> animals = new HashSet<>();
    private final Set<Plant> plants = new HashSet<>();

    private AnimalTracker tracker;

    public SimulationEngine(SimulationSettings settings, WorldMap map, EntityFactoryFacade entityFactory, Rand rand) {
        this.settings = settings;
        this.map = map;
        this.entityFactory = entityFactory;
        this.rand = rand;
    }

    public void setTracker(AnimalTracker tracker) {
        removeTracker();

        this.tracker = tracker;
        animals.forEach(animal -> animal.addObserver(tracker));
    }

    public void removeTracker() {
        if (tracker != null) {
            animals.forEach(animal -> animal.removeObserver(tracker));
            tracker = null;
        }
    }

    public void init(int initAnimals) {
        var emptyFields = map.getEmptyFieldsInsideJungle();
        emptyFields.addAll(map.getEmptyFieldsOutsideJungle());

        if (emptyFields.size() < initAnimals) {
            throw new SimulationException("Cannot create so many animals on simulation start - there is too few fields " +
                    "available");
        }

        for (int i = 0; i < initAnimals; i++) {
            int index = rand.randInt(emptyFields.size());
            animals.add(entityFactory.createAnimal(emptyFields.get(index)));
            emptyFields.remove(index);
        }
    }

    public void newEpoch() {
        animals.forEach(Entity::addEpochToLife);
        animals.forEach(Entity::addEpochToLife);
    }

    public void checkAnimalsEnergy() {
        var iterator = animals.iterator();
        while (iterator.hasNext()) {
            var animal = iterator.next();
            if (animal.getEnergy() <= 0) {
                animal.kill();
                iterator.remove();
            }
        }
    }

    public void turnAnimals() {
        animals.forEach(animal -> animal.turn(getNewOrientation(animal)));
    }

    public void moveAnimals() {
        animals.forEach(animal -> {
            animal.move(map.getNewPosition(animal.getPosition(), animal.getOrientation()));
            animal.subtractEnergy(settings.getMoveEnergy());
        });
    }

    public void feedAnimals() {
        var fedAnimals = new HashSet<Animal>();

        map.getAnimalsToFeed().forEach(field -> {
            field.first.eat();
            field.second.forEach(animal -> {
                animal.addEnergy(settings.getPlantEnergy() / animals.size());
            });

            plants.remove(field.first);
        });
    }

    public void procreate() {
        map.getAnimalsToProcreate(settings.getProcreationEnergy()).forEach(animals -> {
            Animal parent1 = animals.get(0);
            Animal parent2 = animals.get(rand.randInt(1, animals.size()));

            Vector2d childPosition;
            var adjacentFields = map.getEmptyAdjacentFields(parent1.getPosition());

            if (adjacentFields.isEmpty()) {
                childPosition = map.getNewPosition(parent1.getPosition(), Direction.values()[rand.randInt(8)]);
            } else {
                childPosition = adjacentFields.get(rand.randInt(adjacentFields.size()));
            }

            parent1.subtractEnergy(parent1.getEnergy() / 4);
            parent2.subtractEnergy(parent2.getEnergy() / 4);

            this.animals.add(entityFactory.giveBirthToAnimal(childPosition, parent1, parent2));
        });
    }

    public Set<Plant> addPlants() {
        var newPlants = new HashSet<Plant>();
        newPlants.add(trySetPlant(map.getEmptyFieldsInsideJungle()));
        newPlants.add(trySetPlant(map.getEmptyFieldsOutsideJungle()));
        return newPlants;
    }

    public Set<Animal> getAnimalsWithTopEnergy() {
        return map.getAnimalsWithTopEnergy();
    }

    public Set<Plant> getPlants() {
        return map.getPlants();
    }

    private Plant trySetPlant(List<Vector2d> fields) {
        if (fields.isEmpty()) {
            return null;
        } else {
            int index = rand.randInt(fields.size());
            var plant = entityFactory.createPlant(fields.get(index));
            plants.add(plant);
            return plant;
        }
    }

    public Set<Animal> v() {
        return animals;
    }

    private Direction getNewOrientation(Animal animal) {
        int turn = rand.randInt(8);
        var newOrientation = animal.getOrientation();

        for (int i = 0; i < turn; i++) {
            newOrientation = newOrientation.next();
        }
        return newOrientation;
    }
}
