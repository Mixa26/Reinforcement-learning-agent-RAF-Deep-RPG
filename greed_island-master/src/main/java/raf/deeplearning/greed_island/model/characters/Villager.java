package raf.deeplearning.greed_island.model.characters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raf.deeplearning.greed_island.model.GameMap;
import raf.deeplearning.greed_island.model.loot.*;
import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.model.utils.Pair;
import raf.deeplearning.greed_island.model.utils.Randomizer;

@Getter
@Setter
@AllArgsConstructor
public class Villager implements INonPlayableCharacter{

    private int x,y;
    private ASpace currentSpace;
    private transient int waitPeriod;

    private static int MaxWaitPeriod = 2;


    public Villager(int x, int y) {
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

        if(view[0][0].getOccupyingCharacter() instanceof Player) {
            GameMap.getInstance().moveCharacter(this,x-1,y);
        } else if(view[0][1].getOccupyingCharacter() instanceof Player) {
            interactWithPlayer(GameMap.getInstance().getThePlayer());
        } else if(view[0][2].getOccupyingCharacter() instanceof Player) {
            GameMap.getInstance().moveCharacter(this,x+1,y);
        } else if(view[1][0].getOccupyingCharacter() instanceof Player) {
            System.out.println("Villager: Hello there, I am a villager. I can give you a potion if you give me a coin.");
            interactWithPlayer(GameMap.getInstance().getThePlayer());
        } else if(view[1][2].getOccupyingCharacter() instanceof Player) {
            interactWithPlayer(GameMap.getInstance().getThePlayer());
        } else if(view[2][0].getOccupyingCharacter() instanceof Player) {
            GameMap.getInstance().moveCharacter(this,x-1,y);
        } else if(view[2][1].getOccupyingCharacter() instanceof Player) {
            interactWithPlayer(GameMap.getInstance().getThePlayer());
        } else if(view[2][2].getOccupyingCharacter() instanceof Player) {
            GameMap.getInstance().moveCharacter(this,x+1,y);
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
    public void setCoordinates(Pair p) {
        this.setX(p.getX1());
        this.setY(p.getX2());
    }

    @Override
    public Pair getCoordinates() {
        return new Pair(x,y);
    }

    @Override
    public void interactWithPlayer(Player player) {
        System.out.println("Villager: Hello there, I have a present for you!");
        float number = Randomizer.getInstance().randomPresent();
        ILoot loot;
        if (number > 0.95) {
            loot = new Gem();
        } else if(number > 0.55) {
            loot = new Wood();
        } else {
            loot = new Rice();
        }

        try {
            player.addItems(loot.toString(),1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public char getCharacterSymbol() {
        return 'V';
    }

    @Override
    public void setCurrentSpace(ASpace space) {
        this.currentSpace = space;
    }
}
