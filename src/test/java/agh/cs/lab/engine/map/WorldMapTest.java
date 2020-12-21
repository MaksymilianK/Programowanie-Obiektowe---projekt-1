package agh.cs.lab.engine.map;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.shared.Pair;
import agh.cs.lab.shared.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorldMapTest {

    private WorldMap map;

    @BeforeEach
    public void setUp() {
        map = WorldMap.create(7, 15, 0.3f);
    }

    @Test
    public void testCreation() {
        assertThat(map.getMapBorders()).isEqualTo(new Pair<>(
                new Vector2d(0, 0),
                new Vector2d(6, 14)
        ));

        assertThat(map.getJungleBorders()).isEqualTo(new Pair<>(
                new Vector2d(1, 4),
                new Vector2d(5, 10)
        ));
    }

    @Test
    public void testGetAnimalsToFeed() {
        var animal1 = mock(Animal.class);
        var animal2 = mock(Animal.class);
        var animal3 = mock(Animal.class);
        var animal4 = mock(Animal.class);

        when(animal1.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal1.getEnergy()).thenReturn(20);

        when(animal2.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal2.getEnergy()).thenReturn(10);

        when(animal3.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal3.getEnergy()).thenReturn(20);

        when(animal4.getPosition()).thenReturn(new Vector2d(2, 2));
        when(animal4.getEnergy()).thenReturn(30);

        var plant = mock(Plant.class);
        when(plant.getPosition()).thenReturn(new Vector2d(1, 1));

        map.onAnimalCreated(animal1);
        map.onAnimalCreated(animal2);
        map.onAnimalCreated(animal3);
        map.onAnimalCreated(animal4);
        map.onPlantCreated(plant);

        var animals = map.getAnimalsToFeed();

        assertThat(animals).hasSize(1);
        var animalsAndPlant = animals.iterator().next();
        assertThat(animalsAndPlant.first).isEqualTo(plant);
        assertThat(animalsAndPlant.second).containsOnly(animal1, animal3);
    }

    @Test
    public void testGetAnimalsToProcreate() {
        var animal1 = mock(Animal.class);
        var animal2 = mock(Animal.class);
        var animal3 = mock(Animal.class);
        var animal4 = mock(Animal.class);
        var animal5 = mock(Animal.class);
        var animal6 = mock(Animal.class);

        when(animal1.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal1.getEnergy()).thenReturn(20);

        when(animal2.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal2.getEnergy()).thenReturn(10);

        when(animal3.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal3.getEnergy()).thenReturn(30);

        when(animal4.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal4.getEnergy()).thenReturn(20);

        when(animal5.getPosition()).thenReturn(new Vector2d(2, 2));
        when(animal5.getEnergy()).thenReturn(25);

        when(animal6.getPosition()).thenReturn(new Vector2d(2, 2));
        when(animal6.getEnergy()).thenReturn(19);

        map.onAnimalCreated(animal1);
        map.onAnimalCreated(animal2);
        map.onAnimalCreated(animal3);
        map.onAnimalCreated(animal4);
        map.onAnimalCreated(animal5);

        var animals = map.getAnimalsToProcreate(20);

        assertThat(animals).hasSize(1);
        var animalsOnField = animals.iterator().next();
        assertThat(animalsOnField).hasSize(3);
        assertThat(animalsOnField).containsExactly(animal3, animal4, animal1);
    }

    @Test
    public void testGetHealthiestAnimalsWithGenes() {
        var animal1 = mock(Animal.class);
        var animal2 = mock(Animal.class);
        var animal3 = mock(Animal.class);
        var animal4 = mock(Animal.class);

        var gene1 = mock(Gene.class);
        var gene2 = mock(Gene.class);

        when(animal1.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal1.getEnergy()).thenReturn(20);
        when(animal1.getGene()).thenReturn(gene1);

        when(animal2.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal2.getEnergy()).thenReturn(10);
        when(animal2.getGene()).thenReturn(gene2);

        when(animal3.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal3.getEnergy()).thenReturn(30);
        when(animal3.getGene()).thenReturn(gene1);

        when(animal4.getPosition()).thenReturn(new Vector2d(2, 2));
        when(animal4.getEnergy()).thenReturn(15);
        when(animal4.getGene()).thenReturn(gene1);

        map.onAnimalCreated(animal1);
        map.onAnimalCreated(animal2);
        map.onAnimalCreated(animal3);
        map.onAnimalCreated(animal4);

        var animals = map.getHealthiestAnimalsWithGenes(Set.of(gene1));

        assertThat(animals).hasSize(2);
        assertThat(animals).containsOnly(animal3, animal4);
    }

    @Test
    public void testGetAnimalsWithTopEnergy() {
        var animal1 = mock(Animal.class);
        var animal2 = mock(Animal.class);
        var animal3 = mock(Animal.class);
        var animal4 = mock(Animal.class);

        when(animal1.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal1.getEnergy()).thenReturn(20);

        when(animal2.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal2.getEnergy()).thenReturn(30);

        when(animal3.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal3.getEnergy()).thenReturn(25);

        when(animal4.getPosition()).thenReturn(new Vector2d(2, 2));
        when(animal4.getEnergy()).thenReturn(15);

        map.onAnimalCreated(animal1);
        map.onAnimalCreated(animal2);
        map.onAnimalCreated(animal3);
        map.onAnimalCreated(animal4);

        var animals = map.getAnimalsWithTopEnergy();

        assertThat(animals).hasSize(2);
        assertThat(animals).containsOnly(animal2, animal4);
    }

    @Test
    public void testOnAnimalDead() {
        var animal1 = mock(Animal.class);
        var animal2 = mock(Animal.class);
        var animal3 = mock(Animal.class);

        when(animal1.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal1.getEnergy()).thenReturn(20);

        when(animal2.getPosition()).thenReturn(new Vector2d(2, 2));
        when(animal2.getEnergy()).thenReturn(30);

        when(animal3.getPosition()).thenReturn(new Vector2d(1, 1));
        when(animal3.getEnergy()).thenReturn(25);

        map.onAnimalCreated(animal1);
        map.onAnimalCreated(animal2);
        map.onAnimalCreated(animal3);

        map.onAnimalDead(animal1);
        map.onAnimalDead(animal2);

        assertThat(map.getHealthiestAnimalAt(new Vector2d(1, 1))).isPresent();
        assertThat(map.getHealthiestAnimalAt(new Vector2d(1, 1)).get()).isEqualTo(animal3);
        assertThat(map.getHealthiestAnimalAt(new Vector2d(2, 2))).isEmpty();
    }

    @Test
    public void testOnPlantEaten() {
        var plant1 = mock(Plant.class);
        var plant2 = mock(Plant.class);
        var plant3 = mock(Plant.class);

        when(plant1.getPosition()).thenReturn(new Vector2d(1, 1));
        when(plant2.getPosition()).thenReturn(new Vector2d(2, 2));
        when(plant3.getPosition()).thenReturn(new Vector2d(3, 3));

        map.onPlantCreated(plant1);
        map.onPlantCreated(plant2);
        map.onPlantCreated(plant3);

        map.onPlantEaten(plant1);
        map.onPlantEaten(plant2);

        assertThat(map.getPlantAt(new Vector2d(1, 1))).isEmpty();
        assertThat(map.getPlantAt(new Vector2d(2, 2))).isEmpty();
        assertThat(map.getPlantAt(new Vector2d(3, 3))).isPresent();
    }
}
