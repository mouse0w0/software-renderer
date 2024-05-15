package com.github.mouse0w0.softwarerenderer.triangle;

import com.github.mouse0w0.softwarerenderer.Shader;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class TriangleShader implements Shader<TriangleVertex> {
    public final Matrix4f modelViewProjectionMatrix = new Matrix4f();

    @Override
    public void vertex(TriangleVertex vertex) {
        vertex.position.mul(modelViewProjectionMatrix);
    }

    @Override
    public boolean fragment(TriangleVertex fragment, Vector4f color) {
        color.set(fragment.color);
        return false;
    }
}
