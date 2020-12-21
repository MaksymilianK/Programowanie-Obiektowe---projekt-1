package agh.cs.lab.view;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.shared.Pair;
import agh.cs.lab.shared.Vector2d;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class MapController implements Controller {

    @FXML
    private StackPane container;

    @FXML
    private Canvas fields;

    @FXML
    private Canvas entities;

    private GraphicsContext fieldsCtx;
    private GraphicsContext entitiesCtx;
    private double fieldSide;
    private Image animalImage;
    private Image plantImage;
    private Consumer<Vector2d> onClick;

    public void clearAnimals2(Collection<Animal> animals) {
        animals.forEach(animal -> clearEntity(animal.getPosition()));
    }

    public void drawAnimals2(Collection<Animal> animals) {
        animals.forEach(this::redrawAnimal);
    }

    public void drawPlants2(Collection<Plant> plants) {
        plants.forEach(this::drawPlant);
    }

    public void redrawFields(Pair<Vector2d, Vector2d> mapBorders, Pair<Vector2d, Vector2d> jungleBorders) {
        int mapWidth = mapBorders.second.x + 1;
        int mapHeight = mapBorders.second.y + 1;

        setFieldSide(mapWidth, mapHeight);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                var position = new Vector2d(i, j);
                if (i >= jungleBorders.first.x && i <= jungleBorders.second.x &&
                        j >= jungleBorders.first.y && j <= jungleBorders.second.y) {
                    drawJungleField(position);
                } else {
                    drawSawannaField(position);
                }
            }
        }
    }

    public void onClick(Consumer<Vector2d> onClick) {
        this.onClick = onClick;
    }

    public void redrawAnimals(Set<Animal> animals) {
        clearEntities();
        animals.forEach(this::drawAnimal);
    }

    public void drawTrackedAnimal(Animal animal) {
        drawAnimal(animal);

        entitiesCtx.save();

        entitiesCtx.setStroke(Color.BLUE);
        entitiesCtx.setLineWidth(4);
        entitiesCtx.strokeOval(
                getFieldX(animal.getPosition()) + fieldSide * 0.1,
                getFieldY(animal.getPosition()) + fieldSide * 0.1,
                fieldSide * 0.8,
                fieldSide * 0.8
        );
        entitiesCtx.restore();
    }

    public void redrawAnimalsWithMostCommonGenes(Collection<Animal> animals) {
        animals.forEach(this::redrawAnimalWithMostCommonGenes);
    }

    public void redrawAnimal(Animal animal) {
        clearEntity(animal.getPosition());
        drawAnimal(animal);
    }

    private void redrawAnimalWithMostCommonGenes(Animal animal) {
        clearEntity(animal.getPosition());
        drawAnimal(animal);

        entitiesCtx.save();

        entitiesCtx.setStroke(Color.RED);
        entitiesCtx.setLineWidth(4);
        entitiesCtx.strokeOval(
                getFieldX(animal.getPosition()) + fieldSide * 0.1,
                getFieldY(animal.getPosition()) + fieldSide * 0.1,
                fieldSide * 0.8,
                fieldSide * 0.8
        );
        entitiesCtx.restore();
    }

    private void clearEntity(Vector2d position) {
        entitiesCtx.clearRect(
                getFieldX(position),
                getFieldY(position),
                fieldSide,
                fieldSide
        );
    }

    public void drawPlants(Set<Plant> plants) {
        plants.forEach(this::drawPlant);
    }

    public void setViewSize(double width, double height) {
        container.setPrefWidth(width);
        container.setPrefHeight(height);

        fields.setWidth(width);
        fields.setHeight(height);
        entities.setWidth(width);
        entities.setHeight(height);
    }

    private void setFieldSide(double mapWidth, double mapHeight) {
        double widthRatio = container.getPrefWidth() / mapWidth;
        double heightRatio = container.getPrefHeight() / mapHeight;

        fieldSide = Math.max(widthRatio, heightRatio);
        if (fieldSide < 25) {
            fieldSide = 25;
        }

        fields.setWidth(fieldSide * mapWidth);
        fields.setHeight(fieldSide * mapHeight);
        entities.setWidth(fieldSide * mapWidth);
        entities.setHeight(fieldSide * mapHeight);
        container.setPrefWidth(fieldSide * mapWidth);
        container.setPrefHeight(fieldSide * mapHeight);
    }

    private void drawSawannaField(Vector2d position) {
        fieldsCtx.setFill(Color.GREEN);
        drawField(position);
    }

    private void drawJungleField(Vector2d position) {
        fieldsCtx.setFill(Color.DARKGREEN);
        drawField(position);
    }

    private void drawField(Vector2d position) {
        fieldsCtx.fillRect(getFieldX(position), getFieldY(position), fieldSide, fieldSide);
        fieldsCtx.strokeRect(getFieldX(position), getFieldY(position), fieldSide, fieldSide);
    }

    private void drawAnimal(Animal animal) {
        entitiesCtx.save();

        entitiesCtx.translate(
                getFieldCentreX(animal.getPosition()),
                getFieldCentreY(animal.getPosition())
        );
        entitiesCtx.rotate(animal.getOrientation().ordinal() * 45);

        entitiesCtx.drawImage(
                animalImage,
                -fieldSide / 2,
                -fieldSide / 2,
                fieldSide,
                fieldSide
        );

        entitiesCtx.restore();
    }

    private void drawPlant(Plant plant) {
        entitiesCtx.drawImage(
                plantImage,
                getFieldX(plant.getPosition()),
                getFieldY(plant.getPosition()),
                fieldSide,
                fieldSide
        );
    }

    private double getFieldX(Vector2d position) {
        return position.x * fieldSide;
    }

    private double getFieldY(Vector2d position) {
        return fields.getHeight() - (position.y + 1) * fieldSide;
    }

    private double getFieldCentreX(Vector2d position) {
        return getFieldX(position) + fieldSide / 2;
    }

    private double getFieldCentreY(Vector2d position) {
        return getFieldY(position) + fieldSide / 2;
    }

    private void clearFields() {
        fieldsCtx.clearRect(0, 0, fields.getWidth(), fields.getHeight());
    }

    private void clearEntities() {
        entitiesCtx.clearRect(0, 0, entities.getWidth(), entities.getHeight());
    }

    @Override
    public void reset() {
        clearFields();
        clearEntities();
    }

    @Override
    public void init() {
        fieldsCtx = fields.getGraphicsContext2D();
        entitiesCtx = entities.getGraphicsContext2D();

        fieldsCtx.setStroke(Color.BLACK);

        animalImage = new Image("/animal.png");
        plantImage = new Image("/plant.png");

        entities.setOnMouseClicked(event ->
            onClick.accept(new Vector2d(
                    (int) (event.getX() / fieldSide),
                    (int) ((fields.getHeight() - event.getY()) / fieldSide)
            ))
        );
    }
}
