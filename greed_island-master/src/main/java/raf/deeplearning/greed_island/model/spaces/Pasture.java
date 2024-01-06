package raf.deeplearning.greed_island.model.spaces;

import raf.deeplearning.greed_island.model.loot.*;
import raf.deeplearning.greed_island.model.loot.Wood;
import raf.deeplearning.greed_island.model.utils.Randomizer;

public class Pasture extends ASpace{

    public Pasture(int x, int y,int z) {
        super(x,y,z,SpaceType.UNDISCOVERED,true);
    }

    @Override
    public String toString() {
        return "PASTURE (" + getX() +", "+ getY()+")";
    }


    @Override
    public char getSpaceSymbol() {
        if(this.isLooted())
            return '.';
        return '_';
    }

    @Override
    public ILoot loot() {
        if (this.isLooted()) {
            return null;
        }
        this.setLooted(true);

        float number = Randomizer.getInstance().randomPresent();
        if (number > 0.98) {
            return new Gem();
        } else if (number > 0.55) {
            return new Rice();
        } else {
            return new Grass();
        }

    }
}
