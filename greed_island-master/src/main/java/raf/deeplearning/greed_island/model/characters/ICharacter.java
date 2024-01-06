package raf.deeplearning.greed_island.model.characters;

import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.model.utils.Pair;

public interface ICharacter {
    void interactWithWorld(ASpace[][] view);
    Pair getCoordinates();
    void setCoordinates(Pair p);
    char getCharacterSymbol();
    void setCurrentSpace(ASpace space);
    ASpace getCurrentSpace();
}
