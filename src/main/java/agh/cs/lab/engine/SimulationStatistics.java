package agh.cs.lab.engine;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalObserver;
import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.element.plant.PlantObserver;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Vector2d;

import java.util.Set;

public class SimulationStatistics implements AnimalObserver, PlantObserver {

    private final GenesCounter genesCounter = new GenesCounter();
    private final LivingAnimalsChildrenCounter childrenCounter = new LivingAnimalsChildrenCounter();

    private int livingAnimals = 0;
    private int deadAnimals = 0;
    private int livingPlants = 0;
    private int livingAnimalEnergy = 0;
    private int deadAnimalsEpochLived = 0;

    @Override
    public void onAnimalCreated(Animal animal) {
        childrenCounter.addAnimal(animal);
        onNewAnimal(animal);
    }

    @Override
    public void onAnimalBorn(Animal animal, Animal parent1, Animal parent2) {
        childrenCounter.addChild(animal, parent1, parent2);
        onNewAnimal(animal);
    }

    @Override
    public void onAnimalDead(Animal animal) {
        genesCounter.remove(animal.getGenes());
        childrenCounter.removeAnimal(animal);
        livingAnimals--;
        deadAnimals++;
        livingAnimalEnergy -= animal.getEnergy();
        deadAnimalsEpochLived += animal.getLengthOfLife();
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
        livingAnimalEnergy += (animal.getEnergy() - prevEnergy);
    }

    @Override
    public void onPlantCreated(Plant plant) {
        livingPlants++;
    }

    @Override
    public void onPlantEaten(Plant plant) {
        livingPlants--;
    }

    public int getLivingAnimals() {
        return livingAnimals;
    }

    public int getLivingPlants() {
        return livingPlants;
    }

    public Set<Gene> getMostCommonGenes() {
        return genesCounter.getMostCommonGenes();
    }

    public float getAverageLivingAnimalsEnergy() {
        return (float) livingAnimalEnergy / (float) livingAnimals;
    }

    public float getAverageDeadAnimalsLengthOfLife() {
        return (float) deadAnimalsEpochLived / (float) deadAnimals;
    }

    public float getAverageLivingAnimalsChildren() {
        return childrenCounter.getAverageChildren();
    }

    private void onNewAnimal(Animal animal) {
        genesCounter.add(animal.getGenes());
        livingAnimals++;
        livingAnimalEnergy += animal.getEnergy();
    }
}
