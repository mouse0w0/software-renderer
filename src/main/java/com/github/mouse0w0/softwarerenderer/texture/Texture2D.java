package com.github.mouse0w0.softwarerenderer.texture;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import org.joml.Vector4f;

import java.awt.image.BufferedImage;

public interface Texture2D {
    int getWidth();

    int getHeight();

    float[] getComponents();

    float getRed(int x, int y);

    void setRed(int x, int y, float red);

    Vector4f getPixel(int x, int y, Vector4f dest);

    void setPixel(int x, int y, Vector4f src);

    default void fill(Vector4f src) {
        fill(src.x, src.y, src.z, src.x);
    }

    void fill(float red);

    void fill(float red, float green, float blue, float alpha);

    default int[] toIntArgbArray() {
        return toIntArgbArray(new int[getWidth() * getHeight()]);
    }

    int[] toIntArgbArray(int[] pixels);

    default void blit(BufferedImage image) {
        image.setRGB(0, 0, getWidth(), getHeight(), toIntArgbArray(), 0, image.getWidth());
    }
}
