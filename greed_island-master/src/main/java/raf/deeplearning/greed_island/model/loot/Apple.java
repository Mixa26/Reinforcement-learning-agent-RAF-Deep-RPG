package raf.deeplearning.greed_island.model.loot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Apple implements ILoot{

    private  int value;

    public Apple() {
        value = 7;
    }

    @Override
    public int getValueInGold() {
        return value;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
