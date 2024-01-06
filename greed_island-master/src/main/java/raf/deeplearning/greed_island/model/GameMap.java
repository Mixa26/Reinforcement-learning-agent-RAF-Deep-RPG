package raf.deeplearning.greed_island.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import raf.deeplearning.greed_island.model.characters.*;
import raf.deeplearning.greed_island.model.exception.UnknownCharacter;
import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.model.spaces.Gate;
import raf.deeplearning.greed_island.model.spaces.Water;
import raf.deeplearning.greed_island.model.spaces.factory.*;
import raf.deeplearning.greed_island.model.utils.Pair;
import raf.deeplearning.greed_island.model.utils.Randomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class GameMap implements Runnable{

    private int width, height;
    private ASpace[][] spaces;
    private List<ICharacter> characters;
    private Player thePlayer;

    private volatile boolean running = true;

    private transient static GameMap currentGameMap;
    private transient final Water outboundWater;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.spaces = new ASpace[height][width];
        this.outboundWater = new Water(-1,-1,-1);
        System.out.println(width + " " + height);
        System.out.println(this.spaces.length + " " + this.spaces[0].length);

        this.thePlayer = new Player(height/2-1,width/2-1);

        this.characters = new ArrayList<>();
        this.characters.add(new Barbarian(Randomizer.getInstance().randomInt(height),Randomizer.getInstance().randomInt(width)));
        this.characters.add(new Villager(Randomizer.getInstance().randomInt(height),Randomizer.getInstance().randomInt(width)));
        this.characters.add(new Merchant(Randomizer.getInstance().randomInt(height),Randomizer.getInstance().randomInt(width),50));
//        this.characters.add(thePlayer);

        ISpaceFactory[] all_available_spaces = new ISpaceFactory[]{new ElevationFactory(),new MountainFactory(),new PastureFactory(),new WaterFactory(),new WoodFactory(),};
        Random r = new Random();
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
//                this.spaces[i][j] = all_available_spaces[r.nextInt(5)].crateSpace(i,j,1);
                if (i == 0 || j == 0 || i == height-1 || j == width-1)
                    this.spaces[i][j] = this.outboundWater;
                else
                    this.spaces[i][j] = all_available_spaces[2].crateSpace(i,j,1);
            }
        }

        this.spaces[height/2][width/2] = new Gate(height/2,width/2,1, 105);

        for(ICharacter character : characters) {
            Pair p = character.getCoordinates();
            this.spaces[p.getX1()][p.getX2()].setOccupyingCharacter(character);
            character.setCurrentSpace(this.spaces[p.getX1()][p.getX2()]);
        }

        Pair p = thePlayer.getCoordinates();
        this.spaces[p.getX1()][p.getX2()].setOccupyingCharacter(thePlayer);
        thePlayer.setCurrentSpace(this.spaces[p.getX1()][p.getX2()]);

    }

    public static GameMap getInstance() {
        if (currentGameMap == null) {
            Random r = new Random();
//            currentGameMap = new GameMap(r.nextInt(20)+5,r.nextInt(20)+5);
            currentGameMap = new GameMap(10,10);
        }

        return currentGameMap;
    }


    public ASpace[][] lookupForCharacter(ICharacter character) throws Exception{
        int subMatrixSize = 3;

        int x = 0,y = 0;

        if(!this.characters.contains(character) && !character.equals(this.thePlayer)) {
            throw new UnknownCharacter(character);
        }

        Pair p = character.getCoordinates();

        x = p.getX1();
        y = p.getX2();

        ASpace[][] subMatrix = new ASpace[subMatrixSize][subMatrixSize];

        for (int i = x-1; i < x+2; i++) {
            for (int j = y-1; j < y+2; j++) {
                if(i<0 || i >= height || j<0 || j >= width)
                    subMatrix[i-x+1][j-y+1] = this.outboundWater;
                else {
                    ASpace space = this.spaces[i][j];
                    subMatrix[i - x + 1][j - y + 1] = space;
                }
            }
        }

        return subMatrix;
    }

    private void moveCharacterAfterCheck(ICharacter character,int x,int y) {
        this.spaces[x][y].setOccupyingCharacter(character);
        Pair p = character.getCoordinates();
        this.spaces[p.getX1()][p.getX2()].setOccupyingCharacter(null);
        p.setX1(x);
        p.setX2(y);
        character.setCoordinates(p);

        character.setCurrentSpace(this.spaces[p.getX1()][p.getX2()]);
    }

    public void moveCharacter(ICharacter character,int x,int y){
        if(x<0 || x >= height || y<0 || y >= width) {
            return;
        }
        if(!this.spaces[x][y].isReachable() || this.spaces[x][y].getOccupyingCharacter() != null)
            return;

        moveCharacterAfterCheck(character,x,y);
    }

    public void movePlayer(Player character,int x,int y){
        if(x<0 || x >= height || y<0 || y >= width) {
            return;
        }

        if(!this.spaces[x][y].isReachable())
            return;

        if(this.spaces[x][y].getOccupyingCharacter() != null) {
            ICharacter otherCharacter = this.spaces[x][y].getOccupyingCharacter();
            if(otherCharacter instanceof INonPlayableCharacter)
                ((INonPlayableCharacter) otherCharacter).interactWithPlayer(character);
            return;
        }

        moveCharacterAfterCheck(character,x,y);
    }

    public void run() {
//        try {
//            Thread.sleep(10*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for(ICharacter character:this.characters) {
            Pair p = character.getCoordinates();
            this.spaces[p.getX1()][p.getX2()].setOccupyingCharacter(character);
            character.setCurrentSpace(this.spaces[p.getX1()][p.getX2()]);
            System.out.println(character+" is occuping  "+character.getCurrentSpace());
        }

        Pair p = thePlayer.getCoordinates();
        this.spaces[p.getX1()][p.getX2()].setOccupyingCharacter(thePlayer);
        thePlayer.setCurrentSpace(this.spaces[p.getX1()][p.getX2()]);
        System.out.println(thePlayer+" is occuping  "+thePlayer.getCurrentSpace());

        int numberOfRounds = 0;
        while (running) {
            try {
                makeARound();
//                Thread.sleep(5*10);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            ++numberOfRounds;
        }

        System.out.println("Game over in " + numberOfRounds);
    }

    public void makeARound() throws Exception {
        thePlayer.interactWithWorld(this.lookupForCharacter(thePlayer));

        for(ICharacter ch:characters) {
//            System.out.println(ch+" is making a move in world  "+this.hashCode());
            ch.interactWithWorld(this.lookupForCharacter(ch));
        }
//        System.out.println("TICK");
    }

    @Override
    public String toString() {
        return "Map{" +
                "spaces=" + spaces +
                ", characters=" + characters +
                '}';
    }

    public static GameMap createMap(int width, int height) {
        if(currentGameMap !=null) {
            System.err.println("Map already exists:"+ currentGameMap);
            return currentGameMap;
        }

        currentGameMap = new GameMap(width,height);
        return currentGameMap;
    }

    public String getMapString() {

        StringBuilder sb = new StringBuilder();
        for(ASpace[] arr:this.spaces) {
            sb.append(Arrays.stream(arr).map(ASpace::getSpaceSymbol)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString());
            sb.append("\n");
        }
        for(ICharacter character:characters) {
            Pair pair = character.getCoordinates();
            sb.setCharAt((pair.getX1()*(width+1)) + pair.getX2(),character.getCharacterSymbol());
        }

        Pair pair = thePlayer.getCoordinates();
        sb.setCharAt((pair.getX1()*(width+1)) + pair.getX2(),thePlayer.getCharacterSymbol());

        return sb.toString();
    }

    public void setCharacters(List<ICharacter> characters) {

        this.characters = characters;
        for(ICharacter character : this.characters) {
            System.out.println(character.toString());
            Pair p = character.getCoordinates();
            this.spaces[p.getX1()][p.getX2()].setOccupyingCharacter(character);
        }
    }

    public static void tryToSetCurrentMap(GameMap gameMap) {
        currentGameMap = gameMap;
//        if(currentGameMap == null)
//            currentGameMap = gameMap;
//        else System.err.println("Map already exists:"+ currentGameMap);
    }

    public void endGame() {
        this.running = false;
    }

    public Player getThePlayer() {
//        System.out.println("Getting the player>>>> "+thePlayer+ " <<<< in world  "+this.hashCode());
        return thePlayer;
    }

}
