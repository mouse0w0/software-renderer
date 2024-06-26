package com.github.mouse0w0.softwarerenderer.minecraft.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModelLoader {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Vector3f.class, new Vector3fPersister())
            .registerTypeAdapter(Vector4f.class, new Vector4fPersister())
            .create();

    public static Model load(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return load(reader);
        }
    }

    public static Model load(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return load(reader);
        }
    }

    public static Model load(Reader reader) {
        return GSON.fromJson(reader, Model.class);
    }

    private static class Vector3fPersister extends TypeAdapter<Vector3f> {
        @Override
        public void write(JsonWriter out, Vector3f value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginArray();
                out.value(normalizeFloat(value.x));
                out.value(normalizeFloat(value.y));
                out.value(normalizeFloat(value.z));
                out.endArray();
            }
        }

        @Override
        public Vector3f read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else {
                in.beginArray();
                Vector3f v = new Vector3f((float) in.nextDouble(), (float) in.nextDouble(), (float) in.nextDouble());
                in.endArray();
                return v;
            }
        }
    }

    private static class Vector4fPersister extends TypeAdapter<Vector4f> {
        @Override
        public void write(JsonWriter out, Vector4f value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginArray();
                out.value(normalizeFloat(value.x));
                out.value(normalizeFloat(value.y));
                out.value(normalizeFloat(value.z));
                out.value(normalizeFloat(value.w));
                out.endArray();
            }
        }

        @Override
        public Vector4f read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else {
                in.beginArray();
                Vector4f v = new Vector4f(
                        (float) in.nextDouble(), (float) in.nextDouble(),
                        (float) in.nextDouble(), (float) in.nextDouble());
                in.endArray();
                return v;
            }
        }
    }

    private static Number normalizeFloat(float value) {
        int i = (int) value;
        if (i == value) return i;
        else return value;
    }
}
