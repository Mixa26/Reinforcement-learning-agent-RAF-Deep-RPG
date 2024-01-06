package raf.deeplearning.greed_island.model.spaces;

import lombok.Getter;
import lombok.Setter;
import raf.deeplearning.greed_island.model.loot.ILoot;
import raf.deeplearning.greed_island.model.loot.Stone;


@Getter
@Setter
public class Gate extends ASpace{

    private final int priceOfEntrance;
    
    public Gate(int x, int y, int z, int priceOfEntrance) {
        super(x,y,z,SpaceType.UNDISCOVERED,true);
        this.priceOfEntrance = priceOfEntrance;
    }

    public String toString() {
        return "GATE (" + getX() +", "+ getY()+")";
    }


    @Override
    public char getSpaceSymbol() {
        return '|';
    }

    @Override
    public ILoot loot() {
        if(this.isLooted()) {
            return null;
        }
        this.setLooted(true);

        return new Stone();
    }

}
