package agh.cs.lab.element.animal;

import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.element.plant.PlantObserver;
import agh.cs.lab.shared.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlantTest {

    @Test
    public void testCreate() {
        var mockObserver1 = mock(PlantObserver.class);
        var mockObserver2 = mock(PlantObserver.class);

        var plant = Plant.create(1, new Vector2d(1, 1), Set.of(mockObserver1, mockObserver2));

        verify(mockObserver1).onPlantCreated(plant);
        verify(mockObserver2).onPlantCreated(plant);
    }

    @Test
    public void testEat() {
        var mockObserver1 = mock(PlantObserver.class);
        var mockObserver2 = mock(PlantObserver.class);

        var plant = Plant.create(1, new Vector2d(1, 1), Set.of(mockObserver1, mockObserver2));

        plant.eat();

        verify(mockObserver1).onPlantEaten(plant);
        verify(mockObserver2).onPlantEaten(plant);
    }
}
