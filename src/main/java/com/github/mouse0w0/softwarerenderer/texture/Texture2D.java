package com.github.mouse0w0.softwarerenderer.texture;

import org.joml.Vector4f;
import org.joml.Vector4i;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public interface Texture2D {
    float INV_SCALE = 1f / 255f;

    int getWidth();

    int getHeight();

    float[] getComponents();

    float getRed(int x, int y);

    void setRed(int x, int y, float red);

    Vector4f getPixel(int x, int y, Vector4f dest);

    void setPixel(int x, int y, Vector4f src);

    Vector4i getPixel(int x, int y, Vector4i dest);

    void setPixel(int x, int y, Vector4i src);

    int getArgb(int x, int y);

    void setArgb(int x, int y, int argb);

    void fill(Vector4f src);

    void fill(Vector4i src);

    void fill(float red);

    void fill(float red, float green, float blue, float alpha);

    void fillArgb(int argb);

    default int[] toIntArgbArray() {
        return toIntArgbArray(new int[getWidth() * getHeight()]);
    }

    int[] toIntArgbArray(int[] data);

    default byte[] toByteAbgrArray() {
        return toByteAbgrArray(new byte[getWidth() * getHeight() * 4]);
    }

    byte[] toByteAbgrArray(byte[] data);

    default byte[] toByteBgraArray() {
        return toByteBgraArray(new byte[getWidth() * getHeight() * 4]);
    }

    byte[] toByteBgraArray(byte[] data);

    default BufferedImage toBufferedImage() {
        return toBufferedImage(new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB));
    }

    default BufferedImage toBufferedImage(BufferedImage image) {
        switch (image.getType()) {
            case BufferedImage.TYPE_INT_ARGB:
                toIntArgbArray(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                toByteAbgrArray(((DataBufferByte) image.getRaster().getDataBuffer()).getData());
                break;
            default:
                image.setRGB(0, 0, getWidth(), getHeight(), toIntArgbArray(), 0, image.getWidth());
                break;
        }
        return image;
    }
}
