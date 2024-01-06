package raf.deeplearning.greed_island.model.spaces.factory;

import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.model.spaces.Water;

public class WaterFactory implements ISpaceFactory{

    @Override
    public ASpace crateSpace(int x, int y, int z) {
        return new Water(x,y,z);
    }
}
