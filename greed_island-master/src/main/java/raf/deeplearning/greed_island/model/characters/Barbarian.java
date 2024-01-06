package raf.deeplearning.greed_island.model.characters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raf.deeplearning.greed_island.model.GameMap;
import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.model.utils.Pair;
import raf.deeplearning.greed_island.model.utils.Randomizer;

import java.util.Arrays;
import java.util.Map;

@Getter
@Setter
public class Barbarian implements INonPlayableCharacter{

    private int x,y;
    private ASpace currentSpace;
    private transient int waitPeriod;

    private static int MaxWaitPeriod = 1;



    public Barbarian(int x, int y) {
        this.x = x;
        this.y = y;
        this.waitPeriod = MaxWaitPeriod;
    }

    @Override
    public void interactWithWorld(ASpace[][] view) {

        if(waitPeriod<MaxWaitPeriod) {
            ++waitPeriod;
            return;
        }

        this.waitPeriod = 0;


        System.out.println(Arrays.deepToString(view));
        if(view[0][0].getOccupyingCharacter() instanceof Player) {
            GameMap.getInstance().moveCharacter(this,x,y-1);
        } else if(view[0][1].getOccupyingCharacter() instanceof Player) {
            interactWithPlayer(GameMap.getInstance().getThePlayer());
        } else if(view[0][2].getOccupyingCharacter() instanceof Player) {
            GameMap.getInstance().moveCharacter(this,x,y+1);
        } else if(view[1][0].getOccupyingCharacter() instanceof Player) {
            interactWithPlayer(GameMap.getInstance().getThePlayer());
        } else if(view[1][2].getOccupyingCharacter() instanceof Player) {
            interactWithPlayer(GameMap.getInstance().getThePlayer());
        } else if(view[2][0].getOccupyingCharacter() instanceof Player) {
            GameMap.getInstance().moveCharacter(this,x,y-1);
        } else if(view[2][1].getOccupyingCharacter() instanceof Player) {
            interactWithPlayer(GameMap.getInstance().getThePlayer());
        } else if(view[2][2].getOccupyingCharacter() instanceof Player) {
            GameMap.getInstance().moveCharacter(this,x,y+1);
        } else {
            int move = Randomizer.getInstance().randomMove();
            switch (move) {
                case 0 -> GameMap.getInstance().moveCharacter(this,x-1,y);
                case 1 -> GameMap.getInstance().moveCharacter(this,x+1,y);
                case 2 -> GameMap.getInstance().moveCharacter(this,x,y-1);
                case 3 -> GameMap.getInstance().moveCharacter(this,x,y+1);
            }
        }
    }

    @Override
    public Pair getCoordinates() {
        return new Pair(x,y);
    }

    @Override
    public void setCoordinates(Pair p) {
        this.setX(p.getX1());
        this.setY(p.getX2());
    }

    @Override
    public void interactWithPlayer(Player player) {
        System.out.println("Barbarian is taking your loot!");
        Map<String,Integer> bag = player.getBagOfLoot();
        System.out.println(bag);
        if(bag.isEmpty())
            return;
        String[] items = bag.keySet().toArray(String[]::new);

        int itemInd = Randomizer.getInstance().randomInt(items.length);

        int itemCount = bag.get(items[itemInd]);
        if(itemCount==0)
            return;

        int takeCount = Randomizer.getInstance().randomInt(itemCount);
        System.out.println("Barbarian took " + takeCount + " " + items[itemInd] + " from you!");
        try {
            player.removeItems(items[itemInd],takeCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public char getCharacterSymbol() {
        return 'B';
    }

    @Override
    public void setCurrentSpace(ASpace space) {
        this.currentSpace = space;
    }
}
