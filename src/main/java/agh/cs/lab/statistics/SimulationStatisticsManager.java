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
    private final Map<Integer, SimulationStatisticsSnapshot> snapshots = new HashMap<>();

    private int livingAnimals = 0;
    private int deadAnimals = 0;
    private int livingPlants = 0;
    private int livingAnimalEnergy = 0;
    private int deadAnimalsEpochLived = 0;

    /*public SimulationStatisticsManager(StatisticsSaver saver) {
        this.saver = saver;
    }*/

   /* public void save(int toEpoch) {
        var snapshots = this.snapshots.entrySet().stream()
                .filter(entry -> entry.getKey() <= toEpoch)
                .collect(Collectors.toSet());

        int totalLivingAnimals = 0;
        int totalLivingPlants = 0;
        var genesCounter = new HashMap<Gene, Integer>();
        float totalAverageAnimalEnergy = 0;
        int totalAnimalLifeTime = 0;
        float totalAverageChildren = 0;

        for (var entry : snapshots) {
            totalLivingAnimals += entry.getValue().getLivingAnimals();
            totalLivingPlants += entry.getValue().getLivingPlants();
            genesCounter.
            totalAverageAnimalEnergy += entry.getValue().getAverageEnergy();
            totalAnimalLifeTime += entry.getValue().getAverageLifeTime();
            totalAverageChildren += entry.getValue().getAverageChildren();
        }

        saver.saveAverage(
                totalLivingAnimals / (float) snapshots.size(),
                totalLivingPlants / (float) snapshots.size(),
                totalLivingAnimals / (float) snapshots.size(),
                totalAverageAnimalEnergy / (float) snapshots.size(),
                totalAnimalLifeTime / (float) snapshots.size(),
                totalAverageChildren / (float) snapshots.size()
        );
    }*/

    public void nextEpoch(int epoch) {
        snapshots.put(
                epoch,
                SimulationStatisticsSnapshot.builder()
                        .livingAnimals(getLivingAnimals())
                        .livingPlants(getLivingPlants())
                        .genes(genesCounter.getAll())
                        .averageEnergy(getAverageEnergy())
                        .averageLifeTime(getAverageLifeTime())
                        .averageChildren(getAverageChildren())
                        .build()
        );
    }

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
        genesCounter.remove(animal.getGene());
        childrenCounter.removeAnimal(animal);
        livingAnimals--;
        deadAnimals++;
        livingAnimalEnergy -= animal.getEnergy();
        deadAnimalsEpochLived = animal.getLengthOfLife();
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

    public float getAverageEnergy() {
        if (livingAnimals == 0) {
            return 0.0f;
        } else {
            return (float) livingAnimalEnergy / (float) livingAnimals;
        }
    }

    public float getAverageLifeTime() {
        if (deadAnimals == 0) {
            return 0.0f;
        } else {
            return (float) deadAnimalsEpochLived / (float) deadAnimals;
        }
    }

    public float getAverageChildren() {
        return childrenCounter.getAverageChildren();
    }

    public SimulationStatisticsSnapshot getSnapshot(int epoch) {
        if (!snapshots.containsKey(epoch)) {
            throw new SimulationStatisticsException("There are no statistics for the epoch " + epoch);
        }
        return snapshots.get(epoch);
    }

    private void onNewAnimal(Animal animal) {
        genesCounter.add(animal.getGene());
        livingAnimals++;
        livingAnimalEnergy += animal.getEnergy();
    }
}
