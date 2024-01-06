package raf.deeplearning.greed_island.model.loot;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gem implements ILoot{

    private  int value;

    public Gem() {
        this.value = 20;
    }

    @Override
    public int getValueInGold() {
        return value;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
