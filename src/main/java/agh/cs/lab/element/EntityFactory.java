package agh.cs.lab.element;

import agh.cs.lab.statistics.SimulationStatisticsManager;
import agh.cs.lab.engine.map.WorldMap;

public abstract class EntityFactory {   // uważam, że używanie tu fabryk i fasad jest przekombinowane

    protected final WorldMap map;
    protected final SimulationStatisticsManager statistics;

    public EntityFactory(WorldMap map, SimulationStatisticsManager statistics) {
        this.map = map;
        this.statistics = statistics;
    }
}
