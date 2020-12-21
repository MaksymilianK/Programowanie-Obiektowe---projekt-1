package agh.cs.lab.shared;

import java.util.Objects;

public final class Vector2d {

    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d withX(int x) {
        return new Vector2d(x, y);
    }

    public Vector2d withY(int y) {
        return new Vector2d(x, y);
    }

    public boolean xPrecedesStrongly(Vector2d other) {
        return x < other.x;
    }

    public boolean yPrecedesStrongly(Vector2d other) {
        return y < other.y;
    }

    public boolean precedesStrongly(Vector2d other) {
        return xPrecedesStrongly(other) && yPrecedesStrongly(other);
    }

    public boolean xFollowsStrongly(Vector2d other) {
        return x > other.x;
    }

    public boolean yFollowsStrongly(Vector2d other) {
        return y > other.y;
    }

    public boolean followsStrongly(Vector2d other) {
        return xFollowsStrongly(other) && yFollowsStrongly(other);
    }

    public boolean xPrecedesWeakly(Vector2d other) {
        return x <= other.x;
    }

    public boolean yPrecedesWeakly(Vector2d other) {
        return y <= other.y;
    }

    public boolean precedesWeakly(Vector2d other) {
        return xPrecedesWeakly(other) && yPrecedesWeakly(other);
    }

    public boolean xFollowsWeakly(Vector2d other) {
        return x >= other.x;
    }

    public boolean yFollowsWeakly(Vector2d other) {
        return y >= other.y;
    }

    public boolean followsWeakly(Vector2d other) {
        return xFollowsWeakly(other) && yFollowsWeakly(other);
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(
                Math.max(x, other.x),
                Math.max(y, other.y)
        );
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(
                Math.min(x, other.x),
                Math.min(y, other.y)
        );
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vector2d)) {
            return false;
        }
        var otherVector = (Vector2d) other;

        return x == otherVector.x && y == otherVector.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
