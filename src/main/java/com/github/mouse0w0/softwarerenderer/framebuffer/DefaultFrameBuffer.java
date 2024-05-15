package com.github.mouse0w0.softwarerenderer.framebuffer;

import com.github.mouse0w0.softwarerenderer.texture.Texture2D;
import org.joml.Vector4f;

public class DefaultFrameBuffer implements FrameBuffer {
    private Texture2D colorTexture;
    private Texture2D depthTexture;

    public DefaultFrameBuffer() {
    }

    public DefaultFrameBuffer(Texture2D colorTexture) {
        this.colorTexture = colorTexture;
    }

    public DefaultFrameBuffer(Texture2D colorTexture, Texture2D depthTexture) {
        this.colorTexture = colorTexture;
        this.depthTexture = depthTexture;
    }

    public Texture2D getColorTexture() {
        return colorTexture;
    }

    public void setColorTexture(Texture2D colorTexture) {
        this.colorTexture = colorTexture;
    }

    public Texture2D getDepthTexture() {
        return depthTexture;
    }

    public void setDepthTexture(Texture2D depthTexture) {
        this.depthTexture = depthTexture;
    }

    @Override
    public Vector4f getColor(int x, int y, Vector4f dest) {
        return colorTexture.getPixel(x, y, dest);
    }

    @Override
    public void setColor(int x, int y, Vector4f src) {
        colorTexture.setPixel(x, y, src);
    }

    @Override
    public void clearColor(Vector4f color) {
        colorTexture.fill(color);
    }

    @Override
    public void clearColor(float red, float green, float blue, float alpha) {
        colorTexture.fill(red, green, blue, alpha);
    }

    @Override
    public float getDepth(int x, int y) {
        return depthTexture.getRed(x, y);
    }

    @Override
    public void setDepth(int x, int y, float depth) {
        depthTexture.setRed(x, y, depth);
    }

    @Override
    public void clearDepth(float depth) {
        depthTexture.fill(depth);
    }
}
