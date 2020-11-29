package agh.cs.lab.shared;

public enum MapDirection {

    NORTH(new Vector2d(0, 1)),
    NORTH_EAST(new Vector2d(1, 1)),
    EAST(new Vector2d(1, 0)),
    SOUTH_EAST(new Vector2d(1, -1)),
    SOUTH(new Vector2d(0, -1)),
    SOUTH_WEST(new Vector2d(-1, -1)),
    WEST(new Vector2d(-1, 0)),
    NORTH_WEST(new Vector2d(-1, 1));

    private final Vector2d unitVector;

    MapDirection(Vector2d unitVector) {
        this.unitVector = unitVector;
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTH_WEST;
            case NORTH_WEST -> WEST;
            case WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case EAST -> NORTH_EAST;
            case NORTH_EAST -> NORTH;
        };
    }

    public Vector2d toUnitVector() {
        return unitVector;
    }
}
