package raf.deeplearning.greed_island.model.spaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.deeplearning.greed_island.model.characters.ICharacter;
import raf.deeplearning.greed_island.model.loot.ILoot;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ASpace {

    private int x,y,z;
    private SpaceType type;
    private boolean reachable;
    private boolean isLooted;
    private ICharacter occupyingCharacter;

    public ASpace(int x, int y,int z,SpaceType type,boolean reachable) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.reachable = reachable;
        this.isLooted = false;
        this.occupyingCharacter = null;
    }

    @Override
    public String toString() {
        return "ASpace{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", type=" + type +
                ", reachable=" + reachable +
                '}';
    }

    public abstract ILoot loot();
    public abstract char getSpaceSymbol();

    public void setLooted(boolean looted) {
        isLooted = looted;
    }
}
