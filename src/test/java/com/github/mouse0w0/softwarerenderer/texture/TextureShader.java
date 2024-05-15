package com.github.mouse0w0.softwarerenderer.texture;

import com.github.mouse0w0.softwarerenderer.Shader;
import com.github.mouse0w0.softwarerenderer.sampler.DefaultSampler2D;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class TextureShader implements Shader<TextureVertex> {
    public final Matrix4f modelViewProjectionMatrix = new Matrix4f();

    public final DefaultSampler2D sampler = new DefaultSampler2D();

    @Override
    public void vertex(TextureVertex vertex) {
        vertex.position.mul(modelViewProjectionMatrix, vertex.position);
    }

    @Override
    public boolean fragment(TextureVertex fragment, Vector4f color) {
        fragment.color.mul(sampler.sample(fragment.texCoord, color), color);
        return false;
    }
}
