package raf.deeplearning.greed_island.model.spaces.factory;

import raf.deeplearning.greed_island.model.spaces.ASpace;

public interface ISpaceFactory {

    ASpace crateSpace(int x,int y,int z);
}
