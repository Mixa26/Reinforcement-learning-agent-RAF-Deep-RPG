package raf.deeplearning.greed_island.model.exception;

import raf.deeplearning.greed_island.model.characters.ICharacter;

public class UnknownCharacter extends Exception{

    public UnknownCharacter(ICharacter character) {
        super("Unknown character appeared: "+character.toString());
    }
}
