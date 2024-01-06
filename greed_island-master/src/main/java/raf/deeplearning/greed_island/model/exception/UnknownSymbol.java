package raf.deeplearning.greed_island.model.exception;

public class UnknownSymbol extends Exception{
    public UnknownSymbol(char symbol) {
        super("Unknown character: " + symbol);
    }
}
