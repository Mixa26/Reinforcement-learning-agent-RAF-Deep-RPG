package raf.deeplearning.greed_island.model.loot;

public class Ore implements ILoot{

    private  int value;

    public Ore() {
        this.value = 14;
    }

    @Override
    public int getValueInGold() {
        return value;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
