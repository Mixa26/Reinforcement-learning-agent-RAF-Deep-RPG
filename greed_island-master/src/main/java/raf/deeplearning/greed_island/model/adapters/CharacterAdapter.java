package raf.deeplearning.greed_island.model.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import raf.deeplearning.greed_island.model.characters.*;
import raf.deeplearning.greed_island.model.utils.Pair;

import java.io.IOException;

public class CharacterAdapter extends TypeAdapter<ICharacter> {

    @Override
    public void write(JsonWriter out, ICharacter value) throws IOException {
        out.beginObject();
        out.name("className").value(value.getClass().getSimpleName());
        Pair p = value.getCoordinates();
        out.name("x").value(p.getX1());
        out.name("y").value(p.getX2());
        if(value instanceof Merchant) {
            out.name("fullAmountOfGold").value(((Merchant)value).getFullAmountOfGold());
        }
        out.endObject();
    }

    @Override
    public ICharacter read(JsonReader in) throws IOException {
        ICharacter character = null;
        in.beginObject();
        if(in.nextName().equals("className")) {
            String className = in.nextString();
            System.out.println(className);
            in.nextName();
            int x = in.nextInt();
            in.nextName();
            int y = in.nextInt();
            switch (className) {
                case "Merchant" -> {
                    in.nextName();
                    int fullGold = in.nextInt();
                    character = new Merchant(x, y, fullGold);
                }
                case "Villager" ->
                    character = new Villager(x,y);
                case "Barbarian" ->
                    character = new Barbarian(x,y);
                case "Player" -> {
                    character = new Player(x, y);
//                    System.err.println("Player newwwww "+character);
//                    Thread.dumpStack();
//                    System.err.println("+++++++++++++++++++++++++++++++++++++++++++");
//                    System.err.flush();

                }
            }
        }
        in.endObject();
        return character;
    }
}
