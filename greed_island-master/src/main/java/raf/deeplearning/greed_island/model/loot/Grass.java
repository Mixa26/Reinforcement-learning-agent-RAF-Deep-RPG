package raf.deeplearning.greed_island.model.loot;

public class Grass implements ILoot{

    private  int value;

    public Grass() {
        value = 0;
    }

    @Override
    public int getValueInGold() {
        return value;
    }


    public String toString() {
        return this.getClass().getSimpleName();
    }
}
