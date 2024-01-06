package raf.deeplearning.greed_island.model.spaces;

import raf.deeplearning.greed_island.model.loot.*;
import raf.deeplearning.greed_island.model.loot.Wood;
import raf.deeplearning.greed_island.model.utils.Randomizer;

public class Elevation extends ASpace{

    public Elevation(int x, int y, int z) {
        super(x,y,z,SpaceType.UNDISCOVERED,true);
    }

    @Override
    public String toString() {
        return "ELEVATION (" + getX() +", "+ getY()+")";
    }

    @Override
    public char getSpaceSymbol() {
        if(this.isLooted())
            return '<';
        return '>';
    }

    @Override
    public ILoot loot() {
        if(this.isLooted()) {
            return null;
        }
        this.setLooted(true);

        float number = Randomizer.getInstance().randomPresent();
        if (number > 0.9) {
            return new Gem();
        } else if(number > 0.75) {
            return new Wood();
        } else if(number>0.40) {
            return new Bones();
        } else {
            return new Grass();
        }
    }
}
