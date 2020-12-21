package agh.cs.lab.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Vector2dTest {

    private Vector2d position;

    private Vector2d position1;
    private Vector2d position2;
    private Vector2d position3;
    private Vector2d position4;
    private Vector2d position5;
    private Vector2d position6;
    private Vector2d position7;
    private Vector2d position8;
    private Vector2d position9;

    @BeforeEach
    public void setUp() {
        position = new Vector2d(0, 0);

        position1 = new Vector2d(1, 1);
        position2 = new Vector2d(-1, 1);
        position3 = new Vector2d(-1, -1);
        position4 = new Vector2d(1, -1);
        position5 = new Vector2d(0, 0);
        position6 = new Vector2d(0, 1);
        position7 = new Vector2d(-1, 0);
        position8 = new Vector2d(0, -1);
        position9 = new Vector2d(1, 0);
    }

    @Test
    public void testWithX() {
        assertThat(position.withX(5)).isEqualTo(new Vector2d(5, 0));
    }

    public void testWithY() {
        assertThat(position.withY(5)).isEqualTo(new Vector2d(0, 5));
    }

    public void testXPrecedesStrongly() {
        assertThat(position.xPrecedesStrongly(position1)).isFalse();
        assertThat(position.xPrecedesStrongly(position2)).isTrue();
        assertThat(position.xPrecedesStrongly(position3)).isTrue();
        assertThat(position.xPrecedesStrongly(position4)).isFalse();
        assertThat(position.xPrecedesStrongly(position5)).isFalse();
        assertThat(position.xPrecedesStrongly(position6)).isFalse();
        assertThat(position.xPrecedesStrongly(position7)).isTrue();
        assertThat(position.xPrecedesStrongly(position8)).isFalse();
        assertThat(position.xPrecedesStrongly(position9)).isFalse();
    }

    public void testYPrecedesStrongly() {
        assertThat(position.xPrecedesStrongly(position1)).isFalse();
        assertThat(position.xPrecedesStrongly(position2)).isFalse();
        assertThat(position.xPrecedesStrongly(position3)).isTrue();
        assertThat(position.xPrecedesStrongly(position4)).isTrue();
        assertThat(position.xPrecedesStrongly(position5)).isFalse();
        assertThat(position.xPrecedesStrongly(position6)).isFalse();
        assertThat(position.xPrecedesStrongly(position7)).isFalse();
        assertThat(position.xPrecedesStrongly(position8)).isTrue();
        assertThat(position.xPrecedesStrongly(position9)).isFalse();
    }

    public void testPrecedesStrongly() {
        assertThat(position.xPrecedesStrongly(position1)).isFalse();
        assertThat(position.xPrecedesStrongly(position2)).isFalse();
        assertThat(position.xPrecedesStrongly(position3)).isTrue();
        assertThat(position.xPrecedesStrongly(position4)).isFalse();
        assertThat(position.xPrecedesStrongly(position5)).isFalse();
        assertThat(position.xPrecedesStrongly(position6)).isFalse();
        assertThat(position.xPrecedesStrongly(position7)).isFalse();
        assertThat(position.xPrecedesStrongly(position8)).isFalse();
        assertThat(position.xPrecedesStrongly(position9)).isFalse();
    }

    public void testXFollowsStrongly() {
        assertThat(position.xPrecedesStrongly(position1)).isTrue();
        assertThat(position.xPrecedesStrongly(position2)).isFalse();
        assertThat(position.xPrecedesStrongly(position3)).isFalse();
        assertThat(position.xPrecedesStrongly(position4)).isTrue();
        assertThat(position.xPrecedesStrongly(position5)).isFalse();
        assertThat(position.xPrecedesStrongly(position6)).isFalse();
        assertThat(position.xPrecedesStrongly(position7)).isFalse();
        assertThat(position.xPrecedesStrongly(position8)).isFalse();
        assertThat(position.xPrecedesStrongly(position9)).isTrue();
    }

    public void testYFollowsStrongly() {
        assertThat(position.xPrecedesStrongly(position1)).isTrue();
        assertThat(position.xPrecedesStrongly(position2)).isTrue();
        assertThat(position.xPrecedesStrongly(position3)).isFalse();
        assertThat(position.xPrecedesStrongly(position4)).isFalse();
        assertThat(position.xPrecedesStrongly(position5)).isFalse();
        assertThat(position.xPrecedesStrongly(position6)).isTrue();
        assertThat(position.xPrecedesStrongly(position7)).isFalse();
        assertThat(position.xPrecedesStrongly(position8)).isFalse();
        assertThat(position.xPrecedesStrongly(position9)).isFalse();
    }

    public void testFollowsStrongly() {
        assertThat(position.xPrecedesStrongly(position1)).isTrue();
        assertThat(position.xPrecedesStrongly(position2)).isFalse();
        assertThat(position.xPrecedesStrongly(position3)).isFalse();
        assertThat(position.xPrecedesStrongly(position4)).isFalse();
        assertThat(position.xPrecedesStrongly(position5)).isFalse();
        assertThat(position.xPrecedesStrongly(position6)).isFalse();
        assertThat(position.xPrecedesStrongly(position7)).isFalse();
        assertThat(position.xPrecedesStrongly(position8)).isFalse();
        assertThat(position.xPrecedesStrongly(position9)).isFalse();
    }

    public void testXPrecedesWeakly() {
        assertThat(position.xPrecedesStrongly(position1)).isFalse();
        assertThat(position.xPrecedesStrongly(position2)).isTrue();
        assertThat(position.xPrecedesStrongly(position3)).isTrue();
        assertThat(position.xPrecedesStrongly(position4)).isFalse();
        assertThat(position.xPrecedesStrongly(position5)).isTrue();
        assertThat(position.xPrecedesStrongly(position6)).isTrue();
        assertThat(position.xPrecedesStrongly(position7)).isTrue();
        assertThat(position.xPrecedesStrongly(position8)).isTrue();
        assertThat(position.xPrecedesStrongly(position9)).isFalse();
    }

    public void testYPrecedesWeakly() {
        assertThat(position.xPrecedesStrongly(position1)).isFalse();
        assertThat(position.xPrecedesStrongly(position2)).isFalse();
        assertThat(position.xPrecedesStrongly(position3)).isTrue();
        assertThat(position.xPrecedesStrongly(position4)).isTrue();
        assertThat(position.xPrecedesStrongly(position5)).isTrue();
        assertThat(position.xPrecedesStrongly(position6)).isFalse();
        assertThat(position.xPrecedesStrongly(position7)).isTrue();
        assertThat(position.xPrecedesStrongly(position8)).isTrue();
        assertThat(position.xPrecedesStrongly(position9)).isTrue();
    }

    public void testPrecedesWeakly() {
        assertThat(position.xPrecedesStrongly(position1)).isFalse();
        assertThat(position.xPrecedesStrongly(position2)).isFalse();
        assertThat(position.xPrecedesStrongly(position3)).isTrue();
        assertThat(position.xPrecedesStrongly(position4)).isFalse();
        assertThat(position.xPrecedesStrongly(position5)).isTrue();
        assertThat(position.xPrecedesStrongly(position6)).isFalse();
        assertThat(position.xPrecedesStrongly(position7)).isTrue();
        assertThat(position.xPrecedesStrongly(position8)).isTrue();
        assertThat(position.xPrecedesStrongly(position9)).isFalse();
    }

    public void testXFollowsWeakly() {
        assertThat(position.xPrecedesStrongly(position1)).isTrue();
        assertThat(position.xPrecedesStrongly(position2)).isFalse();
        assertThat(position.xPrecedesStrongly(position3)).isFalse();
        assertThat(position.xPrecedesStrongly(position4)).isTrue();
        assertThat(position.xPrecedesStrongly(position5)).isTrue();
        assertThat(position.xPrecedesStrongly(position6)).isTrue();
        assertThat(position.xPrecedesStrongly(position7)).isFalse();
        assertThat(position.xPrecedesStrongly(position8)).isTrue();
        assertThat(position.xPrecedesStrongly(position9)).isTrue();
    }

    public void testYFollowsWeakly() {
        assertThat(position.xPrecedesStrongly(position1)).isTrue();
        assertThat(position.xPrecedesStrongly(position2)).isTrue();
        assertThat(position.xPrecedesStrongly(position3)).isFalse();
        assertThat(position.xPrecedesStrongly(position4)).isFalse();
        assertThat(position.xPrecedesStrongly(position5)).isTrue();
        assertThat(position.xPrecedesStrongly(position6)).isTrue();
        assertThat(position.xPrecedesStrongly(position7)).isTrue();
        assertThat(position.xPrecedesStrongly(position8)).isFalse();
        assertThat(position.xPrecedesStrongly(position9)).isTrue();
    }

    public void testFollowsWeakly() {
        assertThat(position.xPrecedesStrongly(position1)).isTrue();
        assertThat(position.xPrecedesStrongly(position2)).isFalse();
        assertThat(position.xPrecedesStrongly(position3)).isFalse();
        assertThat(position.xPrecedesStrongly(position4)).isFalse();
        assertThat(position.xPrecedesStrongly(position5)).isTrue();
        assertThat(position.xPrecedesStrongly(position6)).isTrue();
        assertThat(position.xPrecedesStrongly(position7)).isFalse();
        assertThat(position.xPrecedesStrongly(position8)).isFalse();
        assertThat(position.xPrecedesStrongly(position9)).isTrue();
    }

    public void testUpperRight() {
        assertThat(position.upperRight(new Vector2d(-1, 5))).isEqualTo(new Vector2d(0, 5));
    }

    public void testLowerLeft() {
        assertThat(position.upperRight(new Vector2d(-1, 5))).isEqualTo(new Vector2d(-1, 0));
    }

    public void testAdd(Vector2d other) {
        assertThat(position.add(new Vector2d(-1, 5))).isEqualTo(new Vector2d(-1, 5));
    }

    public void testSubtract(Vector2d other) {
        assertThat(position.upperRight(new Vector2d(-1, 5))).isEqualTo(new Vector2d(1, -5));
    }

    public void testOpposite() {
        assertThat((new Vector2d(5, -4)).opposite()).isEqualTo(new Vector2d(-5, 4));
    }

    public void testEquals() {
        assertThat((new Vector2d(5, -4)).equals(new Vector2d(5, -4))).isTrue();
        assertThat((new Vector2d(5, -4)).equals(new Vector2d(5, -3))).isFalse();
        assertThat((new Vector2d(5, -4)).equals(new Vector2d(6, -4))).isFalse();
    }

    public void testToString() {
        assertThat(position.toString()).isEqualTo("(0,0)");
    }
}
