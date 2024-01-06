package raf.deeplearning.greed_island.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import raf.deeplearning.greed_island.Paths;
import raf.deeplearning.greed_island.model.GameMap;
import raf.deeplearning.greed_island.model.characters.ICharacter;
import raf.deeplearning.greed_island.model.characters.Player;
import raf.deeplearning.greed_island.model.spaces.ASpace;
import raf.deeplearning.greed_island.services.WorldService;

import java.io.File;
import java.util.Arrays;

@RestController
@CrossOrigin
@RequestMapping(Paths.MAP_PATH)
public class WorldController {


    private WorldService worldService;

    @Autowired
    public WorldController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping("/full")
    public String getFullMap() {
        return Arrays.toString(GameMap.getInstance().getSpaces());
    }

    @GetMapping("/full/matrix")
    public String[][] getFullMapMatrix() {
        ASpace[][] spaces = GameMap.getInstance().getSpaces();
        String[][] matrix = new String[spaces.length][spaces[0].length];

        for(int i = 0; i < spaces.length; i++) {
            for(int j = 0; j < spaces[i].length; j++) {
                matrix[i][j] = String.valueOf(spaces[i][j].getSpaceSymbol());
            }
        }

        for(ICharacter c:GameMap.getInstance().getCharacters()) {
            matrix[c.getCoordinates().getX1()][c.getCoordinates().getX2()] = String.valueOf(c.getCharacterSymbol());
        }

        Player p = GameMap.getInstance().getThePlayer();
        matrix[p.getCoordinates().getX1()][p.getCoordinates().getX2()] = String.valueOf(p.getCharacterSymbol());

//        System.err.println(matrix.length + " " + matrix[0].length);
        return matrix;
    }

    @GetMapping("/isover")
    public boolean checkIfGameOver() {
        return !GameMap.getInstance().isRunning();
    }

    @PutMapping("/restart")
    public boolean restartGame(@RequestParam(required = false, name = "map_number") Integer mapNumber) {
        if (mapNumber == null) {
            mapNumber = 1;
        }
        String map = "static"+ File.separator +"map_test_beta_"+mapNumber+".json";

        try {
            worldService.loadGameMap(map);
            worldService.rerunGame();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @PostMapping("/reset")
    public void resetMap() {
        System.out.println("RESET MAP");
        GameMap.getInstance().setRunning(false);
        worldService.rerunGame();

    }
}
