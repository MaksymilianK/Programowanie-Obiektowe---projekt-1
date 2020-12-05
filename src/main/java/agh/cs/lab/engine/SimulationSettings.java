package agh.cs.lab.engine;

public final class SimulationSettings {

    private final int mapWidth;
    private final int mapHeight;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private final float jungleRatio;
    private final int procreationEnergy;

    public SimulationSettings(int mapWidth, int mapHeight, int startEnergy, int moveEnergy, int plantEnergy,
                              float jungleRatio) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.procreationEnergy = (int) Math.ceil(startEnergy / 2);
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
