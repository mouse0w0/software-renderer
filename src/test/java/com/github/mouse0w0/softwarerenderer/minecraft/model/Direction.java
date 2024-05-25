package com.github.mouse0w0.softwarerenderer.minecraft.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Locale;

@JsonAdapter(Direction.Persister.class)
public enum Direction {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static class Persister extends TypeAdapter<Direction> {
        @Override
        public void write(JsonWriter out, Direction value) throws IOException {
            out.value(value.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public Direction read(JsonReader in) throws IOException {
            return Direction.valueOf(in.nextString().toUpperCase(Locale.ROOT));
        }
    }
}
