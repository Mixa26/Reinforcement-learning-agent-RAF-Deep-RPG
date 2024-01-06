package raf.deeplearning.greed_island.model.spaces;

import raf.deeplearning.greed_island.model.loot.ILoot;

public class Water extends ASpace{
    public Water(int x, int y, int z) {
        super(x,y,z,SpaceType.UNDISCOVERED,false);
    }

    @Override
    public String toString() {
        return "WATER (" + getX() +", "+ getY()+")";
    }

    @Override
    public ILoot loot() {
        return null;
    }


    @Override
    public char getSpaceSymbol() {
        return '-';
    }
}
