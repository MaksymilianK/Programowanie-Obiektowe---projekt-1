package agh.cs.lab.element.animal;

import agh.cs.lab.shared.Direction;
import agh.cs.lab.shared.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class AnimalTest {

    @Test
    public void testGiveBirth() {
        var mockObserver1 = mock(AnimalObserver.class);
        var mockObserver2 = mock(AnimalObserver.class);
        var animal = Animal.create(
                1,
                new Vector2d(1, 1),
                new Gene(List.of(), ""),
                Direction.EAST,
                10,
                Set.of(mockObserver1, mockObserver2)
        );

        verify(mockObserver1).onAnimalCreated(animal);
        verify(mockObserver2).onAnimalCreated(animal);
    }

    @Test
    public void testCreate() {
        var mockObserver1 = mock(AnimalObserver.class);
        var mockObserver2 = mock(AnimalObserver.class);
        var mockParent1 = mock(Animal.class);
        var mockParent2 = mock(Animal.class);

        var animal = Animal.giveBirth(
                1,
                new Vector2d(1, 1),
                new Gene(List.of(), ""),
                Direction.EAST,
                10,
                mockParent1,
                mockParent2,
                Set.of(mockObserver1, mockObserver2)
        );

        verify(mockObserver1).onAnimalBorn(animal, mockParent1, mockParent2);
        verify(mockObserver2).onAnimalBorn(animal, mockParent1, mockParent2);
    }

    @Test
    public void testKill() {
        var mockObserver1 = mock(AnimalObserver.class);
        var mockObserver2 = mock(AnimalObserver.class);

        var animal = Animal.create(
                1,
                new Vector2d(1, 1),
                new Gene(List.of(), ""),
                Direction.EAST,
                10,
                Set.of(mockObserver1, mockObserver2)
        );

        animal.kill();

        verify(mockObserver1).onAnimalDead(animal);
        verify(mockObserver2).onAnimalDead(animal);
    }

    @Test
    public void testTurn() {
        var mockObserver1 = mock(AnimalObserver.class);
        var mockObserver2 = mock(AnimalObserver.class);

        var animal = Animal.create(
                1,
                new Vector2d(1, 1),
                new Gene(List.of(), ""),
                Direction.EAST,
                10,
                Set.of(mockObserver1, mockObserver2)
        );

        animal.turn(Direction.SOUTH);

        assertThat(animal.getOrientation()).isEqualTo(Direction.SOUTH);
        verify(mockObserver1).onAnimalTurned(animal, Direction.EAST);
        verify(mockObserver2).onAnimalTurned(animal, Direction.EAST);
    }

    @Test
    public void testMove() {
        var mockObserver1 = mock(AnimalObserver.class);
        var mockObserver2 = mock(AnimalObserver.class);

        var animal = Animal.create(
                1,
                new Vector2d(1, 1),
                new Gene(List.of(), ""),
                Direction.EAST,
                10,
                Set.of(mockObserver1, mockObserver2)
        );

        animal.move(new Vector2d(1, 2));

        assertThat(animal.getPosition()).isEqualTo(new Vector2d(1, 2));
        verify(mockObserver1).onAnimalMoved(animal, new Vector2d(1, 1));
        verify(mockObserver2).onAnimalMoved(animal, new Vector2d(1, 1));
    }

    @Test
    public void testAddEnergy() {
        var mockObserver1 = mock(AnimalObserver.class);
        var mockObserver2 = mock(AnimalObserver.class);

        var animal = Animal.create(
                1,
                new Vector2d(1, 1),
                new Gene(List.of(), ""),
                Direction.EAST,
                10,
                Set.of(mockObserver1, mockObserver2)
        );

        animal.addEnergy(5);

        assertThat(animal.getEnergy()).isEqualTo(15);
        verify(mockObserver1).onEnergyChanged(animal, 10);
        verify(mockObserver2).onEnergyChanged(animal, 10);
    }

    @Test
    public void testSubtractEnergy() {
        var mockObserver1 = mock(AnimalObserver.class);
        var mockObserver2 = mock(AnimalObserver.class);

        var animal = Animal.create(
                1,
                new Vector2d(1, 1),
                new Gene(List.of(), ""),
                Direction.EAST,
                10,
                Set.of(mockObserver1, mockObserver2)
        );

        animal.subtractEnergy(5);

        assertThat(animal.getEnergy()).isEqualTo(5);
        verify(mockObserver1).onEnergyChanged(animal, 10);
        verify(mockObserver2).onEnergyChanged(animal, 10);
    }
}
