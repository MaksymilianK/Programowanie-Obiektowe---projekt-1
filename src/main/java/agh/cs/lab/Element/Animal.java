package agh.cs.lab.Element;

import agh.cs.lab.shared.Vector2d;
import agh.cs.lab.shared.MapDirection;

import java.util.List;
import java.util.Objects;

public class Animal {

    public final int id;
    public final List<Integer> genes;

    private Vector2d position;
    private MapDirection orientation;
    private int energy;

    public Animal(int id, Vector2d position, MapDirection orientation, int energy, List<Integer> genes) {
        this.id = id;
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
        this.genes = genes;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
    }

    public void subtractEnergy(int energy) {
        addEnergy(-energy);
    }

    public List<Integer> getGenes() {
        return genes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Animal) {
            if (this.id == ((Animal) obj).id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}