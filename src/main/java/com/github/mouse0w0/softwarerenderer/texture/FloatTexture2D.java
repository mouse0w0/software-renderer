package com.github.mouse0w0.softwarerenderer.texture;

import org.joml.Vector4f;
import org.joml.Vector4i;

import java.util.Arrays;

public class FloatTexture2D implements Texture2D {
    private final int width;
    private final int height;
    private final float[] components;

    public FloatTexture2D(int width, int height) {
        this.width = width;
        this.height = height;
        this.components = new float[width * height];
    }

    public FloatTexture2D(int width, int height, float[] components) {
        this.width = width;
        this.height = height;
        this.components = components;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public float[] getComponents() {
        return components;
    }

    @Override
    public float getRed(int x, int y) {
        return components[offset(x, y)];
    }

    @Override
    public void setRed(int x, int y, float red) {
        components[offset(x, y)] = red;
    }

    @Override
    public Vector4f getPixel(int x, int y, Vector4f dest) {
        dest.x = components[offset(x, y)];
        return dest;
    }

    @Override
    public void setPixel(int x, int y, Vector4f src) {
        components[offset(x, y)] = src.x;
    }

    @Override
    public Vector4i getPixel(int x, int y, Vector4i dest) {
        dest.x = ((int) (components[offset(x, y)] * 0xFF)) & 0xFF;
        return dest;
    }

    @Override
    public void setPixel(int x, int y, Vector4i src) {
        components[offset(x, y)] = (src.x & 0xFF) * INV_SCALE;
    }

    @Override
    public int getArgb(int x, int y) {
        return ((int) (components[offset(x, y)] * 0xFF) & 0xFF) << 16;
    }

    @Override
    public void setArgb(int x, int y, int argb) {
        components[offset(x, y)] = ((argb >> 16) & 0xFF) * INV_SCALE;
    }

    private int offset(int x, int y) {
        return x + y * width;
    }

    @Override
    public void fill(Vector4f src) {
        fill(src.x);
    }

    @Override
    public void fill(Vector4i src) {
        fill((src.x & 0xFF) * INV_SCALE);
    }

    @Override
    public void fill(float red) {
        Arrays.fill(components, red);
    }

    @Override
    public void fill(float red, float green, float blue, float alpha) {
        Arrays.fill(components, red);
    }

    @Override
    public void fillArgb(int argb) {
        fill(((argb >> 16) & 0xFF) * INV_SCALE);
    }

    @Override
    public int[] toIntArgbArray(int[] data) {
        int pixelsLength = width * height;
        if (data.length < pixelsLength) {
            throw new IllegalArgumentException("Array is too small, Actual: " + data.length + ", Expected: " + pixelsLength);
        }
        for (int i = 0; i < pixelsLength; i++) {
            data[i] = ((int) (components[i] * 0xFF) & 0xFF) << 16;
        }
        return data;
    }

    @Override
    public byte[] toByteAbgrArray(byte[] data) {
        int pixelsLength = width * height;
        int componentsLength = pixelsLength * 4;
        if (data.length < componentsLength) {
            throw new IllegalArgumentException("Array is too small, Actual: " + data.length + ", Expected: " + componentsLength);
        }
        for (int i = 0; i < pixelsLength; i++) {
            data[i * 4 + 3] = (byte) ((int) (components[i] * 0xFF) & 0xFF);
        }
        return data;
    }

    @Override
    public byte[] toByteBgraArray(byte[] data) {
        int pixelsLength = width * height;
        int componentsLength = pixelsLength * 4;
        if (data.length < componentsLength) {
            throw new IllegalArgumentException("Array is too small, Actual: " + data.length + ", Expected: " + componentsLength);
        }
        for (int i = 0; i < pixelsLength; i++) {
            data[i * 4 + 2] = (byte) ((int) (components[i] * 0xFF) & 0xFF);
        }
        return data;
    }
}
