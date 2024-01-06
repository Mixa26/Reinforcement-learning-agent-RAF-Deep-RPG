package raf.deeplearning.greed_island.model.spaces;

import raf.deeplearning.greed_island.model.loot.*;
import raf.deeplearning.greed_island.model.loot.Wood;
import raf.deeplearning.greed_island.model.utils.Randomizer;

public class Mountain extends ASpace{


    public Mountain(int x, int y,int z) {
        super(x,y,z,SpaceType.UNDISCOVERED,false);
    }

    @Override
    public String toString() {
        return "MOUNTAIN (" + getX() +", "+ getY()+")";
    }



    @Override
    public char getSpaceSymbol() {
        return '$';
    }

    @Override
    public ILoot loot() {
        if(this.isLooted()) {
            return null;
        }
        this.setLooted(true);

        float number = Randomizer.getInstance().randomPresent();
        if (number > 0.94) {
            return new Gem();
        } else if(number > 0.90) {
            return new Ore();
        } else if(number>0.70) {
            return new Bones();
        } else {
            return new Stone();
        }
    }
}
