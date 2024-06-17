package com.github.mouse0w0.softwarerenderer.util;

import com.github.mouse0w0.softwarerenderer.texture.RgbaTexture2D;
import com.github.mouse0w0.softwarerenderer.texture.Texture2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

public class FXUtils {
    private static final float INV_SCALE = 1f / 255f;

    public static RgbaTexture2D createRgbaTexture2D(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        int pixelsLength = width * height;
        int[] pixels = new int[pixelsLength];

        image.getPixelReader().getPixels(0, 0, width, height, WritablePixelFormat.getIntArgbInstance(), pixels, 0, width);

        float[] components = new float[pixelsLength * 4];
        for (int i = 0; i < pixelsLength; i++) {
            int offset = i * 4;
            int pixel = pixels[i];
            // @formatter:off
            components[offset]     = ((pixel >> 16) & 0xFF) * INV_SCALE;
            components[offset + 1] = ((pixel >> 8 ) & 0xFF) * INV_SCALE;
            components[offset + 2] = ((pixel      ) & 0xFF) * INV_SCALE;
            components[offset + 3] = ((pixel >> 24) & 0xFF) * INV_SCALE;
            // @formatter:on
        }
        return new RgbaTexture2D(width, height, components);
    }

    public static WritableImage toWritableImage(Texture2D texture) {
        return toWritableImage(texture, new WritableImage(texture.getWidth(), texture.getHeight()));
    }

    public static WritableImage toWritableImage(Texture2D texture, WritableImage image) {
        image.getPixelWriter().setPixels(0, 0, texture.getWidth(), texture.getHeight(), PixelFormat.getByteBgraInstance(), texture.toByteAbgrArray(), 0, texture.getWidth());
        return image;
    }
}
