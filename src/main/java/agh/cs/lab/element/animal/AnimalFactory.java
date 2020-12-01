package agh.cs.lab.element.animal;

import agh.cs.lab.element.EntityFactory;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.MapDirection;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.view.WorldMapView;

import java.util.*;

public class AnimalFactory extends EntityFactory {

    private final Random rand = new Random();
    private final int startEnergy = 50;
    private final GenesFactory genesFactory;

    private int counter = 0;

    public AnimalFactory(WorldMap map, WorldMapView view, GenesFactory genesFactory) {
        super(map, view);
        this.genesFactory = genesFactory;
    }

    public Animal create(Vector2d position) {
        return new Animal(
            ++counter,
            Set.of(map, view),
            position,
            genesFactory.create(),
            randomOrientation(),
            startEnergy
        );
    }

    public Animal create(Vector2d position, Animal parent1, Animal parent2) {
        return new Animal(
                ++counter,
                Set.of(map, view),
                position,
                genesFactory.create(parent1.getGenes(), parent2.getGenes()),
                randomOrientation(),
                startEnergy
        );
    }

    private MapDirection randomOrientation() {
        return MapDirection.values()[rand.nextInt(8)];
    }
}
