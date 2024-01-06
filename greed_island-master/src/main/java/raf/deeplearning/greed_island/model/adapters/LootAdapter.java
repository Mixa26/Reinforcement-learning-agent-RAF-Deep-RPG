package raf.deeplearning.greed_island.model.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.context.ApplicationListener;
import raf.deeplearning.greed_island.model.loot.*;
import raf.deeplearning.greed_island.model.spaces.Mountain;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class LootAdapter extends TypeAdapter<ILoot> {
    @Override
    public void write(JsonWriter out, ILoot value) throws IOException {
        out.beginObject();
        out.name("className").value(value.getClass().getSimpleName());
        out.endObject();
    }

    @Override
    public ILoot read(JsonReader in) throws IOException {
        ILoot loot = null;
        in.beginObject();
        in.nextName();
        String className = in.nextString();
        switch (className) {
            case "Apple" -> loot = new Apple();
            case "Bones" -> loot = new Bones();
            case "Gem" -> loot = new Gem();
            case "Grass" -> loot = new Grass();
            case "Ore" -> loot = new Ore();
            case "Rice" -> loot = new Rice();
            case "Stone" -> loot = new Stone();
            case "Wood" -> loot = new Wood();
        }
        in.endObject();
        return loot;
    }
}
