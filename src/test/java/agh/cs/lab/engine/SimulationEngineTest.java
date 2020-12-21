package agh.cs.lab.engine;

import agh.cs.lab.element.EntityFactoryFacade;
import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.AnimalFactory;
import agh.cs.lab.element.animal.GeneFactory;
import agh.cs.lab.element.plant.PlantFactory;
import agh.cs.lab.engine.map.WorldMap;
import agh.cs.lab.shared.Rand;
import agh.cs.lab.statistics.SimulationStatisticsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SimulationEngineTest {

    private WorldMap map;
    private SimulationEngine engine;

    @BeforeEach
    public void setUp() {
        var rand = new Rand();

        var settings = new SimulationSettings(
                7,
                15,
                20,
                2,
                10,
                0.3f
        );

        map = WorldMap.create(7, 15, 0.3f);
        var statistics = new SimulationStatisticsManager();
        var geneFactory = new GeneFactory(rand);

        var entityFactoryFacade = new EntityFactoryFacade(
                new AnimalFactory(20, map, statistics, rand, geneFactory),
                new PlantFactory(map, statistics)
        );

        engine = SimulationEngine.create(settings, map, entityFactoryFacade, rand, 5);
    }

    @Test
    public void testSimulationCreation() {
        var animals = engine.getAnimals();

        assertThat(engine.getCurrentEpoch()).isEqualTo(0);
        assertThat(animals).hasSize(5);
        assertThat(engine.getPlants()).isEmpty();
        for (var animal : animals) {
            assertThat(map.getHealthiestAnimalAt(animal.getPosition())).isPresent();
        }
    }

    @Test
    public void testGetByGenes() {
        var genes =
                engine.getAnimals().stream()
                        .limit(3)
                        .map(Animal::getGene)
                        .collect(Collectors.toSet());

        var withGenes = engine.getAnimalsWithGenes(genes);
        assertThat(withGenes).allMatch(animal -> genes.contains(animal.getGene()));
    }

    @Test
    public void testNextEpoch() {
        engine.nextEpoch();
        assertThat(engine.getCurrentEpoch()).isEqualTo(1);
    }

    @Test
    public void testAddPlants() {
        engine.addPlants();

        var plants = engine.getPlants();

        assertThat(plants).hasSize(2);
        for (var plant : plants) {
            assertThat(map.getPlantAt(plant.getPosition())).isPresent();
        }
    }
}
