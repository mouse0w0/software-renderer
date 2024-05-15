package com.github.mouse0w0.softwarerenderer.texture;

import javafx.scene.image.Image;
import javafx.scene.image.WritablePixelFormat;
import org.joml.Vector4f;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class RgbaTexture2D implements Texture2D {
    private static final float INV_SCALE = 1f / 255f;

    private final int width;
    private final int height;
    private final float[] components;

    public RgbaTexture2D(int width, int height) {
        this.width = width;
        this.height = height;
        this.components = new float[width * height * 4];
    }

    public RgbaTexture2D(int width, int height, float[] components) {
        this.width = width;
        this.height = height;
        this.components = components;
    }

    public RgbaTexture2D(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();

        int pixelsLength = width * height;
        int[] pixels = new int[pixelsLength];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        this.components = new float[pixelsLength * 4];
        for (int i = 0; i < pixelsLength; i++) {
            int pixel = pixels[i];
            int offset = i * 4;
            // @formatter:off
            components[offset]     = ((pixel >> 16) & 0xFF) * INV_SCALE;
            components[offset + 1] = ((pixel >> 8 ) & 0xFF) * INV_SCALE;
            components[offset + 2] = ((pixel      ) & 0xFF) * INV_SCALE;
            components[offset + 3] = ((pixel >> 24) & 0xFF) * INV_SCALE;
            // @formatter:on
        }
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
        int offset = offset(x, y);
        dest.x = components[offset];
        dest.y = components[offset + 1];
        dest.z = components[offset + 2];
        dest.w = components[offset + 3];
        return dest;
    }

    @Override
    public void setPixel(int x, int y, Vector4f src) {
        int offset = offset(x, y);
        components[offset] = src.x;
        components[offset + 1] = src.y;
        components[offset + 2] = src.z;
        components[offset + 3] = src.w;
    }

    private int offset(int x, int y) {
        return (x + y * width) * 4;
    }

    @Override
    public void fill(float red, float green, float blue, float alpha) {
        for (int i = 0; i < components.length; i += 4) {
            components[i] = red;
            components[i + 1] = green;
            components[i + 2] = blue;
            components[i + 3] = alpha;
        }
    }

    @Override
    public void fill(float value) {
        Arrays.fill(components, value);
    }

    @Override
    public int[] toIntArgbArray(int[] pixels) {
        int pixelsLength = width * height;
        if (pixels.length < pixelsLength) {
            throw new IllegalArgumentException("Array is too small, Actual: " + pixels.length + ", Expected: " + pixelsLength);
        }
        for (int i = 0; i < pixelsLength; i++) {
            int offset = i * 4;
            // @formatter:off
            pixels[i] = ((int) (components[offset]     * 0xFF) & 0xFF) << 16 |
                        ((int) (components[offset + 1] * 0xFF) & 0xFF) << 8  |
                        ((int) (components[offset + 2] * 0xFF) & 0xFF)       |
                        ((int) (components[offset + 3] * 0xFF) & 0xFF) << 24 ;
            // @formatter:on
        }
        return pixels;
    }
}
