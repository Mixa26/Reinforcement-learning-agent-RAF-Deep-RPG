package raf.deeplearning.greed_island.model.characters;

import lombok.Getter;
import lombok.Setter;
import raf.deeplearning.greed_island.model.GameMap;
import raf.deeplearning.greed_island.model.exception.NonexistingItem;
import raf.deeplearning.greed_island.model.loot.ILoot;
import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.model.spaces.Gate;
import raf.deeplearning.greed_island.model.utils.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Getter
@Setter
public class Player implements ICharacter{

    private int x,y;
    private ASpace currentSpace;

    private Map<String,Integer> bagOfLoot;
    private Integer currentAmountOfGold;
    private BlockingDeque<PlayerActions> bufferedActions;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;

        this.bagOfLoot = new HashMap<>();
        this.currentAmountOfGold = 0;
        this.bufferedActions = new LinkedBlockingDeque<>();
        System.out.println("Player is on space:  "+this.getCurrentSpace());
    }

    public Player() {
        System.out.println("Empty constructor called");
        throw new RuntimeException("Empty constructor called but shouldn't be called");
    }

    @Override
    public void interactWithWorld(ASpace[][] view) {
        PlayerActions action;
        while(true) {
            while (this.bufferedActions.isEmpty()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

            action = this.bufferedActions.poll();
            if (action == null) {
                System.err.println("Action is null");
                continue;
            }
            break;
        }
        System.out.println(action);
        switch (action) {
            case UP -> GameMap.getInstance().movePlayer(this,x-1,y);
            case DOWN -> GameMap.getInstance().movePlayer(this,x+1,y);
            case LEFT -> GameMap.getInstance().movePlayer(this,x,y-1);
            case RIGHT -> GameMap.getInstance().movePlayer(this,x,y+1);
            case WAIT -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.getCurrentSpace() == null) {
            return;
        }

        if (this.getCurrentSpace() instanceof Gate gate) {
            if(gate.getPriceOfEntrance() <= this.currentAmountOfGold)
                GameMap.getInstance().endGame();
            else {
                System.out.println("Player doesn't have enough gold to enter the gate");
            }
            return;
        }

        ILoot loot = this.getCurrentSpace().loot();
        if(loot != null) {
            try {
                addItems(loot.getClass().getSimpleName(),1);
            } catch (Exception e) {
                e.printStackTrace();
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



    public void removeItems(String item, int number) throws Exception {
        if(!this.bagOfLoot.containsKey(item)) {
            throw  new NonexistingItem(item);
        }

        int contains = this.bagOfLoot.get(item);
        number = Math.min(contains,number);
        contains -= number;
        this.bagOfLoot.put(item,contains);
    }

    public void addItems(String item, int number) throws Exception {
//        if(!this.bagOfLoot.containsKey(item)) {
//            throw  new NonexistingItem(item);
//        }

        int contains = 0;
        if (this.bagOfLoot.containsKey(item)) {
            contains = this.bagOfLoot.get(item);
        }


        this.bagOfLoot.put(item,contains+number);
    }

    @Override
    public char getCharacterSymbol() {
        return 'P';
    }

    @Override
    public void setCurrentSpace(ASpace space) {
        this.currentSpace = space;
    }

    public void addCoins(int coins) {
        if(this.currentAmountOfGold + coins < 0) {
            this.currentAmountOfGold = 0;
        }
        this.currentAmountOfGold += coins;
    }
}
