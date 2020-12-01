package agh.cs.lab.element;

import agh.cs.lab.map.WorldMap;
import agh.cs.lab.view.WorldMapView;

public abstract class EntityFactory {

    protected final WorldMap map;
    protected final WorldMapView view;

    public EntityFactory(WorldMap map, WorldMapView view) {
        this.map = map;
        this.view = view;
    }
}
