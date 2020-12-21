package agh.cs.lab.element;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalFactory;
import agh.cs.lab.element.animal.GeneFactory;
import agh.cs.lab.element.plant.PlantFactory;
import agh.cs.lab.engine.map.WorldMap;
import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.statistics.SimulationStatisticsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class EntityFactoryFacadeTest {

    private EntityFactoryFacade factory;
    private GeneFactory geneFactory;

    @BeforeEach
    public void setUp() {
        var rand = new Rand();

        geneFactory = new GeneFactory(rand);

        var animalFactory = new AnimalFactory(
                10,
                mock(WorldMap.class),
                mock(SimulationStatisticsManager.class),
                rand,
                geneFactory
        );

        var plantFactory = new PlantFactory(
                mock(WorldMap.class),
                mock(SimulationStatisticsManager.class)
        );

        factory = new EntityFactoryFacade(animalFactory, plantFactory);
    }

    @RepeatedTest(50)
    public void testCreateAnimal() {
        var animal = factory.createAnimal(new Vector2d(1, 1));

        assertThat(animal.getId()).isEqualTo(1);
        assertThat(animal.getEnergy()).isEqualTo(10);
        assertThat(animal.getGene().getSequence()).hasSize(32);
        assertThat(animal.getGene().getSequence()).containsOnly(0, 1, 2, 3, 4, 5, 6, 7);
        assertThat(animal.getGene().toString()).hasSize(32);
        assertThat(animal.getPosition()).isEqualTo(new Vector2d(1, 1));
    }

    @RepeatedTest(50)
    public void testGiveBirthToAnimal() {
        var animal1 = Animal.create(
                1,
                new Vector2d(1, 1),
                geneFactory.create(),
                Direction.EAST,
                50,
                Set.of()
        );

        var animal2 = Animal.create(
                2,
                new Vector2d(1, 1),
                geneFactory.create(),
                Direction.EAST,
                50,
                Set.of()
        );

        var animal = factory.giveBirthToAnimal(new Vector2d(1, 2), animal1, animal2);

        assertThat(animal.getId()).isEqualTo(1);
        assertThat(animal.getEnergy()).isEqualTo(24);
        assertThat(animal.getGene().getSequence()).hasSize(32);
        assertThat(animal.getGene().getSequence()).containsOnly(0, 1, 2, 3, 4, 5, 6, 7);
        assertThat(animal.getGene().getSequence()).contains(0, 1, 2, 3, 4, 5, 6, 7);
        assertThat(animal.getGene().toString()).hasSize(32);
        assertThat(animal.getPosition()).isEqualTo(new Vector2d(1, 2));
    }
}
