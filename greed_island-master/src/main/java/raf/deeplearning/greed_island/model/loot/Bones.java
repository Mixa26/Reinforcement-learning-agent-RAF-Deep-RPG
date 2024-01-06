package raf.deeplearning.greed_island.model.loot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bones implements ILoot{

    private  int value;

    public Bones() {
        value = 2;
    }

    @Override
    public int getValueInGold() {
        return value;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
