package agh.cs.lab.view;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.shared.Pair;
import agh.cs.lab.shared.Vector2d;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Set;

public class WorldMapView implements View {

    @FXML
    private StackPane pane;

    @FXML
    private Canvas fields;

    @FXML
    private Canvas entities;

    private GraphicsContext fieldsCtx;
    private GraphicsContext entitiesCtx;
    private double fieldSide;
    private Set<Vector2d> animalsPositions;
    private Set<Vector2d> plantsPositions;

    private Image[] animalImages = new Image[8];
    private Image plantImage;

    public void drawAnimals(Set<Animal> animals) {
        animalsPositions.forEach(this::clearField);
        animals.forEach(this::drawAnimal);
    }

    public Parent draw(Pair<Vector2d, Vector2d> mapBorders, Pair<Vector2d, Vector2d> jungleBorders,
                       double viewWidth, double viewHeight) {
        int mapWidth = mapBorders.second.x + 1;
        int mapHeight = mapBorders.second.y + 1;

        double widthRatio = viewWidth / mapWidth;
        double heightRatio = viewHeight / mapHeight;

        fieldSide = Math.min(widthRatio, heightRatio);
        if (fieldSide < 25) {
            fieldSide = 25;
        }

        fields.setWidth(fieldSide*mapWidth);
        fields.setHeight(fieldSide*mapHeight);

        entities.setWidth(fieldSide*mapWidth);
        entities.setHeight(fieldSide*mapHeight);

        fieldsCtx = fields.getGraphicsContext2D();
        entitiesCtx = entities.getGraphicsContext2D();

        for (int i = 0; i < 8; i++) {
            animalImages[i] = new Image("./animal" + i + ".png");
        }
        plantImage = new Image("./plant.png");

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

                /*if ((new Random()).nextInt(2) == 0) {
                    fieldsCtx.drawImage(aimage, i * fieldSide + fieldSide * 0.15, j * fieldSide + fieldSide * 0.15,
                            fieldSide * 0.7, fieldSide * 0.7);
                } else {
                    fieldsCtx.drawImage(pimage, i * fieldSide + fieldSide * 0.15, j * fieldSide + fieldSide * 0.15,
                            fieldSide * 0.7, fieldSide * 0.7);
                }*/
            }
        }

        return pane;
    }

    public void clearEntities() {
        entitiesCtx.clearRect(0, 0, entities.getWidth(), entities.getHeight());
    }

    public void drawPlants(Set<Plant> plants) {
        plants.forEach(this::drawPlant);
    }

    private void drawAnimal(Animal animal) {
        entitiesCtx.drawImage(animalImages[animal.getOrientation().ordinal()],
                animal.getPosition().x * fieldSide,
                entities.getHeight() - (animal.getPosition().y + 1) * fieldSide, fieldSide, fieldSide);
    }

    private void drawPlant(Plant plant) {
        entitiesCtx.drawImage(plantImage,
                plant.getPosition().x * fieldSide,
                entities.getHeight() - (plant.getPosition().y + 1) * fieldSide, fieldSide, fieldSide);
    }

    private void clearField(Vector2d position) {
        entitiesCtx.clearRect(
                position.x * fieldSide,
                entities.getHeight() - (position.y + 1) * fieldSide,
                fieldSide,
                fieldSide
        );
    }

    @Override
    public void reset() {

    }
}
