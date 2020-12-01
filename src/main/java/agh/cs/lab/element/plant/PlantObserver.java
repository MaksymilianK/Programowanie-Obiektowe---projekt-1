package agh.cs.lab.element.plant;

public interface PlantObserver {

    void onPlantSet(Plant plant);
    void onPlantEaten(Plant plant);
}
