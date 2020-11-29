package agh.cs.lab.map;

import agh.cs.lab.shared.Vector2d;

/**
 * The class is a simple unmodifiable container for two coordinates intended to represent map's corners.
 * Implemented to overcome the lack of possibility to return more than one value from one function call in Java.
 */
public final class MapCorners {

    public final Vector2d lowerLeft;
    public final Vector2d upperRight;

    public MapCorners(Vector2d lowerLeft, Vector2d upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }
}
