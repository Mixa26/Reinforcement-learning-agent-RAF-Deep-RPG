package raf.deeplearning.greed_island.services;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import raf.deeplearning.greed_island.model.GameMap;
import raf.deeplearning.greed_island.model.GameMapSerializer;

import java.io.File;
import java.util.Arrays;

@Service
public class WorldService implements ApplicationRunner {

    private GameMap gameMap;

    public WorldService() {
        System.err.println("+++++++++++++++++++++++++++++++++++++++++++");
//        gameMap = GameMap.getInstance();
//        System.out.println(Arrays.deepToString(gameMap.getSpaces()));
//        System.out.println(Arrays.deepToString(gameMap.getSpaces()));
//        System.out.println(gameMap.toJson());
        String path = "/home/milan/IdeaProjects/greed_island/src/main/resources/static/map.txt";
        String json_path = "/home/milan/IdeaProjects/greed_island/src/main/resources/static/map_test.json";

//        path = "C:\\Users\\mboji\\Desktop\\greed_island\\src\\main\\resources\\static\\map_test_beta_5.txt";
//        json_path = "C:\\Users\\mboji\\Desktop\\greed_island\\src\\main\\resources\\static\\map_test_beta_5.json";

//        json_path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+ File.separator +"map_test_beta_5.json";
        json_path = "static"+ File.separator +"map_test_beta_5.json";

        try {
            loadGameMap(json_path);
        } catch (Exception e) {
            e.printStackTrace();

        }
//        try {
//            gameMap = GameMapSerializer.fromSimpleMap(path);
//            System.out.println("GAME MAP: "+ Arrays.deepToString(gameMap.getSpaces()));
//            System.out.println("SIZE " + gameMap.getSpaces().length+" ** "+gameMap.getSpaces()[0].length);
//            GameMapSerializer.toJsonMap(gameMap,json_path);
//
////            gameMap = GameMapSerializer.fromJsonMap(json_path);
////            System.err.println("GAME MAP {}: "+gameMap);
////            GameMapSerializer.toSimpleMap(gameMap,path);
//
//        } catch (Exception e) {
//            gameMap = GameMap.getInstance();
//            e.printStackTrace();
//        }
//        GameMap.tryToSetCurrentMap(gameMap);
//        gameMap = GameMap.getInstance();
//        System.err.println("GAME MAP: "+gameMap);
//        System.out.println("Player: "+gameMap.getThePlayer());
//
//        runGame();
    }


    public void loadGameMap(String path) throws Exception{
        if(path.endsWith(".txt")) {
            gameMap = GameMapSerializer.fromSimpleMap(path);
            System.out.println("GAME MAP: "+ Arrays.deepToString(gameMap.getSpaces()));
            System.out.println("SIZE " + gameMap.getSpaces().length+" ** "+gameMap.getSpaces()[0].length);
        }else if(path.endsWith(".json")) {
            gameMap = GameMapSerializer.fromJsonMap(path);
            System.err.println("GAME MAP {}: "+gameMap);
        } else {
            throw new RuntimeException("Invalid file format");
        }

        GameMap.tryToSetCurrentMap(gameMap);
        gameMap = GameMap.getInstance();
        System.err.println("GAME MAP: "+gameMap);
        System.out.println("Player: "+gameMap.getThePlayer());

        runGame();

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("World service is running");
        System.out.println("Player: "+GameMap.getInstance().getThePlayer() + " " + GameMap.getInstance());
//        runGame();
    }

    private void runGame() {
        GameMap.getInstance().setRunning(true);
        Thread t1 = new Thread(GameMap.getInstance());
        t1.start();
    }

    public void rerunGame() {
        GameMap.getInstance().setRunning(false);
        GameMap.getInstance().getThePlayer().getBufferedActions().clear();
        runGame();
    }
}
