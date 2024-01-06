package raf.deeplearning.greed_island.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import raf.deeplearning.greed_island.model.adapters.CharacterAdapter;
import raf.deeplearning.greed_island.model.adapters.LootAdapter;
import raf.deeplearning.greed_island.model.adapters.SpaceAdapter;
import raf.deeplearning.greed_island.model.characters.*;
import raf.deeplearning.greed_island.model.exception.UnknownCharacter;
import raf.deeplearning.greed_island.model.exception.UnknownSymbol;
import raf.deeplearning.greed_island.model.loot.ILoot;
import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.model.spaces.factory.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMapSerializer {

    private static Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(ICharacter.class, new CharacterAdapter());
        builder.registerTypeHierarchyAdapter(ASpace.class, new SpaceAdapter());
        builder.registerTypeHierarchyAdapter(ILoot.class, new LootAdapter());

        gson = builder.create();
    }

    public static void toSimpleMap(GameMap map,String path) throws Exception{

        File f = new File(path);
        if(!f.exists()) {
            f.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(map.getHeight() + " " + map.getWidth());
        bw.newLine();
        bw.write(map.getMapString());

        bw.close();
    }

    private static ASpace returnSpaceFromChar(char input, int i, int j) throws Exception{
        switch (input) {
            case '>' -> {
                return new ElevationFactory().crateSpace(i, j, 0);
            }
            case '$' -> {
                return new MountainFactory().crateSpace(i, j, 0);
            }
            case '_' -> {
                return new PastureFactory().crateSpace(i, j, 0);
            }
            case '-' -> {
                return new WaterFactory().crateSpace(i, j, 0);
            }
            case '+' -> {
                return new WoodFactory().crateSpace(i, j, 0);
            }
            case '|' -> {
                return new GateFactory().crateSpace(i, j, 0);
            }
            default -> {
                throw new UnknownSymbol(input);
            }
        }
    }

    public static GameMap fromSimpleMap(String path) throws Exception {

        BufferedReader bw = new BufferedReader(new FileReader(path));
        String sizeStr = bw.readLine();
        String[] sz = sizeStr.split(" ");
        int height = Integer.parseInt(sz[0]);
        int width = Integer.parseInt(sz[1]);
        ASpace[][] spaces = new ASpace[height][width];

        String line = bw.readLine();
        Player player = null;
        List<ICharacter> characters = new ArrayList<>();

        int i = 0;
        char prev_char = '-';
        while(line != null) {
            char[] charArray = line.toCharArray();
            System.out.println(charArray);
            for(int j=0;j<width;j++) {
//                System.out.print(charArray);
                switch (charArray[j]) {
                    case 'P' -> {
                        player = new Player(i,j);
//                        characters.add(player);
                        spaces[i][j] = returnSpaceFromChar(prev_char,i,j);
                    }
                    case 'M' -> {
                        characters.add(new Merchant(i, j, 50));
                        spaces[i][j] = returnSpaceFromChar(prev_char, i, j);
                    }
                    case 'V' -> {
                        characters.add(new Villager(i,j));
                        spaces[i][j] = returnSpaceFromChar(prev_char,i,j);
                    }
                    case 'B' -> {
                        characters.add(new Barbarian(i,j));
                        spaces[i][j] = returnSpaceFromChar(prev_char,i,j);
                    }
                    default -> {

                        spaces[i][j] = returnSpaceFromChar(charArray[j], i, j);
                        prev_char = charArray[j];
                    }

                }
            }
            line = bw.readLine();
            i++;
        }

        bw.close();

        GameMap gameMap = new GameMap(width,height);
        gameMap.setSpaces(spaces);
        gameMap.setThePlayer(player);
        gameMap.setCharacters(characters);

        return gameMap;
    }
    public static void toJsonMap(GameMap map, String path) throws IOException {
        System.out.println("Saving map to " + path);
        System.out.println(map);
        File f = new File(path);
        if(!f.exists()) {
            f.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(gson.toJson(map));

        bw.close();
    }

    public static GameMap fromJsonMap(String path) throws IOException {
//        File f = new File(path);
//        InputStream stream = GameMapSerializer.class.getResourceAsStream(path);
        Resource resource = new ClassPathResource(path);
//        Path newPath = resource.getFile().toPath();
//        String jsonContent = Files.readString(newPath);
//        if(!f.exists()) {
//            System.out.println(System.getProperty("user.dir"));
//            System.err.println(path + " does not exist");
//            System.out.println(Arrays.toString(File.listRoots()));
//            f.createNewFile();
//        }

//        return gson.fromJson(new FileReader(path), GameMap.class);
//        System.out.println(newPath);
//        return gson.fromJson(jsonContent, GameMap.class);
        try(InputStream inputStream = resource.getInputStream()) {
            return gson.fromJson(new InputStreamReader(inputStream), GameMap.class);
        }
    }

}
