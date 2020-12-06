package agh.cs.lab.shared;

import java.util.Random;

public class Rand {

    private final Random rand = new Random();

    public int randInt(int from, int to) {
        return rand.nextInt(to - from) + from;
    }

    public int randInt(int to) {
        return rand.nextInt(to);
    }
}
