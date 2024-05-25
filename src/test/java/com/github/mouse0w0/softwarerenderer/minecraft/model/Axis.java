package com.github.mouse0w0.softwarerenderer.minecraft.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Locale;

@JsonAdapter(Axis.Persister.class)
public enum Axis {
    X, Y, Z;

    public static class Persister extends TypeAdapter<Axis> {
        @Override
        public void write(JsonWriter out, Axis value) throws IOException {
            out.value(value.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public Axis read(JsonReader in) throws IOException {
            return Axis.valueOf(in.nextString().toUpperCase(Locale.ROOT));
        }
    }
}
