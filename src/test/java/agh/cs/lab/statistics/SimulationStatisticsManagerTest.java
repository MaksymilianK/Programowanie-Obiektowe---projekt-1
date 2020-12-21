package agh.cs.lab.statistics;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Pair;
import agh.cs.lab.shared.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.Vector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SimulationStatisticsManagerTest {

    @Test
    public void test() {
        var statistics = new SimulationStatisticsManager();

        assertThat(statistics.getAverageChildren()).isEqualTo(0);
        assertThat(statistics.getAverageEnergy()).isEqualTo(0);
        assertThat(statistics.getLivingPlants()).isEqualTo(0);
        assertThat(statistics.getLivingAnimals()).isEqualTo(0);
        assertThat(statistics.getAverageLifeTime()).isEqualTo(0);
        assertThat(statistics.getMostCommonGenes()).isEmpty();
        assertThat(statistics.getMostCommonGenes(2)).isEmpty();

        var gene1 = mock(Gene.class);
        var gene2 = mock(Gene.class);

        var animal1 = Animal.create(
                1,
                new Vector2d(1, 1),
                gene1,
                Direction.EAST,
                40,
                Set.of()
        );

        var animal2 = Animal.create(
                2,
                new Vector2d(1, 1),
                gene1,
                Direction.EAST,
                20,
                Set.of()
        );

        var animal3 = Animal.create(
                3,
                new Vector2d(2, 1),
                gene2,
                Direction.WEST,
                36,
                Set.of()
        );

        var plant1 = Plant.create(1, new Vector2d(1, 1), Set.of());
        var plant2 = Plant.create(2, new Vector2d(1, 2), Set.of());

        statistics.onAnimalCreated(animal1);
        statistics.onAnimalCreated(animal2);
        statistics.onAnimalBorn(animal3, animal1, animal2);
        statistics.onPlantCreated(plant1);
        statistics.onPlantCreated(plant2);

        assertThat(statistics.getAverageChildren()).isEqualTo(1);
        assertThat(statistics.getAverageEnergy()).isEqualTo(32);
        assertThat(statistics.getLivingPlants()).isEqualTo(2);
        assertThat(statistics.getLivingAnimals()).isEqualTo(3);
        assertThat(statistics.getAverageLifeTime()).isEqualTo(0);
        assertThat(statistics.getMostCommonGenes()).containsOnly(gene1);
        assertThat(statistics.getMostCommonGenes(2)).containsOnly(new Pair<>(gene1, 2), new Pair<>(gene2, 1));

        animal2.addEpochToLife();
        statistics.onAnimalDead(animal2);
        statistics.onPlantEaten(plant1);

        assertThat(statistics.getAverageChildren()).isEqualTo(1);
        assertThat(statistics.getAverageEnergy()).isEqualTo(38);
        assertThat(statistics.getLivingPlants()).isEqualTo(1);
        assertThat(statistics.getLivingAnimals()).isEqualTo(2);
        assertThat(statistics.getAverageLifeTime()).isEqualTo(1);
        assertThat(statistics.getMostCommonGenes()).containsOnly(gene1, gene2);
        assertThat(statistics.getMostCommonGenes(2)).containsOnly(new Pair<>(gene1, 1), new Pair<>(gene2, 1));
    }
}
