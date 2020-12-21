package agh.cs.lab.engine.map;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalObserver;
import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.element.plant.PlantObserver;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.shared.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class WorldMap implements AnimalObserver, PlantObserver {

    public final Pair<Vector2d, Vector2d> jungleBorders;
    public final Pair<Vector2d, Vector2d> mapBorders;
    private final Map<Vector2d, MapField> fields = new HashMap<>();
    private final Set<MapField> withAnimals = new HashSet<>();
    private final Set<MapField> withPlants = new HashSet<>();
    private final Set<Vector2d> emptyInsideJungle = new HashSet<>();
    private final Set<Vector2d> emptyOutsideJungle = new HashSet<>();

    private WorldMap(Pair<Vector2d, Vector2d> mapBorders, Pair<Vector2d, Vector2d> jungleBorders) {
        this.jungleBorders = jungleBorders;
        this.mapBorders = mapBorders;
    }

    public Collection<Pair<Plant, List<Animal>>> getAnimalsToFeed() {
        return withAnimals.stream()
                .filter(MapField::isPlantAt)
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
        return new ArrayList<>(emptyInsideJungle);
    }

    public List<Vector2d> getEmptyFieldsOutsideJungle() {
        return new ArrayList<>(emptyOutsideJungle);
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

        if (newPosition.xPrecedesStrongly(mapBorders.first)) {
            newPosition = newPosition.withX(mapBorders.second.x);
        }
        if (newPosition.xFollowsStrongly(mapBorders.second)) {
            newPosition = newPosition.withX(mapBorders.first.x);
        }
        if (newPosition.yPrecedesStrongly(mapBorders.first)) {
            newPosition = newPosition.withY(mapBorders.second.y);
        }
        if (newPosition.yFollowsStrongly(mapBorders.second)) {
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
        emptyOutsideJungle.remove(plant.getPosition());
        emptyInsideJungle.remove(plant.getPosition());
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

    public Set<Animal> getHealthiestAnimalsWithGenes(Collection<Gene> genes) {
        return withAnimals.stream()
                .filter(field -> field.containsAnimalWithGenes(genes))
                .map(field -> field.getHealthiestAnimalWithGenes(genes))
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

        var jungleSize = findBestJungleSize(width, height, jungleRatio);

        var jungleBorders = new Pair<>(
                new Vector2d(
                        width / 2 - jungleSize.first / 2,
                        height / 2 - jungleSize.second / 2
                ),
                new Vector2d(
                        width / 2 - jungleSize.first / 2 + jungleSize.first - 1,
                        height / 2 - jungleSize.second / 2 + jungleSize.second - 1
                )
        );

        var map = new WorldMap(mapBorders, jungleBorders);
        map.initFields();
        return map;
    }

    private void initFields() {
        int width = mapBorders.second.x - mapBorders.first.x + 1;
        int height = mapBorders.second.y - mapBorders.first.y + 1;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                var position = new Vector2d(x, y);
                fields.put(position, new MapField(position));
                if (inJungle(position)) {
                    emptyInsideJungle.add(position);
                } else {
                    emptyOutsideJungle.add(position);
                }
            }
        }
    }

    private static Pair<Integer, Integer> findBestJungleSize(int width, int height, float jungleRatio) {
        int jungleWidth = (int) Math.round(width * Math.sqrt(jungleRatio));
        int jungleHeight = (int) Math.round(height * Math.sqrt(jungleRatio));

        if (jungleWidth % 2 == width % 2 && jungleHeight % 2 == height % 2) {
            return new Pair<>(jungleWidth, jungleHeight);
        }

        float expectedArea = jungleRatio * width * height;

        if (jungleWidth % 2 != width % 2 && jungleHeight % 2 != height % 2) {
            float subAddDifference = abs((jungleWidth - 1) * (jungleHeight + 1) - expectedArea);
            float addSubDifference = abs((jungleWidth + 1) * (jungleHeight - 1) - expectedArea);

            if (subAddDifference < addSubDifference) {
                return new Pair<>(
                        jungleWidth - 1,
                        jungleHeight + 1
                );
            } else {
                return new Pair<>(
                        jungleWidth + 1,
                        jungleHeight - 1
                );
            }
        } else if (jungleWidth % 2 != width % 2) {
            float subDifference = abs((jungleWidth - 1) * jungleHeight - expectedArea);
            float addDifference = abs((jungleWidth + 1) * jungleHeight - expectedArea);

            if (subDifference < addDifference) {
                return new Pair<>(
                        jungleWidth - 1,
                        jungleHeight
                );
            } else {
                return new Pair<>(
                        jungleWidth + 1,
                        jungleHeight
                );
            }
        } else {
            float subDifference = abs(jungleWidth * (jungleHeight - 1) - expectedArea);
            float addDifference = abs(jungleWidth * (jungleHeight + 1) - expectedArea);

            if (subDifference < addDifference) {
                return new Pair<>(
                        jungleWidth,
                        jungleHeight - 1
                );
            } else {
                return new Pair<>(
                        jungleWidth,
                        jungleHeight + 1
                );
            }
        }
    }

    public Optional<Animal> getHealthiestAnimalAt(Vector2d position) {
        return fields.get(position).getHealthiestAnimal();
    }

    public Optional<Plant> getPlantAt(Vector2d position) {
        return fields.get(position).getPlantAt();
    }

    private void placeAnimal(Animal animal) {
        fields.get(animal.getPosition()).addAnimal(animal);
        withAnimals.add(fields.get(animal.getPosition()));
        emptyInsideJungle.remove(animal.getPosition());
        emptyOutsideJungle.remove(animal.getPosition());
    }

    private void removeAnimal(Animal animal, Vector2d position) {
        fields.get(position).removeAnimal(animal);

        if (!fields.get(position).animalAt()) {
            withAnimals.remove(fields.get(position));

            if (!fields.get(position).isPlantAt()) {
                if (inJungle(position)) {
                    emptyInsideJungle.add(position);
                } else {
                    emptyOutsideJungle.add(position);
                }
            }
        }
    }

    private boolean inJungle(Vector2d position) {
        return position.followsWeakly(jungleBorders.first) && position.precedesWeakly(jungleBorders.second);
    }
}
