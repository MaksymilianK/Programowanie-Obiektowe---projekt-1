package agh.cs.lab.element.plant;

public interface PlantObserver {

    void onPlantCreated(Plant plant);
    void onPlantEaten(Plant plant);
}
