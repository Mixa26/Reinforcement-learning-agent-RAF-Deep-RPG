package raf.deeplearning.greed_island.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import raf.deeplearning.greed_island.Paths;
import raf.deeplearning.greed_island.model.GameMap;
import raf.deeplearning.greed_island.model.characters.PlayerActions;
import raf.deeplearning.greed_island.model.loot.ILoot;
import raf.deeplearning.greed_island.services.WorldService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(Paths.PLAYER_PATH)
public class PlayerController {


    @Autowired
    public PlayerController() {
        System.out.println("PLAYER CONTROLLER");
    }

    @PutMapping("/up")
    public void upAction() {
        GameMap.getInstance().getThePlayer().getBufferedActions().add(PlayerActions.UP);
    }

    @PutMapping("/down")
    public void downAction() {
        GameMap.getInstance().getThePlayer().getBufferedActions().add(PlayerActions.DOWN);
    }

    @PutMapping("/left")
    public void leftAction() {
        GameMap.getInstance().getThePlayer().getBufferedActions().add(PlayerActions.LEFT);
    }

    @PutMapping("/right")
    public void rightAction() {
        GameMap.getInstance().getThePlayer().getBufferedActions().add(PlayerActions.RIGHT);
    }

    @PutMapping("/wait")
    public void waitAction() {
        GameMap.getInstance().getThePlayer().getBufferedActions().add(PlayerActions.WAIT);
    }

    @GetMapping("/inventory")
    public Map<String, Integer> getLoot() {
        return GameMap.getInstance().getThePlayer().getBagOfLoot();
    }

    @GetMapping("/inventory/value")
    public Integer getLootValue() {
        Map<String,Integer> map =  GameMap.getInstance().getThePlayer().getBagOfLoot();
        int full_value = 0;
        for(String lootName:map.keySet()) {
            try {
                Class<? extends ILoot> loot = Class.forName("raf.deeplearning.greed_island.model.loot."+lootName).asSubclass(ILoot.class);
                full_value += loot.getConstructor().newInstance().getValueInGold() * map.get(lootName);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return full_value;
    }

    @GetMapping("/inventory/gold")
    public Integer getGold() {
        return GameMap.getInstance().getThePlayer().getCurrentAmountOfGold();
    }
}
