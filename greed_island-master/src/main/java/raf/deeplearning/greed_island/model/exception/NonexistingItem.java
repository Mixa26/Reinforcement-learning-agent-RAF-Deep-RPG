package raf.deeplearning.greed_island.model.exception;

public class NonexistingItem extends RuntimeException{

    public NonexistingItem(String unexcitingItem) {
        super("Given Item that does not exist: " + unexcitingItem);
    }

}
