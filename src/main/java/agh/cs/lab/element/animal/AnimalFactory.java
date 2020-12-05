package agh.cs.lab.element.animal;

import agh.cs.lab.element.EntityFactory;
import agh.cs.lab.engine.SimulationStatistics;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.view.WorldMapView;

import java.util.*;

public class AnimalFactory extends EntityFactory {

    private final Random rand = new Random();
    private final int startEnergy = 50;
    private final SimulationStatistics statistics;
    private final GenesFactory genesFactory;

    private int counter = 0;

    public AnimalFactory(WorldMap map, WorldMapView view, SimulationStatistics statistics, GenesFactory genesFactory) {
        super(map, view, statistics);
        this.statistics = statistics;
        this.genesFactory = genesFactory;
    }

    public Animal create(Vector2d position) {
        return Animal.create(
            ++counter,
            Set.of(map, view, statistics),
            position,
            genesFactory.create(),
            randomOrientation(),
            startEnergy
        );
    }

    public Animal giveBirth(Vector2d position, Animal parent1, Animal parent2) {
        return Animal.giveBirth(
                ++counter,
                Set.of(map, view, statistics),
                position,
                genesFactory.create(parent1.getGenes(), parent2.getGenes()),
                randomOrientation(),
                startEnergy,
                parent1,
                parent2
        );
    }

    private Direction randomOrientation() {
        return Direction.values()[rand.nextInt(8)];
    }
}
