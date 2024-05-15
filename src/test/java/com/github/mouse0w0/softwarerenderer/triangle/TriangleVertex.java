package com.github.mouse0w0.softwarerenderer.triangle;

import com.github.mouse0w0.softwarerenderer.Vertex;
import org.joml.Vector4f;

public class TriangleVertex implements Vertex<TriangleVertex> {
    public final Vector4f position = new Vector4f();
    public final Vector4f color = new Vector4f();

    @Override
    public Vector4f position() {
        return position;
    }

    @Override
    public void lerp(TriangleVertex other, float t, TriangleVertex dest) {
        position.lerp(other.position, t, dest.position);
        color.lerp(other.color, t, dest.color);
    }

    @Override
    public void perspectiveDivide() {
        float inv = 1f / position.w;

        position.x = position.x * inv;
        position.y = position.y * inv;
        position.z = position.z * inv;
        position.w = inv;

        color.mul(inv, color);
    }

    @Override
    public void beforeFragmentShader() {
        float inv = 1f / position.w;

        color.mul(inv, color);
    }
}
