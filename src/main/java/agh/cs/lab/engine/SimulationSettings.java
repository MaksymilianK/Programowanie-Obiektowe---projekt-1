package agh.cs.lab.engine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SimulationSettings {

    private final int mapWidth;
    private final int mapHeight;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private final float jungleRatio;
    private final int procreationEnergy;

    @JsonCreator()
    public SimulationSettings(@JsonProperty("mapWidth") int mapWidth, @JsonProperty("mapHeight") int mapHeight,
                              @JsonProperty("startEnergy") int startEnergy, @JsonProperty("moveEnergy") int moveEnergy,
                              @JsonProperty("plantEnergy") int plantEnergy, @JsonProperty("jungleRatio") float jungleRatio) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.procreationEnergy = startEnergy / 2;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public float getJungleRatio() {
        return jungleRatio;
    }

    public int getProcreationEnergy() {
        return procreationEnergy;
    }
}
