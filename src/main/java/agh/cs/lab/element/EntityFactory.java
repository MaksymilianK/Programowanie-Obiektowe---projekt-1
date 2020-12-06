package agh.cs.lab.element;

import agh.cs.lab.engine.SimulationStatistics;
import agh.cs.lab.map.WorldMap;

public abstract class EntityFactory {

    protected final WorldMap map;
    protected final SimulationStatistics statistics;

    public EntityFactory(WorldMap map, SimulationStatistics statistics) {
        this.map = map;
        this.statistics = statistics;
    }
}
