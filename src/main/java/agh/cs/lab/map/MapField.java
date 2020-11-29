package agh.cs.lab.map;

import agh.cs.lab.Element.Animal;
import agh.cs.lab.shared.Vector2d;

import java.util.*;

public class MapField {

    private final Vector2d position;
    private final List<Animal> animals = new LinkedList<>();

    private boolean plant = false;

    public MapField(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public boolean animalAt() {
        return !animals.isEmpty();
    }

    public int countAnimals() {
        return animals.size();
    }

    public void addAnimal(Animal newAnimal) {
        int index = 0;
        for (var animal : animals) {
            if (animal.getEnergy() < newAnimal.getEnergy()) {
                break;
            }
        }
        animals.add(index, newAnimal);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public int countAnimalsWithEnergy(int energy) {
        if (animals.isEmpty()) {
            return 0;
        }

        int counter = 0;
        for (var animal : animals) {
            if (animal.getEnergy() < energy) {
                break;
            }
            counter++;
        }
        return counter;
    }

    /**
     * Gets animals with the highest energy. This method returns at least one animal. If there is more animals with the
     * same energy, they are also returned.
     * Caller of this method must ensure that there are at least one animal on the field before invoking the method.
     *
     * @return list of animals with the same highest energy.
     * @throws MapException if there is no animal on the field
     */
    public List<Animal> getHealthiestAnimals() {
        if (animals.isEmpty()) {
            throw new MapException("There is no animals on the field");
        }

        var healthiest = new ArrayList<Animal>();

        for (var animal : animals) {
            if (healthiest.isEmpty()) {
                healthiest.add(animal);
            }

            if (animal.getEnergy() == healthiest.get(0).getEnergy()) {
                healthiest.add(animal);
            }
        }
        return healthiest;
    }

    /**
     * Gets animals with highest energies. This method returns at least two animals. If there is more animals with the
     * same energy, they are also returned.
     * Caller of this method must ensure that there are at least two animals on the field before invoking the method.
     *
     * @returns list of animals ordered by their energies from the one with the highest to the one with the lowest
     * @throws MapException if there are less than two animals on the field
     */
    public List<Animal> getAtLeastTwoHealthiestAnimals() {
        if (animals.size() < 2) {
            throw new MapException("There is less than two animals on the field");
        }

        var healthiest = new ArrayList<Animal>();

        for (var animal : animals) {
            if (healthiest.size() < 2) {
                healthiest.add(animal);
            }

            if (animal.getEnergy() == healthiest.get(healthiest.size() - 1).getEnergy()) {
                healthiest.add(animal);
            }
        }
        return healthiest;
    }

    public boolean plantAt() {
        return plant;
    }

    public boolean removePlant() {
        if (plant) {
            plant = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean growPlant() {
        if (plant) {
            return false;
        } else {
            plant = true;
            return true;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MapField) {
            if (this.getPosition().equals(((MapField) obj).getPosition())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
