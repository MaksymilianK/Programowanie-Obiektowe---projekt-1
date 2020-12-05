package agh.cs.lab.view;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalObserver;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.element.plant.PlantObserver;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Pair;
import agh.cs.lab.shared.Vector2d;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class WorldMapView implements AnimalObserver, PlantObserver {

    @FXML
    private Canvas fields;

    public Canvas draw(Pair<Vector2d, Vector2d> mapBorders, Pair<Vector2d, Vector2d> jungleBorders,
                       double viewWidth, double viewHeight) {
        int mapWidth = mapBorders.second.x + 1;
        int mapHeight = mapBorders.second.y + 1;

        double widthRatio = viewWidth / mapWidth;
        double heightRatio = viewHeight / mapHeight;

        double fieldSide = Math.max(widthRatio, heightRatio);
        if (fieldSide < 25) {
            fieldSide = 25;
        }

        fields.setWidth(fieldSide*mapWidth);
        fields.setHeight(fieldSide*mapHeight);

        var fieldsCtx = fields.getGraphicsContext2D();

        var aimage = new Image("./animal.png");
        var pimage = new Image("./plant.png");

        fieldsCtx.setFill(Color.GREEN);
        fieldsCtx.setStroke(Color.BLACK);
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (i >= jungleBorders.first.x && i <= jungleBorders.second.x &&
                        j >= jungleBorders.first.y && j <= jungleBorders.second.y) {
                    fieldsCtx.save();
                    fieldsCtx.setFill(Color.DARKGREEN);
                    fieldsCtx.fillRect(i*fieldSide, j*fieldSide, fieldSide, fieldSide);
                    fieldsCtx.strokeRect(i*fieldSide, j*fieldSide, fieldSide, fieldSide);
                    fieldsCtx.restore();
                } else {
                    fieldsCtx.fillRect(i*fieldSide, j*fieldSide, fieldSide, fieldSide);
                    fieldsCtx.strokeRect(i*fieldSide, j*fieldSide, fieldSide, fieldSide);
                }

                if ((new Random()).nextInt(2) == 0) {
                    fieldsCtx.drawImage(aimage, i * fieldSide + fieldSide * 0.15, j * fieldSide + fieldSide * 0.15,
                            fieldSide * 0.7, fieldSide * 0.7);
                } else {
                    fieldsCtx.drawImage(pimage, i * fieldSide + fieldSide * 0.15, j * fieldSide + fieldSide * 0.15,
                            fieldSide * 0.7, fieldSide * 0.7);
                }
            }
        }

        System.out.println((jungleBorders.second.x - jungleBorders.first.x + 1)*(jungleBorders.second.y - jungleBorders.first.y + 1) / (double) (mapWidth * mapHeight));

        return fields;
    }

    @Override
    public void onAnimalCreated(Animal animal) {

    }

    @Override
    public void onAnimalBorn(Animal animal, Animal parent1, Animal parent2) {

    }

    @Override
    public void onAnimalDead(Animal animal) {

    }

    @Override
    public void onAnimalTurned(Animal animal, Direction prevOrientation) {

    }

    @Override
    public void onAnimalMoved(Animal animal, Vector2d prevPosition) {

    }

    @Override
    public void onEnergyChanged(Animal animal, int prevEnergy) {

    }

    @Override
    public void onPlantCreated(Plant plant) {

    }

    @Override
    public void onPlantEaten(Plant plant) {

    }
}
