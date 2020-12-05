package agh.cs.lab.element;

import agh.cs.lab.shared.Vector2d;

import java.util.Objects;

public abstract class Entity {

    private final int id;

    private int lengthOfLife = 0;

    public Entity(int id) {
        this.id = id;
    }

    public abstract Vector2d getPosition();

    public int getId() {
        return id;
    }

    public int getLengthOfLife() {
        return lengthOfLife;
    }

    public void addEpochToLife() {
        lengthOfLife++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(getClass())) {
            return id == ((Entity) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
