package agh.cs.lab.shared;

import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class RandTest {

    @RepeatedTest(100)
    public void tetRandInt() {
        var rand = new Rand();

        assertThat(rand.randInt(5, 10)).isBetween(5, 9);
        assertThat(rand.randInt(5)).isBetween(0, 4);
    }
}
