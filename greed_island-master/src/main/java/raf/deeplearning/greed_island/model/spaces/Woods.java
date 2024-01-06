package raf.deeplearning.greed_island.model.spaces;

import raf.deeplearning.greed_island.model.loot.*;
import raf.deeplearning.greed_island.model.utils.Randomizer;

public class Woods extends ASpace{

    public Woods(int x, int y, int z) {
        super(x,y,z,SpaceType.UNDISCOVERED,true);
    }

    @Override
    public String toString() {
        return "WOOD (" + getX() +", "+ getY()+")";
    }


    @Override
    public char getSpaceSymbol() {
        if(this.isLooted())
            return ':';
        return '+';
    }

    @Override
    public ILoot loot() {
        if(this.isLooted()) {
            return null;
        }
        this.setLooted(true);

        float number = Randomizer.getInstance().randomPresent();
        if (number > 0.95) {
            return new Stone();
        } else if(number > 0.55) {
            return new Apple();
        } else {
            return new Wood();
        }
    }
}
