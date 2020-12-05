package agh.cs.lab.element.plant;

import agh.cs.lab.element.EntityFactory;
import agh.cs.lab.engine.SimulationStatistics;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.view.WorldMapView;

import java.util.Set;

public class PlantFactory extends EntityFactory {

    private int counter = 0;

    public PlantFactory(WorldMap map, WorldMapView view, SimulationStatistics statistics) {
        super(map, view, statistics);
    }

    public Plant create(Vector2d position) {
        return new Plant(++counter, position, Set.of(map, view, statistics));
    }
}
