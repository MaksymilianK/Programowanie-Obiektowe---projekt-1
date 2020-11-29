package agh.cs.lab.map;

import agh.cs.lab.Element.Animal;
import agh.cs.lab.shared.MapDirection;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {

    private final int width;
    private final int height;
    private final Map<Vector2d, MapField> fields = new HashMap<>();
    private final Set<MapField> occupied = new HashSet<>();
    private final Pair<Vector2d, Vector2d> jungleBorders;

    public WorldMap(int width, int height, Pair<Vector2d, Vector2d> jungleBorders) {
        this.width = width;
        this.height = height;
        this.jungleBorders = jungleBorders;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                var position = new Vector2d(i, j);
                fields.put(position, new MapField(position));
            }
        }
    }

    public Set<Animal> getAllAnimals() {
        return occupied.stream()
                .flatMap(field -> field.getAnimals().stream())
                .collect(Collectors.toSet());
    }

    public void placeAnimal(Animal animal) {
        fields.get(animal.getPosition()).addAnimal(animal);
        occupied.add(fields.get(animal.getPosition()));
    }

    public void removeAnimals(Set<Animal> animals) {
        animals.forEach(animal -> {
            var field = fields.get(animal.getPosition());
            field.removeAnimal(animal);
        });

        removeUnoccupied();
    }

    public void moveAnimals(Set<Animal> animals) {
        animals.forEach(animal -> {
            var newPosition = getNewPosition(animal);
            animal.setPosition(newPosition);
            fields.get(newPosition).addAnimal(animal);
            occupied.add(fields.get(newPosition));
        });

        removeUnoccupied();
    }

    public Set<List<Animal>> getAnimalsToFeed() {
        return occupied.stream()
                .filter(MapField::plantAt)
                .filter(MapField::animalAt)
                .map(MapField::getHealthiestAnimals)
                .collect(Collectors.toSet());
    }

    public Set<List<Animal>> getAnimalsToProcreate(int minEnergy) {
        return occupied.stream()
                .filter(field -> field.countAnimalsWithEnergy(minEnergy) > 1)
                .map(MapField::getAtLeastTwoHealthiestAnimals)
                .collect(Collectors.toSet());
    }

    public void removePlants(Set<Vector2d> positions) {
        positions.forEach(position -> fields.get(position).removePlant());
    }

    public Set<Vector2d> getEmptyFieldsInsideJungle() {
        var fieldsInJungle = new HashSet<Vector2d>();

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

    public Set<Vector2d> getEmptyFieldsOutsideJungle() {
        return fields.entrySet().stream()
                .filter(entry -> !inJungle(entry.getKey()))
                .filter(entry -> !entry.getValue().animalAt())
                .filter(entry -> !entry.getValue().plantAt())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public List<Vector2d> getEmptyAdjacentFields(Vector2d centralPosition) {
        var adjacentFields = new ArrayList<Vector2d>();

        for (var direction : MapDirection.values()) {
            var newPosition = centralPosition.add(direction.toUnitVector());
            if (!fields.get(newPosition).animalAt()) {
                adjacentFields.add(newPosition);
            }
        }
        return adjacentFields;
    }

    private boolean inJungle(Vector2d position) {
        return position.follows(jungleBorders.first) && position.precedes(jungleBorders.second);
    }

    private Vector2d getNewPosition(Animal animal) {
        var newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());

        if (newPosition.x < 0) {
            newPosition = newPosition.withX(width - 1);
        }
        if (newPosition.y < 0) {
            newPosition = newPosition.withY(height - 1);
        }
        if (newPosition.x >= width) {
            newPosition = newPosition.withX(0);
        }
        if (newPosition.y >= height) {
            newPosition = newPosition.withY(0);
        }

        return newPosition;
    }

    private void removeUnoccupied() {
        occupied.removeIf(field -> !field.animalAt());
    }
}
