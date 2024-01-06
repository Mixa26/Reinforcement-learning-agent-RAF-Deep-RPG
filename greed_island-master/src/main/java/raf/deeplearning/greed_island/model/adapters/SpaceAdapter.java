package raf.deeplearning.greed_island.model.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import raf.deeplearning.greed_island.model.characters.*;
import raf.deeplearning.greed_island.model.spaces.*;

import java.io.IOException;

public class SpaceAdapter extends TypeAdapter<ASpace> {
    @Override
    public void write(JsonWriter out, ASpace value) throws IOException {
        System.err.println(value);
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("className").value(value.getClass().getSimpleName());
        out.name("x").value(value.getX());
        out.name("y").value(value.getY());
        if (value instanceof Gate) {
            out.name("price").value(((Gate)value).getPriceOfEntrance());
        }
        out.endObject();
    }

    @Override
    public ASpace read(JsonReader in) throws IOException {
        ASpace space = null;
        in.beginObject();
        if(in.nextName().equals("className")) {
            String className = in.nextString();
            in.nextName();
            int x = in.nextInt();
            in.nextName();
            int y = in.nextInt();
            switch (className) {
                case "Mountain" ->
                    space = new Mountain(x,y,1);
                case "Water" ->
                    space = new Water(x,y,1);
                case "Woods" ->
                    space = new Woods(x,y,1);
                case "Pasture" ->
                    space = new Pasture(x,y,1);
                case "Elevation" ->
                    space = new Elevation(x,y,1);
                case "Gate" -> {
                    in.nextName();
                    int value = in.nextInt();
                    space = new Gate(x,y,1,value);

                }
            }
        }
        in.endObject();
        return space;
    }
}
