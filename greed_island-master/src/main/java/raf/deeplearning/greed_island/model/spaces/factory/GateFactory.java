package raf.deeplearning.greed_island.model.spaces.factory;

import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.model.spaces.Gate;

public class GateFactory implements ISpaceFactory{

    @Override
    public ASpace crateSpace(int x, int y, int z) {
        return new Gate(x,y,z, 50);
    }
}
