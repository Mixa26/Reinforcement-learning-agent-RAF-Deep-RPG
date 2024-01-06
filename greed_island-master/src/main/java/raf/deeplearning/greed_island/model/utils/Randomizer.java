package raf.deeplearning.greed_island.model.utils;

import java.util.Random;

public class Randomizer {

    private static Randomizer instance;
    private final Random random;


    private Randomizer() {
        this.random = new Random();
    }

    public static Randomizer getInstance() {
        if(instance == null)
            instance = new Randomizer();
        return instance;
    }

    public float randomPresent() {
        return this.random.nextFloat();
    }
    /*
        0 - left
        1 - right
        2 - up
        3 - down
     */
    public int randomMove() {
        return this.random.nextInt(4);
    }

    public int randomInt(int bound) {
        return this.random.nextInt(bound);
    }
}
