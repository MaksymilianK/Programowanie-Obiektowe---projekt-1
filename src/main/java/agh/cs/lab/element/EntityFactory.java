package agh.cs.lab.element;

import agh.cs.lab.engine.SimulationStatistics;
import agh.cs.lab.map.WorldMap;
import agh.cs.lab.view.WorldMapView;

public abstract class EntityFactory {

    protected final WorldMap map;
    protected final WorldMapView view;
    protected final SimulationStatistics statistics;

    public EntityFactory(WorldMap map, WorldMapView view, SimulationStatistics statistics) {
        this.map = map;
        this.view = view;
        this.statistics = statistics;
    }
}
