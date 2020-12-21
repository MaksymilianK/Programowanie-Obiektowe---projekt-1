package agh.cs.lab.statistics;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalObserver;
import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.element.plant.PlantObserver;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Pair;
import agh.cs.lab.shared.Vector2d;

import java.util.*;

public class SimulationStatisticsManager implements AnimalObserver, PlantObserver {

    private final GenesCounter genesCounter = new GenesCounter();
    private final ChildrenCounter childrenCounter = new ChildrenCounter();
    private final GenesCounter totalGenesCounter = new GenesCounter();

    private int livingAnimals = 0;
    private int deadAnimals = 0;
    private int livingPlants = 0;
    private long livingAnimalEnergy = 0;
    private long deadAnimalsEpochLived = 0;
    private int totalLivingAnimals = 0;
    private int totalLivingPlants = 0;
    private float totalAverageEnergy = 0;
    private float totalAverageLifeTime = 0;
    private float totalAverageChildren = 0;
    private int epochs = 0;

    public void nextEpoch() {
        epochs++;
        totalLivingAnimals += getLivingAnimals();
        totalLivingPlants += getLivingPlants();
        totalAverageEnergy += getAverageEnergy();
        totalAverageLifeTime += getAverageLifeTime();
        totalAverageChildren += getAverageChildren();
    }

    @Override
    public void onAnimalCreated(Animal animal) {
        childrenCounter.addAnimal(animal);
        totalGenesCounter.add(animal.getGene());
        onNewAnimal(animal);
    }

    @Override
    public void onAnimalBorn(Animal animal, Animal parent1, Animal parent2) {
        childrenCounter.addChild(animal, parent1, parent2);
        totalGenesCounter.add(animal.getGene());
        onNewAnimal(animal);
    }

    @Override
    public void onAnimalDead(Animal animal) {
        genesCounter.remove(animal.getGene());
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

    public List<Pair<Gene, Integer>> getMostCommonGenes(int howMany) {
        return genesCounter.getMostCommonGenes(howMany);
    }

    public Set<Gene> getMostCommonGenes() {
        return genesCounter.getMostCommonGenes();
    }

    public double getAverageEnergy() {
        if (livingAnimals == 0) {
            return 0.0f;
        } else {
            return (float) livingAnimalEnergy / (float) livingAnimals;
        }
    }

    public double getAverageLifeTime() {
        if (deadAnimals == 0) {
            return 0.0f;
        } else {
            return (float) deadAnimalsEpochLived / (float) deadAnimals;
        }
    }

    public double getAverageChildren() {
        return childrenCounter.getAverageChildren();
    }

    private void onNewAnimal(Animal animal) {
        genesCounter.add(animal.getGene());
        livingAnimals++;
        livingAnimalEnergy += animal.getEnergy();
    }

    public SimulationStatisticsSnapshot getSnapshot() {
        double averageLivingAnimals = 0;
        if (epochs != 0) {
            averageLivingAnimals = (double) totalLivingAnimals / (double) epochs;
        }

        double averageLivingPlants = 0;
        if (epochs != 0) {
            averageLivingPlants = (double) totalLivingPlants / (double) epochs;
        }

        Gene mostCommonGene = null;
        var mostCommonGeneList = totalGenesCounter.getMostCommonGenes(1);
        if (!mostCommonGeneList.isEmpty()) {
            mostCommonGene = mostCommonGeneList.get(0).first;
        }

        double averageEnergy = 0;
        if (epochs != 0) {
            averageEnergy = totalAverageEnergy / (double) epochs;
        }

        double averageLifeTime = 0;
        if (epochs != 0) {
            averageLifeTime = totalAverageLifeTime / (double) epochs;
        }

        double averageChildren = 0;
        if (epochs != 0) {
            averageChildren = totalAverageChildren / (double) epochs;
        }

        return SimulationStatisticsSnapshot.builder()
                .totalLivingAnimals(averageLivingAnimals)
                .totalLivingPlants(averageLivingPlants)
                .totalMostCommonGene(mostCommonGene)
                .totalAverageEnergy(averageEnergy)
                .totalAverageLifeTime(averageLifeTime)
                .totalAverageChildren(averageChildren)
                .build();
    }
}
