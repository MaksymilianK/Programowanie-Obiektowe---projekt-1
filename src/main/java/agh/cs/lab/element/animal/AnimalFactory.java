package agh.cs.lab.element.animal;

import agh.cs.lab.element.EntityFactory;
import agh.cs.lab.statistics.SimulationStatisticsManager;
import agh.cs.lab.engine.map.WorldMap;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.shared.Vector2d;

import java.util.*;

public class AnimalFactory extends EntityFactory {

    private final int startEnergy;
    private final SimulationStatisticsManager statistics;
    private final Rand rand;
    private final GeneFactory genesFactory;

    private int counter = 0;

    public AnimalFactory(int startEnergy, WorldMap map, SimulationStatisticsManager statistics, Rand rand,
                         GeneFactory genesFactory) {
        super(map, statistics);
        this.startEnergy = startEnergy;
        this.statistics = statistics;
        this.rand = rand;
        this.genesFactory = genesFactory;
    }

    public Animal create(Vector2d position) {
        return Animal.create(
            ++counter,
            position,
            genesFactory.create(),
            randomOrientation(),
            startEnergy,
            Set.of(map, statistics)
        );
    }

    public Animal giveBirth(Vector2d position, Animal parent1, Animal parent2) {
        return Animal.giveBirth(
                ++counter,
                position,
                genesFactory.create(parent1.getGene(), parent2.getGene()),
                randomOrientation(),
                parent1.getEnergy() / 4 + parent2.getEnergy() / 4,
                parent1,
                parent2,
                Set.of(map, statistics)
        );
    }

    private Direction randomOrientation() {
        return Direction.values()[rand.randInt(8)];
    }
}
