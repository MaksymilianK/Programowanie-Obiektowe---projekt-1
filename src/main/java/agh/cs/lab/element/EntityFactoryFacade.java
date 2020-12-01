package agh.cs.lab.element;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalFactory;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.element.plant.PlantFactory;
import agh.cs.lab.shared.Vector2d;

public class EntityFactoryFacade {

    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory;

    public EntityFactoryFacade(AnimalFactory animalFactory, PlantFactory plantFactory) {
        this.animalFactory = animalFactory;
        this.plantFactory = plantFactory;
    }

    public Animal createAnimal(Vector2d position) {
        return animalFactory.create(position);
    }

    public Animal createAnimal(Vector2d position, Animal parent1, Animal parent2) {
        return animalFactory.create(position, parent1, parent2);
    }

    public Plant createPlant(Vector2d position) {
        return plantFactory.create(position);
    }
}
