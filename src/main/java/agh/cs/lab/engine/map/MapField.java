package agh.cs.lab.engine.map;

import agh.cs.lab.element.animal.Animal;
import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.element.plant.Plant;
import agh.cs.lab.shared.Vector2d;

import java.util.*;

class MapField {

    private final Vector2d position;
    private final List<Animal> animals = new LinkedList<>();

    private Plant plant = null;

    MapField(Vector2d position) {
        this.position = position;
    }

    boolean animalAt() {
        return !animals.isEmpty();
    }

    void addAnimal(Animal newAnimal) {
        int index = 0;
        for (var animal : animals) {
            if (animal.getEnergy() > newAnimal.getEnergy()) {
                index++;
            } else {
                break;
            }
        }
        animals.add(index, newAnimal);
    }

    void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    int countAnimalsWithEnergy(int energy) {
        if (animals.isEmpty()) {
            return 0;
        }

        int counter = 0;
        for (var animal : animals) {
            if (animal.getEnergy() >= energy) {
                counter++;
            } else {
                break;
            }
        }
        return counter;
    }

    Optional<Animal> getHealthiestAnimal() {
        if (animalAt()) {
            return Optional.of(animals.get(0));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Gets animals with the highest energy. This method returns at least one animal. If there is more animals with the
     * same energy, they are also returned.
     * Caller of this method must ensure that there are at least one animal on the field before invoking the method.
     *
     * @return list of animals with the same highest energy.
     * @throws MapException if there is no animal on the field
     */
    List<Animal> getHealthiestAnimals() {
        if (!animalAt()) {
            throw new MapException("There is no animals on the field");
        }

        var healthiest = new ArrayList<Animal>();
        healthiest.add(animals.get(0));

        for (var animal : animals) {
            if (animal.getEnergy() == healthiest.get(0).getEnergy()) {
                healthiest.add(animal);
            } else {
                break;
            }
        }
        return healthiest;
    }

    /**
     * Gets animals with highest energies. This method returns at least two animals. If there is more animals with the
     * same energy, they are also returned.
     * Caller of this method must ensure that there are at least two animals on the field before invoking the method.
     *
     * @return list of animals ordered by their energies from the one with the highest to the one with the lowest
     * @throws MapException if there are less than two animals on the field
     */
    List<Animal> getAtLeastTwoHealthiestAnimals() {
        if (animals.size() < 2) {
            throw new MapException("There is less than two animals on the field");
        }

        var healthiest = new ArrayList<Animal>();

        for (var animal : animals) {
            if (animal.getEnergy() >= animals.get(1).getEnergy()) {
                healthiest.add(animal);
            } else {
                break;
            }
        }
        return healthiest;
    }

    boolean isPlantAt() {
        return plant != null;
    }

    Optional<Plant> getPlantAt() {
        return Optional.ofNullable(plant);
    }

    void setPlant(Plant plant) {
        if (this.plant != null) {
            throw new MapException("Cannot set plant at the field " + position);
        }
        this.plant = plant;
    }

    void removePlant() {
        if (this.plant == null) {
            throw new MapException("Cannot remove a plant from the field " + position);
        }
        this.plant = null;
    }

    Plant getPlant() {
        return plant;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MapField) {
            return this.position.equals(((MapField) obj).position);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    boolean containsAnimalWithGenes(Collection<Gene> genes) {
        if (animals.isEmpty()) {
            return false;
        }

        for (var animal : animals) {
            if (genes.contains(animal.getGene())) {
                return true;
            }
        }
        return false;
    }

    Animal getHealthiestAnimalWithGenes(Collection<Gene> genes) {
        if (animals.isEmpty()) {
            throw new MapException("Cannot get animal from field at which is no animal " + position);
        }

        for (var animal : animals) {
            if (genes.contains(animal.getGene())) {
                return animal;
            }
        }
        throw new MapException("Unexpected error while searching for animals with genes");
    }
}
