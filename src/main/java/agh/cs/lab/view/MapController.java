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

    private static int MIN_FIELD_SIDE = 25;

    @FXML
    private StackPane container;

    @FXML
    private Canvas fields;

    @FXML
    private Canvas animals;

    @FXML
    private Canvas plants;

    private GraphicsContext fieldsCtx;
    private GraphicsContext animalsCtx;
    private GraphicsContext plantsCtx;
    private double fieldSide;
    private Image[] animalImages = new Image[6];
    private Image plantImage;
    private Consumer<Vector2d> onClick;
    private int startEnergy;

    public void setViewSize(double width, double height) {
        container.setPrefWidth(width);
        container.setPrefHeight(height);

        fields.setWidth(width);
        fields.setHeight(height);
        animals.setWidth(width);
        animals.setHeight(height);
        plants.setWidth(width);
        plants.setHeight(height);
    }

    public void onClick(Consumer<Vector2d> onClick) {
        this.onClick = onClick;
    }

    public void redrawFields(Pair<Vector2d, Vector2d> mapBorders, Pair<Vector2d, Vector2d> jungleBorders, int startEnergy) {
        this.startEnergy = startEnergy;

        int mapWidth = mapBorders.second.x + 1;
        int mapHeight = mapBorders.second.y + 1;

        setFieldSide(mapWidth, mapHeight);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                var position = new Vector2d(i, j);
                if (position.followsWeakly(jungleBorders.first) && position.precedesWeakly(jungleBorders.second)) {
                    drawJungleField(position);
                } else {
                    drawSawannaField(position);
                }
            }
        }
    }

    public void redrawAnimals(Set<Animal> animals) {
        clearAnimals();
        animals.forEach(this::drawAnimal);
    }

    public void drawTrackedAnimal(Animal animal) {
        drawAnimal(animal);

        drawCircleAroundAnimal(Color.BLUE, animal.getPosition());
    }

    public void redrawAnimalsWithMostCommonGenes(Collection<Animal> animals) {
        animals.forEach(this::redrawAnimalWithMostCommonGenes);
    }

    public void redrawAnimal(Animal animal) {
        clearAnimal(animal.getPosition());
        drawAnimal(animal);
    }

    public void drawPlants(Collection<Plant> plants) {
        plants.forEach(this::drawPlant);
    }

    public void clearPlants(Collection<Plant> plants) {
        plants.forEach(this::clearPlant);
    }

    @Override
    public void reset() {
        clearFields();
        clearAnimals();
        clearPlants();
    }

    @Override
    public void init() {
        fieldsCtx = fields.getGraphicsContext2D();
        animalsCtx = animals.getGraphicsContext2D();
        plantsCtx = plants.getGraphicsContext2D();

        fieldsCtx.setStroke(Color.BLACK);

        animalImages[0] = new Image("/animal-1.png");
        animalImages[1] = new Image("/animal-2.png");
        animalImages[2] = new Image("/animal-3.png");
        animalImages[3] = new Image("/animal-4.png");
        animalImages[4] = new Image("/animal-5.png");
        animalImages[5] = new Image("/animal-6.png");

        plantImage = new Image("/plant.png");

        plants.setOnMouseClicked(event ->
                onClick.accept(new Vector2d(
                        (int) (event.getX() / fieldSide),
                        (int) ((fields.getHeight() - event.getY()) / fieldSide)
                ))
        );
    }

    private void setFieldSide(double mapWidth, double mapHeight) {
        double widthRatio = container.getPrefWidth() / mapWidth;
        double heightRatio = container.getPrefHeight() / mapHeight;

        fieldSide = Math.max(widthRatio, heightRatio);
        if (fieldSide < MIN_FIELD_SIDE) {
            fieldSide = MIN_FIELD_SIDE;
        }

        fields.setWidth(fieldSide * mapWidth);
        fields.setHeight(fieldSide * mapHeight);
        animals.setWidth(fieldSide * mapWidth);
        animals.setHeight(fieldSide * mapHeight);
        plants.setWidth(fieldSide * mapWidth);
        plants.setHeight(fieldSide * mapHeight);
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

    private void redrawAnimalWithMostCommonGenes(Animal animal) {
        clearAnimal(animal.getPosition());
        drawAnimal(animal);

        drawCircleAroundAnimal(Color.RED, animal.getPosition());
    }

    private void clearAnimal(Vector2d position) {
        animalsCtx.clearRect(
                getFieldX(position),
                getFieldY(position),
                fieldSide,
                fieldSide
        );
    }

    private void drawAnimal(Animal animal) {
        animalsCtx.save();

        animalsCtx.translate(
                getFieldCentreX(animal.getPosition()),
                getFieldCentreY(animal.getPosition())
        );
        animalsCtx.rotate(animal.getOrientation().ordinal() * 45);

        animalsCtx.drawImage(
                getAnimalImage(animal),
                -fieldSide / 2,
                -fieldSide / 2,
                fieldSide,
                fieldSide
        );

        animalsCtx.restore();
    }

    private Image getAnimalImage(Animal animal) {
        int index = (int) ((float) animal.getEnergy() / ((float) startEnergy / (float) 5));
        if (index > 5) {
            index = 5;
        }
        return animalImages[index];
    }

    private void drawCircleAroundAnimal(Color colour, Vector2d position) {
        animalsCtx.save();
        animalsCtx.setStroke(colour);
        animalsCtx.setLineWidth(4);
        animalsCtx.strokeOval(
                getFieldX(position) + fieldSide * 0.1,
                getFieldY(position) + fieldSide * 0.1,
                fieldSide * 0.8,
                fieldSide * 0.8
        );
        animalsCtx.restore();
    }

    private void drawPlant(Plant plant) {
        plantsCtx.drawImage(
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

    private void clearAnimals() {
        animalsCtx.clearRect(0, 0, animals.getWidth(), animals.getHeight());
    }

    private void clearPlants() {
        plantsCtx.clearRect(0, 0, plants.getWidth(), plants.getHeight());
    }

    private void clearPlant(Plant plant) {
        plantsCtx.clearRect(
                getFieldX(plant.getPosition()),
                getFieldY(plant.getPosition()),
                fieldSide,
                fieldSide
        );
    }
}
