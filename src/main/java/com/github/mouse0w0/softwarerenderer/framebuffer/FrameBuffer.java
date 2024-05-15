package com.github.mouse0w0.softwarerenderer.framebuffer;

import org.joml.Vector4f;

public interface FrameBuffer {
    Vector4f getColor(int x, int y, Vector4f dest);

    void setColor(int x, int y, Vector4f src);

    void clearColor(Vector4f color);

    void clearColor(float red, float green, float blue, float alpha);

    float getDepth(int x, int y);

    void setDepth(int x, int y, float depth);

    void clearDepth(float depth);
}
