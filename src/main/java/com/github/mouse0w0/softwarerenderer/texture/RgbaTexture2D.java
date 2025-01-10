package com.github.mouse0w0.softwarerenderer.texture;

import org.joml.Vector4f;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class RgbaTexture2D implements Texture2D {
    private static final float INV_SCALE = 1f / 255f;

    private final int width;
    private final int height;
    private final float[] components; // RGBA

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

        switch (image.getType()) {
            case BufferedImage.TYPE_INT_ARGB:
                this.components = intArgbToComponents(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                this.components = byteAbgrToComponents(((DataBufferByte) image.getRaster().getDataBuffer()).getData());
                break;
            default:
                int[] pixels = new int[width * height];
                image.getRGB(0, 0, width, height, pixels, 0, width);
                this.components = intArgbToComponents(pixels);
                break;
        }
    }

    private static float[] intArgbToComponents(int[] data) {
        float[] components = new float[data.length * 4];
        for (int i = 0; i < data.length; i++) {
            int offset = i * 4;
            int pixel = data[i];
            // @formatter:off
            components[offset]     = ((pixel >> 16) & 0xFF) * INV_SCALE;
            components[offset + 1] = ((pixel >> 8 ) & 0xFF) * INV_SCALE;
            components[offset + 2] = ((pixel      ) & 0xFF) * INV_SCALE;
            components[offset + 3] = ((pixel >> 24) & 0xFF) * INV_SCALE;
            // @formatter:on
        }
        return components;
    }

    private static float[] byteAbgrToComponents(byte[] data) {
        float[] components = new float[data.length];
        for (int i = 0; i < data.length; i += 4) {
            // @formatter:off
            components[i]     = (data[i + 3] & 0xFF) * INV_SCALE;
            components[i + 1] = (data[i + 2] & 0xFF) * INV_SCALE;
            components[i + 2] = (data[i + 1] & 0xFF) * INV_SCALE;
            components[i + 3] = (data[i    ] & 0xFF) * INV_SCALE;
            // @formatter:on
        }
        return components;
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
    public void fill(float red) {
        for (int i = 0; i < components.length; i += 4) {
            components[i] = red;
        }
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
    public int[] toIntArgbArray(int[] data) {
        int pixelsLength = width * height;
        if (data.length < pixelsLength) {
            throw new IllegalArgumentException("Array is too small, Actual: " + data.length + ", Expected: " + pixelsLength);
        }
        for (int i = 0; i < pixelsLength; i++) {
            int offset = i * 4;
            // @formatter:off
            data[i] =   ((int) (components[offset]     * 0xFF) & 0xFF) << 16 |
                        ((int) (components[offset + 1] * 0xFF) & 0xFF) << 8  |
                        ((int) (components[offset + 2] * 0xFF) & 0xFF)       |
                        ((int) (components[offset + 3] * 0xFF) & 0xFF) << 24 ;
            // @formatter:on
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
        for (int i = 0; i < componentsLength; i += 4) {
            // @formatter:off
            data[i    ] = (byte) ((int) (components[i + 3] * 0xFF) & 0xFF);
            data[i + 1] = (byte) ((int) (components[i + 2] * 0xFF) & 0xFF);
            data[i + 2] = (byte) ((int) (components[i + 1] * 0xFF) & 0xFF);
            data[i + 3] = (byte) ((int) (components[i    ] * 0xFF) & 0xFF);
            // @formatter:on
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
        for (int i = 0; i < componentsLength; i += 4) {
            // @formatter:off
            data[i    ] = (byte) ((int) (components[i + 2] * 0xFF) & 0xFF);
            data[i + 1] = (byte) ((int) (components[i + 1] * 0xFF) & 0xFF);
            data[i + 2] = (byte) ((int) (components[i    ] * 0xFF) & 0xFF);
            data[i + 3] = (byte) ((int) (components[i + 3] * 0xFF) & 0xFF);
            // @formatter:on
        }
        return data;
    }
}
