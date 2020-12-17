package agh.cs.lab.map;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalObserver;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.element.plant.PlantObserver;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.shared.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements AnimalObserver, PlantObserver {

    public final Pair<Vector2d, Vector2d> jungleBorders;
    public final Pair<Vector2d, Vector2d> mapBorders;
    private final Map<Vector2d, MapField> fields;
    private final Set<MapField> withAnimals = new HashSet<>();
    private final Set<MapField> withPlants = new HashSet<>();

    private WorldMap(Pair<Vector2d, Vector2d> mapBorders, Pair<Vector2d, Vector2d> jungleBorders,
                    Map<Vector2d, MapField> fields) {
        this.jungleBorders = jungleBorders;
        this.mapBorders = mapBorders;
        this.fields = fields;
    }

    public Collection<Pair<Plant, List<Animal>>> getAnimalsToFeed() {
        return withAnimals.stream()
                .filter(MapField::plantAt)
                .map(field -> new Pair<>(field.getPlant(), field.getHealthiestAnimals()))
                .collect(Collectors.toSet());
    }

    public Collection<List<Animal>> getAnimalsToProcreate(int minEnergy) {
        return withAnimals.stream()
                .filter(field -> field.countAnimalsWithEnergy(minEnergy) > 1)
                .map(MapField::getAtLeastTwoHealthiestAnimals)
                .collect(Collectors.toSet());
    }

    public List<Vector2d> getEmptyFieldsInsideJungle() {
        var fieldsInJungle = new ArrayList<Vector2d>();

        for (int i = jungleBorders.first.x; i <= jungleBorders.second.x; i++) {
            for (int j = jungleBorders.first.y; j <= jungleBorders.second.y; j++) {
                var position = new Vector2d(i, j);
                if (!fields.get(position).animalAt() && !fields.get(position).plantAt()) {
                    fieldsInJungle.add(position);
                }
            }
        }
        return fieldsInJungle;
    }

    public List<Vector2d> getEmptyFieldsOutsideJungle() {
        return fields.entrySet().stream()
                .filter(entry -> !inJungle(entry.getKey()))
                .filter(entry -> !entry.getValue().animalAt())
                .filter(entry -> !entry.getValue().plantAt())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Vector2d> getEmptyAdjacentFields(Vector2d centralPosition) {
        var adjacentFields = new ArrayList<Vector2d>();

        for (var direction : Direction.values()) {
            var newPosition = getNewPosition(centralPosition, direction);
            if (!fields.get(newPosition).animalAt()) {
                adjacentFields.add(newPosition);
            }
        }
        return adjacentFields;
    }

    public Vector2d getNewPosition(Vector2d currentPosition, Direction moveDirection) {
        var newPosition = currentPosition.add(moveDirection.toUnitVector());

        if (newPosition.xPrecedes(mapBorders.first)) {
            newPosition = newPosition.withX(mapBorders.second.x);
        }
        if (newPosition.xFollows(mapBorders.second)) {
            newPosition = newPosition.withX(mapBorders.first.x);
        }
        if (newPosition.yPrecedes(mapBorders.first)) {
            newPosition = newPosition.withY(mapBorders.second.y);
        }
        if (newPosition.yFollows(mapBorders.second)) {
            newPosition = newPosition.withY(mapBorders.first.y);
        }

        return newPosition;
    }

    public Pair<Vector2d, Vector2d> getMapBorders() {
        return mapBorders;
    }

    public Pair<Vector2d, Vector2d> getJungleBorders() {
        return jungleBorders;
    }

    @Override
    public void onAnimalCreated(Animal animal) {
        placeAnimal(animal);
    }

    @Override
    public void onAnimalBorn(Animal animal, Animal parent1, Animal parent2) {
        placeAnimal(animal);
    }

    @Override
    public void onAnimalDead(Animal animal) {
        removeAnimal(animal, animal.getPosition());
    }

    @Override
    public void onAnimalTurned(Animal animal, Direction prevOrientation) {
        //do nothing
    }

    @Override
    public void onAnimalMoved(Animal animal, Vector2d oldPosition) {
        removeAnimal(animal, oldPosition);
        placeAnimal(animal);
    }

    @Override
    public void onEnergyChanged(Animal animal, int oldEnergy) {
        fields.get(animal.getPosition()).removeAnimal(animal);
        fields.get(animal.getPosition()).addAnimal(animal);
    }

    @Override
    public void onPlantCreated(Plant plant) {
        fields.get(plant.getPosition()).setPlant(plant);

        withPlants.add(fields.get(plant.getPosition()));
    }

    @Override
    public void onPlantEaten(Plant plant) {
        fields.get(plant.getPosition()).removePlant();

        withPlants.remove(fields.get(plant.getPosition()));
    }

    public Set<Animal> getAnimalsWithTopEnergy() {
        return withAnimals.stream()
                .map(field -> field.getHealthiestAnimals().get(0))
                .collect(Collectors.toSet());
    }

    public Set<Plant> getPlants() {
        return withPlants.stream()
                .map(MapField::getPlant)
                .collect(Collectors.toSet());
    }


    public static WorldMap create(int width, int height, float jungleRatio) {
        var mapBorders = new Pair<>(
                new Vector2d(0, 0),
                new Vector2d(width - 1, height - 1)
        );

        int jungleWidth = (int) (width * Math.sqrt(jungleRatio));
        int jungleHeight = (int) (height * Math.sqrt(jungleRatio));

        if (jungleWidth % 2 != width % 2) {
            jungleWidth++;
        }
        if (jungleHeight % 2 != height % 2) {
            jungleHeight++;
        }

        var jungleBorders = new Pair<>(
                new Vector2d(width / 2 - jungleWidth / 2, height / 2 - jungleHeight / 2),
                new Vector2d(width / 2 - jungleWidth / 2 + jungleWidth - 1, height / 2 - jungleHeight / 2 + jungleHeight - 1)
        );

        var fields = new HashMap<Vector2d, MapField>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                var position = new Vector2d(x, y);
                fields.put(position, new MapField(position));
            }
        }

        return new WorldMap(mapBorders, jungleBorders, fields);
    }

    public Optional<Animal> getAnimalAt(Vector2d position) {
        return fields.get(position).getHealthiestAnimal();
    }

    private void placeAnimal(Animal animal) {
        fields.get(animal.getPosition()).addAnimal(animal);
        withAnimals.add(fields.get(animal.getPosition()));
    }

    private void removeAnimal(Animal animal, Vector2d position) {
        fields.get(position).removeAnimal(animal);

        if (!fields.get(position).animalAt()) {
            withAnimals.remove(fields.get(position));
        }
    }

    private boolean inJungle(Vector2d position) {
        return position.follows(jungleBorders.first) && position.precedes(jungleBorders.second);
    }
}
